package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.pos.KassenAbschluss;
import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.core.SyncTimer.BulkLoadDetails;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class SynchronizePosCashBalance extends AbstractSynchronizer implements Consumer<PosCashBalance> {

	private final PosCashBalanceDAO cashBalanceDAO;
	private final PosTicketDAO ticketDAO;
	private final PosTxDAO txDAO;
	private final ObjectMapper om;

	private final KassenAbschlussDAO abschlussDao;
	private final KassenBelegDAO belegDao;
	private final KassenVorgangDAO vorgangDao;
	private final Integer limit;

	public SynchronizePosCashBalance(PosCashBalanceDAO cashBalanceDAO, PosTicketDAO ticketDAO, PosTxDAO txDAO, KassenAbschlussDAO abschlussDao, KassenBelegDAO belegDao, KassenVorgangDAO vorgangDao, Integer limit) {
		this.cashBalanceDAO = cashBalanceDAO;
		this.ticketDAO = ticketDAO;
		this.abschlussDao = abschlussDao;
		this.belegDao = belegDao;
		this.vorgangDao = vorgangDao;
		this.txDAO = txDAO;
		this.limit = limit;
		this.om = Jackson.newObjectMapper();
	}

	/**
	 * erzeuge Balances für die neu angelegten Abschlüsse
	 * 
	 * Wird vom Sync-Timer aufgerufen.
	 * 
	 * @param syncStart
	 */
	public BigDecimal fetchNewBalances(BigDecimal rowver) {
		BigDecimal res = rowver;
		Optional<String> maxId = Optional.fromNullable(cashBalanceDAO.getMaxAbschlussId());

		List<KassenAbschluss> belege = abschlussDao.fetchAllAfter(maxId.or("20140101"), limit);
		List<PosCashBalance> pcb = createNewBalances(belege);
		cashBalanceDAO.insertAll(pcb.iterator());
		PosAdapterApplication.homingQueue.addAll(pcb); // sync the new ones back home
		if (belege.isEmpty() == false) {
			res = belege.get(belege.size()-1).getZeitmarke();
			if (rowver == null || rowver.intValue() == 0) {
				rowver = res;
			}
		}
		List<KassenAbschluss> belegeChg = abschlussDao.fetchAllChangedAfter(rowver);
		if (belegeChg.isEmpty() == false) {
			BigDecimal newRes = belegeChg.get(belegeChg.size()-1).getZeitmarke();
			if (newRes.compareTo(res) > 0) {
				res = newRes;
			}
		}
		pcb = updateBalances(belegeChg);
		PosAdapterApplication.homingQueue.addAll(pcb); // sync the new ones back home
		
		return res;
	}

	
	
	private List<PosCashBalance> updateBalances(List<KassenAbschluss> abschluesse) {
		List<PosCashBalance> invs = new ArrayList<>();
		for (KassenAbschluss abschluss : abschluesse) {
			if (abschluss.getIst() != null && abschluss.getAbschlussid() != null) {
				PosCashBalance bal = cashBalanceDAO.fetchForDate(abschluss.getAbschlussid());
				if (bal != null) {
					updateBalanceValues(abschluss, bal);
					invs.add(bal);
					cashBalanceDAO.update(bal);
				}
			}
		}
		return invs;
	}

	/**
	 * erzeuge Balances für Abschlüsse in dem geg. Zeitraum
	 * @param bulkLoad
	 */
	public void doBulkLoad(BulkLoadDetails bulkLoad) {
		Logger log = LoggerFactory.getLogger("BalanceBulkLoad");
		log.info("doing " + bulkLoad);
		
		if (bulkLoad.isSendHome()) {
			log.info("sending only home ");
			List<PosCashBalance> balances = cashBalanceDAO.fetchAllBetween(bulkLoad.getFrom().toString("yyyyMMdd"), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1).toString("yyyyMMdd"));
			log.info("found " + balances.size() + " existing Balances");
			PosAdapterApplication.homingQueue.addAll(balances); // sync them back home
		} else {
			log.info("importing from pos ");
			log.info("deleting balances and everything in timespan");
			cashBalanceDAO.deleteBalancesBetween(bulkLoad.getFrom().toString("yyyyMMdd"), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1).toString("yyyyMMdd"));

			List<KassenAbschluss> belege = abschlussDao.fetchAllBetween(bulkLoad.getFrom().toDateTimeAtStartOfDay(), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1));
			log.info("found " + belege.size() + " KassenAbschlüsse");
			List<PosCashBalance> pcb = createNewBalances(belege);
			cashBalanceDAO.insertAll(pcb.iterator());
			PosAdapterApplication.homingQueue.addAll(pcb); // sync the new ones back home
		}
		log.info("done with " + bulkLoad);
	}
	
	/**
	 * erzeuge neue Balances für die geg. Abschlüsse
	 * @param abschs
	 * @return
	 */
	public List<PosCashBalance> createNewBalances(List<KassenAbschluss> abschs) {
		List<PosCashBalance> pcb = new ArrayList<PosCashBalance>();
		for (KassenAbschluss abschluss : abschs) {
			if (abschluss.getIst() != null) {
				try {
					CashBalance balComp = new CashBalance(ticketDAO);
					PosCashBalance accBal = balComp.createBalance(abschluss.getVonDatum(), abschluss.getBisDatum());
					updateBalanceValues(abschluss, accBal);
					pcb.add(accBal);
				} catch (Exception e) {
					LoggerFactory.getLogger(this.getClass()).error("problem creating balance " + abschluss.getAbschlussid(), e);
					PosAdapterApplication.problemMessages.add("problem creating balance " + abschluss.getAbschlussid());
				}
			}
		}
		return pcb;
	}

	/** 
	 * aktualisiert genau einen Kassenabschluss (kommt von resync-queue)
	 * @param accBal
	 */
	@Override
	public void accept(PosCashBalance accBal) {
		KassenAbschluss abschluss = abschlussDao.fetchForDate(accBal.getAbschlussId());
		updateBalanceValues(abschluss, accBal);

		if (accBal.getId() > 0) {
			cashBalanceDAO.update(accBal); // save to database
		} else {
			cashBalanceDAO.insert(accBal); // save to database
		}
		
		PosAdapterApplication.homingQueue.offer(accBal); // sync back home
	}


	private void updateBalanceValues(KassenAbschluss abschluss, PosCashBalance bal) {
		bal.setAbschlussId(abschluss.getAbschlussid());
    	updDate(bal::setFirstCovered, bal.getFirstCovered(), abschluss.getVonDatum());
    	updDate(bal::setLastCovered, bal.getLastCovered(), abschluss.getBisDatum());
    	// revenue wird berechnet, das wird nicht aus dem abschluss übernommen
    	// balanceSheet natürlich ebenso
		updMoney(bal::setAbsorption, bal.getAbsorption(), abschluss.getAbschoepfung());
		updMoney(bal::setCash, bal.getCash(), abschluss.getBar());
		updMoney(bal::setCashStart, bal.getCashStart(), abschluss.getAnfang());
		updMoney(bal::setCashDiff, bal.getCashDiff(), abschluss.getDifferenz());
		updMoney(bal::setCashEnd, bal.getCashEnd(), abschluss.getBar().subtract(abschluss.getAbschoepfung()));
		try {
			bal.setOrigAbschluss(om.writeValueAsString(abschluss));
		} catch (JsonProcessingException e) {
			// das  ist uns egal
		}
		// DIe Tickets wollen wir immer neu dranschreiben
		updateBalanceTickets(abschluss, bal);
	}

	private void updateBalanceTickets(KassenAbschluss abschluss, PosCashBalance bal) {
		// jetzt die Tx neu Synchronisieren
		SynchronizePosTx syncTx = new SynchronizePosTx(txDAO, vorgangDao);
		List<KassenVorgang> vorgs = vorgangDao.fetchAllBetween(bal.getFirstCovered(), bal.getLastCovered());
		List<PosTx> posTxs = syncTx.convertVorgangToTx(vorgs);
		
		// jetzt die Tickets neu synchronisieren
		SynchronizePosTicket syncTicket = new SynchronizePosTicket(ticketDAO, belegDao);
		List<KassenBeleg> belgs = belegDao.fetchAllBetween(bal.getFirstCovered(), bal.getLastCovered());
		List<PosTicket> posTickets= syncTicket.convertBelegToTicket(belgs);
		
		CashBalance balComp = new CashBalance(ticketDAO);
		balComp.computeBalance(bal,posTxs, posTickets);

		bal.setTickets(posTickets);
		
		for (PosTicket ticket : posTickets) {
			List<PosTx> titxs = new ArrayList<>();
			for (PosTx tx : posTxs) {
				if (tx.getBelegNr() == ticket.getBelegNr()) {
					titxs.add(tx);
				}
			}
			Collections.sort(titxs);
			ticket.setTxs(titxs);
		} // wir wollen alle Tickets mitgeben
	}

	
	
}
