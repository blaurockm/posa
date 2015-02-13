package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.pos.KassenAbschluss;
import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class SynchronizePosCashBalance extends AbstractSynchronizer {

	private final PosCashBalanceDAO cashBalanceDAO;
	private final PosTicketDAO ticketDAO;
	private final PosTxDAO txDAO;
	private final ObjectMapper om;

	private final KassenAbschlussDAO abschlussDao;
	private final KassenBelegDAO belegDao;
	private final KassenVorgangDAO vorgangDao;
	private LocalDate syncStart;


	public SynchronizePosCashBalance(PosCashBalanceDAO cashBalanceDAO, PosTicketDAO ticketDAO, PosTxDAO txDAO, KassenAbschlussDAO abschlussDao, KassenBelegDAO belegDao, KassenVorgangDAO vorgangDao,  LocalDate syncStart) {
		this.cashBalanceDAO = cashBalanceDAO;
		this.ticketDAO = ticketDAO;
		this.abschlussDao = abschlussDao;
		this.belegDao = belegDao;
		this.vorgangDao = vorgangDao;
		this.txDAO = txDAO;
		this.om = Jackson.newObjectMapper();
		this.syncStart = syncStart;
	}


	public synchronized  void execute() throws Exception {
		Optional<String> maxId = Optional.fromNullable(cashBalanceDAO.getMaxAbschlussId());

		List<KassenAbschluss> belege = abschlussDao.fetchAllAfter(maxId.or(syncStart.toString("yyyyMMdd")));
		
		// convert KassenVorgang to posTx
		for (KassenAbschluss abschluss : belege) {
			if (abschluss.getIst() != null) {
				PosCashBalance bal = createNewBalance(abschluss);
				cashBalanceDAO.insert(bal);
			}
		}
		
//		Optional<DateTime> maxCreTi = Optional.fromNullable(cashBalanceDAO.getMaxDatum());
//		if (maxCreTi.isPresent()) {
//			belege = abschlussDao.fetchAllModified(maxCreTi.get());
//			for (KassenAbschluss abschluss : belege) {
//				PosCashBalance oldBal = cashBalanceDAO.fetchForDate(abschluss.getAbschlussid());
//				if (oldBal == null) {
//					PosCashBalance bal = createNewBalance(abschluss);
//					cashBalanceDAO.insert(bal);
//				} else {
//					updateBalance(abschluss, oldBal);
//					cashBalanceDAO.update(oldBal);
//				}
//			}
//		}
		
		if (PosAdapterApplication.resyncQueue.isEmpty() == false) {
			PosCashBalance toBeResynced = PosAdapterApplication.resyncQueue.poll();
			while (toBeResynced != null) {
				updateCashBalance(toBeResynced);
				toBeResynced = PosAdapterApplication.resyncQueue.poll();
			}
		}
	}

	/** 
	 * aktualisieret genau einen Kassenabschluss
	 * @param oldBal
	 */
	public void updateCashBalance(PosCashBalance oldBal) {
		KassenAbschluss abschluss = abschlussDao.fetchForDate(oldBal.getAbschlussId());
		updateBalance(abschluss, oldBal);
		cashBalanceDAO.update(oldBal);
	}

	private PosCashBalance createNewBalance(KassenAbschluss abschluss) {
		CashBalance balComp = new CashBalance(ticketDAO);
		PosCashBalance bal = balComp.createBalance(abschluss.getVonDatum(), abschluss.getBisDatum());
		updateBalance(abschluss, bal);
		bal.setId(abschluss.getId());
		bal.setAbschlussId(abschluss.getAbschlussid());
		return bal;
	}

	private void updateBalance(KassenAbschluss abschluss, PosCashBalance bal) {
    	updDate(bal::setFirstCovered, bal.getFirstCovered(), abschluss.getVonDatum());
    	updDate(bal::setLastCovered, bal.getLastCovered(), abschluss.getBisDatum());
    	// revenue wird berechnet, das wird nicht aus dem abschluss übernommen
    	// balanceSheet natürlich ebenso
		updMoney(bal::setAbsorption, bal.getAbsorption(), abschluss.getAbschoepfung());
		updMoney(bal::setCashStart, bal.getCashStart(), abschluss.getAnfang());
		updMoney(bal::setCashEnd, bal.getCashEnd(), abschluss.getBar().subtract(abschluss.getAbschoepfung()));
		try {
			bal.setOrigAbschluss(om.writeValueAsString(abschluss));
		} catch (JsonProcessingException e) {
			// das  ist uns egal
		}
		
		// jetzt die Tx neu Synchronisieren
		txDAO.deleteTxBetween(abschluss.getVonDatum(), abschluss.getBisDatum());
		SynchronizePosTx syncTx = new SynchronizePosTx(txDAO, vorgangDao, syncStart);
		List<KassenVorgang> vorgs = vorgangDao.fetchAllBetween(bal.getFirstCovered(), bal.getLastCovered());
		List<PosTx> posTxs = syncTx.createNewTx(vorgs);
		
		// jetzt die Tickets neu synchronisieren
		SynchronizePosTicket syncTicket = new SynchronizePosTicket(ticketDAO, belegDao, syncStart);
		ticketDAO.deleteTicketsBetween(abschluss.getVonDatum(), abschluss.getBisDatum());
		List<KassenBeleg> belgs = belegDao.fetchAllBetween(bal.getFirstCovered(), bal.getLastCovered());
		List<PosTicket> posTickets= syncTicket.createTickets(belgs);
		
		CashBalance balComp = new CashBalance(ticketDAO);
		balComp.updateBalance(bal,posTxs, posTickets);
	}



	
	
	
}
