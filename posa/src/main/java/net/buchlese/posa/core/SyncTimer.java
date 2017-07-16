package net.buchlese.posa.core;

import java.net.MalformedURLException;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.core.cmd.AbstractCommand;
import net.buchlese.posa.core.cmd.PayOffCouponCommand;
import net.buchlese.posa.core.cmd.PayOffInvoiceCommand;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.ArtikelDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;
import net.buchlese.posa.jdbi.pos.KleinteilDAO;

import org.joda.time.LocalDate;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class SyncTimer extends TimerTask {
	
	public static class BulkLoadDetails {
		private LocalDate from;
		private LocalDate till;
		private boolean sendHome;
		public LocalDate getFrom() {
			return from;
		}
		public void setFrom(LocalDate from) {
			this.from = from;
		}
		public LocalDate getTill() {
			return till;
		}
		public void setTill(LocalDate till) {
			this.till = till;
		}
		public boolean isSendHome() {
			return sendHome;
		}
		public void setSendHome(boolean sendHome) {
			this.sendHome = sendHome;
		}
		@Override
		public String toString() {
			return "BulkLoadDetails [from=" + from + ", till=" + till + ", sendHome=" + sendHome + "]";
		}
	}

	private final DBI bofcDBI;
	private final DBI posDBI;
	private final Logger logger;
	private final Lock syncLock;
	private final PosStateGatherer psg;
	private final ServerStateGatherer ssg;
	private final PosAdapterConfiguration config;
	
	public static long lastRun;
	public static long lastRunWithDbConnection;
	public static long maxDuration;
	
	@Inject
	public SyncTimer(@Named("SyncLock") Lock l, @Named("bofcdb") DBI bofcDBI, 
			@Named("posdb") DBI posDBI, PosStateGatherer psg, ServerStateGatherer ssg,
			PosAdapterConfiguration config) throws MalformedURLException {
		this.syncLock = l;
		this.bofcDBI = bofcDBI;
		this.posDBI = posDBI;
		this.psg = psg;
		this.ssg = ssg;
		this.config = config;
		logger = LoggerFactory.getLogger(SyncTimer.class);
	}	
	
	@Override
	public void run() {
		syncLock.lock();
		lastRun = System.currentTimeMillis();
	    try(Handle bofc = bofcDBI.open();  Handle pos = posDBI.open()) {
	    	lastRunWithDbConnection = lastRun;
	    	KassenVorgangDAO vorgangDao = pos.attach(KassenVorgangDAO.class);
    	    KassenBelegDAO belegDao = pos.attach(KassenBelegDAO.class);
    	    KassenAbschlussDAO abschlussDao = pos.attach(KassenAbschlussDAO.class);

    	    KleinteilDAO kleinteilDao = pos.attach(KleinteilDAO.class);
    	    ArtikelDAO artikelDAO = pos.attach(ArtikelDAO.class);

    	    PosTxDAO posTxDao = bofc.attach(PosTxDAO.class);
    	    PosTicketDAO posTicketDao =  bofc.attach(PosTicketDAO.class);
    	    PosCashBalanceDAO posCashBalanceDao =  bofc.attach(PosCashBalanceDAO.class);

    	    PosInvoiceDAO posInvoiceDao =  bofc.attach(PosInvoiceDAO.class);
    	    PosArticleDAO posArticleDao =  bofc.attach(PosArticleDAO.class);

    	    SyncStateUtils.recordSyncRun(bofc);
    	    
    	    Integer limit = config.getDaysBack();
    	    SynchronizePosCashBalance syncBalance = new SynchronizePosCashBalance(posCashBalanceDao, posTicketDao, posTxDao, abschlussDao, belegDao, vorgangDao, limit);
    	    SynchronizePosInvoice syncInvoice = new SynchronizePosInvoice(posInvoiceDao, kleinteilDao, limit);
    	    SynchronizePosArticle syncArticle = new SynchronizePosArticle(posArticleDao, artikelDAO, limit);
    	    SynchronizePosArticleStockChange syncStockChange = new SynchronizePosArticleStockChange(posArticleDao, artikelDAO, limit);
    	    SyncStateUtils.synchronize(bofc, "balRowver", syncBalance::fetchNewBalances );
    	    SyncStateUtils.synchronize(bofc, "invRowver", syncInvoice::fetchNewAndChangedInvoices );
    	    SyncStateUtils.synchronize(bofc, "issRowver", syncInvoice::fetchNewAndChangedIssueSlips );
    	    SyncStateUtils.synchronize(bofc, "artRowver", syncArticle::fetchNewAndChangedArticles );
//    	    SyncStateUtils.synchronize(bofc, "schRowver", syncStockChange::fetchNewAndChangedStockChanges );

    		psg.gatherData();
    		ssg.delayedGatherData();
    		    		
    		// sollen welche neu synchronisiertwerden? dann mach das jetzt
    		if (PosAdapterApplication.resyncQueue.isEmpty() == false) {
    			PosAdapterApplication.resyncQueue.forEach(syncBalance);
    			PosAdapterApplication.resyncQueue.clear();
    		}

    		AbstractCommand chain = new PayOffCouponCommand(config, pos);
    		chain = chain.concat(new PayOffInvoiceCommand(config, pos));

    		if (PosAdapterApplication.commandQueue.isEmpty() == false) {
    			PosAdapterApplication.commandQueue.forEach(chain);
    			PosAdapterApplication.commandQueue.clear();
    		}

	    	long dur = System.currentTimeMillis()-lastRun;
	    	if (dur > maxDuration) maxDuration = dur;
	    } catch (Throwable t) {
	    	if ((t instanceof UnableToObtainConnectionException) == false) {
	    		logger.error("error while sync ", t);
	    		PosAdapterApplication.problemMessages.add("Sync-Problem: " + t.getMessage());
	    	}
	    } finally {
	    	syncLock.unlock();
	    }
	}
	
}