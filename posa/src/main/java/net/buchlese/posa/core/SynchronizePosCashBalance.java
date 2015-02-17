package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.util.ArrayList;
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

import org.joda.time.LocalDate;
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

	public SynchronizePosCashBalance(PosCashBalanceDAO cashBalanceDAO, PosTicketDAO ticketDAO, PosTxDAO txDAO, KassenAbschlussDAO abschlussDao, KassenBelegDAO belegDao, KassenVorgangDAO vorgangDao) {
		this.cashBalanceDAO = cashBalanceDAO;
		this.ticketDAO = ticketDAO;
		this.abschlussDao = abschlussDao;
		this.belegDao = belegDao;
		this.vorgangDao = vorgangDao;
		this.txDAO = txDAO;
		this.om = Jackson.newObjectMapper();
	}

	/**
	 * erzeuge Balances für die neu angelegten Abschlüsse
	 * 
	 * @param syncStart
	 */
	public void fetchNewBalances(LocalDate syncStart) {
		Optional<String> maxId = Optional.fromNullable(cashBalanceDAO.getMaxAbschlussId());

		List<KassenAbschluss> belege = abschlussDao.fetchAllAfter(maxId.or(syncStart.toString("yyyyMMdd")));
		
		List<PosCashBalance> pcb = createNewBalances(belege);
		PosAdapterApplication.homingQueue.addAll(pcb); // sync back home
	}

	/**
	 * erzeuge Balances für Abschlüsse in dem geg. Zeitraum
	 * @param bulkLoad
	 */
	public void doBulkLoad(BulkLoadDetails bulkLoad) {
		Logger log = LoggerFactory.getLogger("BalanceBulkLoad");
		log.info("doing " + bulkLoad);
		CashBalance balComp = new CashBalance(ticketDAO);
		
		if (bulkLoad.isSendHome()) {
			log.info("sending only home ");
			List<PosCashBalance> balances = cashBalanceDAO.fetchAllBetween(bulkLoad.getFrom().toString("yyyyMMdd"), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1).toString("yyyyMMdd"));
			log.info("found " + balances.size() + " existing Balances");
			balances.forEach(b -> balComp.amendTickets(b)); // wir wollen alle Tickets mitgeben
			PosAdapterApplication.homingQueue.addAll(balances); // sync them back home
		} else {
			log.info("importing from pos ");
			log.info("deleting balances in timespan");
			cashBalanceDAO.deleteBalancesBetween(bulkLoad.getFrom().toString("yyyyMMdd"), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1).toString("yyyyMMdd"));
			List<KassenAbschluss> belege = abschlussDao.fetchAllBetween(bulkLoad.getFrom().toDateTimeAtStartOfDay(), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1));
			log.info("found " + belege.size() + " KassenAbschlüsse");
			List<PosCashBalance> pcb = createNewBalances(belege);
			pcb.forEach(b -> balComp.amendTickets(b)); // wir wollen alle Tickets mitgeben
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
				pcb.add(createNewBalance(abschluss));
			}
		}
		cashBalanceDAO.insertAll(pcb.iterator());
		return pcb;
	}


	private PosCashBalance createNewBalance(KassenAbschluss abschluss) {
		CashBalance balComp = new CashBalance(ticketDAO);
		PosCashBalance bal = balComp.createBalance(abschluss.getVonDatum(), abschluss.getBisDatum());
		setBalanceValues(abschluss, bal);
		return bal;
	}

	/** 
	 * aktualisiert genau einen Kassenabschluss
	 * @param accBal
	 */
	@Override
	public void accept(PosCashBalance accBal) {
		KassenAbschluss abschluss = abschlussDao.fetchForDate(accBal.getAbschlussId());
		setBalanceValues(abschluss, accBal);
		resyncBalanceDetails(abschluss, accBal);
		cashBalanceDAO.update(accBal); // save to database
		PosAdapterApplication.homingQueue.offer(accBal); // sync back home
	}


	private void setBalanceValues(KassenAbschluss abschluss, PosCashBalance bal) {
		bal.setId(abschluss.getId());
		bal.setAbschlussId(abschluss.getAbschlussid());
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
	}


	private void resyncBalanceDetails(KassenAbschluss abschluss, PosCashBalance bal) {
		// jetzt die Tx neu Synchronisieren
		txDAO.deleteTxBetween(abschluss.getVonDatum(), abschluss.getBisDatum());
		SynchronizePosTx syncTx = new SynchronizePosTx(txDAO, vorgangDao);
		List<KassenVorgang> vorgs = vorgangDao.fetchAllBetween(bal.getFirstCovered(), bal.getLastCovered());
		List<PosTx> posTxs = syncTx.createNewTx(vorgs);
		
		// jetzt die Tickets neu synchronisieren
		SynchronizePosTicket syncTicket = new SynchronizePosTicket(ticketDAO, belegDao);
		ticketDAO.deleteTicketsBetween(abschluss.getVonDatum(), abschluss.getBisDatum());
		List<KassenBeleg> belgs = belegDao.fetchAllBetween(bal.getFirstCovered(), bal.getLastCovered());
		List<PosTicket> posTickets= syncTicket.createTickets(belgs);
		
		CashBalance balComp = new CashBalance(ticketDAO);
		balComp.updateBalance(bal,posTxs, posTickets);
		
	}

	
	
}
