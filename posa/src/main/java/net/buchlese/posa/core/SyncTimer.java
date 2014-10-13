package net.buchlese.posa.core;

import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncTimer extends TimerTask {
	private final DBI bofcDBI;
	private final DBI posDBI;
	private final Logger logger;
	private final Lock syncLock;
	

	public SyncTimer(Lock l, DBI bofcDBI, DBI posDBI) {
		this.syncLock = l;
		this.bofcDBI = bofcDBI;
		this.posDBI = posDBI;
		logger = LoggerFactory.getLogger(SyncTimer.class);
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
	    	SynchronizePosTicket snycTickets = new SynchronizePosTicket(posTicketDao, belegDao);
	    	SynchronizePosCashBalance syncBalance = new SynchronizePosCashBalance(posCashBalanceDao, posTicketDao, posTxDao, abschlussDao);

	    	syncTx.fetchNewTx();
	    	syncTx.updateExistingTx();

	    	snycTickets.fetchNewTickets();
	    	snycTickets.updateExistingTickets();

	    	syncBalance.execute();
	    } catch (Throwable t) {
	    	logger.info("problem with auto-sync " + t.getMessage());
	    } finally {
	    	syncLock.unlock();
	    }
	}
	
}