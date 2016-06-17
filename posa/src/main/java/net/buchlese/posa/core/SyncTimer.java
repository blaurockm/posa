package net.buchlese.posa.core;

import io.dropwizard.jdbi.args.JodaDateTimeMapper;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;
import net.buchlese.posa.jdbi.pos.KleinteilDAO;

import org.joda.time.DateTime;
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
	
	private volatile BulkLoadDetails bulkLoad; 
	
	public static long lastRun;
	public static long lastRunWithDbConnection;
	public static long maxDuration;
	
	@Inject
	public SyncTimer(@Named("SyncLock") Lock l, @Named("bofcdb") DBI bofcDBI, @Named("posdb") DBI posDBI, PosStateGatherer psg, ServerStateGatherer ssg) {
		this.syncLock = l;
		this.bofcDBI = bofcDBI;
		this.posDBI = posDBI;
		this.psg = psg;
		this.ssg = ssg;
		logger = LoggerFactory.getLogger(SyncTimer.class);
	}

	public void setBulkLoad(BulkLoadDetails det) {
		syncLock.lock();
		try {
			this.bulkLoad = det;
		} finally {
			syncLock.unlock();
		}
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

    	    PosTxDAO posTxDao = bofc.attach(PosTxDAO.class);
    	    PosTicketDAO posTicketDao =  bofc.attach(PosTicketDAO.class);
    	    PosCashBalanceDAO posCashBalanceDao =  bofc.attach(PosCashBalanceDAO.class);

    	    PosInvoiceDAO posInvoiceDao =  bofc.attach(PosInvoiceDAO.class);
    	    
    	    DateTime lastSync = DateTime.now().minusMinutes(30); // Änderungen der letzten 30 minuten
    	    List<DateTime> lastRuns = bofc.createQuery("select value from dynamicstate where key='lastsyncrun'").map(new JodaDateTimeMapper()).list();
    	    
    	    if (lastRuns.isEmpty()) {
    	    	bofc.execute("insert into dynamicstate (key, value) values('lastsyncrun', ?)", lastSync);
    	    } else {
    	    	lastSync = lastRuns.get(0);
    	    	bofc.execute("update dynamicstate set value = ? where key = 'lastsyncrun'", DateTime.now());
    	    }
    	    
	    	SynchronizePosCashBalance syncBalance = new SynchronizePosCashBalance(posCashBalanceDao, posTicketDao, posTxDao, abschlussDao, belegDao, vorgangDao);
	    	SynchronizePosInvoice syncInvoice = new SynchronizePosInvoice(posInvoiceDao, kleinteilDao);
	    	
	    	if (bulkLoad != null) {
	    		// wir wollen einen Haufen Daten rumschaufeln
	    		syncBalance.doBulkLoad(bulkLoad);
	    		bulkLoad = null; // wir löschen wieder
	    	} else {
	    		// ein normaler Aktualisierungslauf
	    		psg.gatherData();
	    		ssg.delayedGatherData();
	    		
	    		// hole neue Abschlüsse
	    		syncBalance.fetchNewBalances(lastSync);
	    		syncInvoice.fetchNewAndChangedInvoices(lastSync);
	    		
	    		// sollen welche neu synchronisiertwerden? dann mach das jetzt
	    		if (PosAdapterApplication.resyncQueue.isEmpty() == false) {
	    			PosAdapterApplication.resyncQueue.forEach(syncBalance);
	    			PosAdapterApplication.resyncQueue.clear();
	    		}
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