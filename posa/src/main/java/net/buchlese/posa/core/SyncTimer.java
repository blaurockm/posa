package net.buchlese.posa.core;

import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.LocalDate;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final LocalDate syncStart;
	
	private volatile BulkLoadDetails bulkLoad; 
	
	public SyncTimer(Lock l, DBI bofcDBI, DBI posDBI) {
		this.syncLock = l;
		this.bofcDBI = bofcDBI;
		this.posDBI = posDBI;
		logger = LoggerFactory.getLogger(SyncTimer.class);
		this.syncStart = LocalDate.now();
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
	    try(Handle bofc = bofcDBI.open();  Handle pos = posDBI.open()) {
	    	KassenVorgangDAO vorgangDao = pos.attach(KassenVorgangDAO.class);
    	    KassenBelegDAO belegDao = pos.attach(KassenBelegDAO.class);
    	    KassenAbschlussDAO abschlussDao = pos.attach(KassenAbschlussDAO.class);

    	    PosTxDAO posTxDao = bofc.attach(PosTxDAO.class);
    	    PosTicketDAO posTicketDao =  bofc.attach(PosTicketDAO.class);
    	    PosCashBalanceDAO posCashBalanceDao =  bofc.attach(PosCashBalanceDAO.class);

    	    SynchronizePosTx syncTx = new SynchronizePosTx(posTxDao, vorgangDao);
	    	SynchronizePosTicket syncTickets = new SynchronizePosTicket(posTicketDao, belegDao);
	    	SynchronizePosCashBalance syncBalance = new SynchronizePosCashBalance(posCashBalanceDao, posTicketDao, posTxDao, abschlussDao, belegDao, vorgangDao);

	    	if (bulkLoad != null) {
	    		// wir wollen einen Haufen Daten rumschaufeln
	    		syncTx.doBulkLoad(bulkLoad);
	    		syncTickets.doBulkLoad(bulkLoad);
	    		syncBalance.doBulkLoad(bulkLoad);
	    		bulkLoad = null; // wir löschen wieder
	    	} else {
	    		// ein normaler Aktualisierungslauf
	    		syncTx.fetchNewTx(syncStart);
	    		syncTickets.fetchNewTickets(syncStart);
	    		syncBalance.fetchNewBalances(syncStart);
	    		
	    		syncTx.updateLast10Tx();
	    		syncTickets.updateLast10Tickets();
	    		if (PosAdapterApplication.resyncQueue.isEmpty() == false) {
	    			PosAdapterApplication.resyncQueue.forEach(syncBalance);
	    			PosAdapterApplication.resyncQueue.clear();
	    		}
	    	}

	    } catch (Throwable t) {
	    	if (t instanceof NullPointerException) {
	    		logger.error("error while sync ", t);
	    	} else {
	    		logger.info("problem with auto-sync " + t.getMessage());
	    	}
	    } finally {
	    	syncLock.unlock();
	    }
	}
	
}