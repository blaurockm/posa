package net.buchlese.posa.core;

import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.api.bofc.PaymentMethod;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.core.SyncTimer.BulkLoadDetails;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class SynchronizePosTicket extends AbstractSynchronizer {

	private final PosTicketDAO ticketDAO;
	private final KassenBelegDAO belegDao;


	public SynchronizePosTicket(PosTicketDAO txDAO, KassenBelegDAO vorgangsDao) {
		this.ticketDAO = txDAO;
		this.belegDao = vorgangsDao;
	}

	/**
	 * erzeuge Tickets für neu angelegte Belege
	 * @param syncStart
	 * @return
	 */
	public List<PosTicket> fetchNewTickets(LocalDate syncStart) {
		Optional<DateTime> maxDatum = Optional.fromNullable(ticketDAO.getMaxTimestamp());

		List<KassenBeleg> belege = belegDao.fetchAllAfter(maxDatum.or(syncStart.toDateTimeAtStartOfDay()));

		// convert KassenVorgang to posTx
		return createTickets(belege);
	}
	
	/**
	 * erzeuge Tickets für die Belege in dem Zeitraum
	 * @param bulkLoad
	 */
	public void doBulkLoad(BulkLoadDetails bulkLoad) {
		Logger log = LoggerFactory.getLogger("TicketBulkLoad");
		log.info("doing " + bulkLoad);
		if (bulkLoad.isSendHome()) {
			log.info("only sending home, nothing to do for tickets");
			return;
		}
		log.info("deleting tickets in this timespan ");
		ticketDAO.deleteTicketsBetween(bulkLoad.getFrom().toDateTimeAtStartOfDay(), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1));
		List<KassenBeleg> belege = belegDao.fetchAllBetween(bulkLoad.getFrom().toDateTimeAtStartOfDay(), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1));
		log.info("found " + belege.size() + " Belege");
		createTickets(belege);
		log.info("done with " + bulkLoad);
	}

	/**
	 * erzeuge Tickets für die geg. Belege
	 * @param belege
	 * @return
	 */
	public List<PosTicket> createTickets(List<KassenBeleg> belege) {
		// convert KassenVorgang to posTx
		List<PosTicket> res = new ArrayList<>();
		for (KassenBeleg beleg : belege) {
			// wir legen den Beleg auf jeden Fall an,
			// geparkte Belege bekommen das Kennzeichen "toBeCheckedAgain"
			PosTicket tx = createNewTicket(beleg);
			res.add(tx);
		}

		ticketDAO.insertAll(res.iterator());
		
		return res;
	}
	
	
	public void updateLast10Tickets() throws Exception {
		List<KassenBeleg> lastBelege = belegDao.fetchLast();
		for (KassenBeleg orig : lastBelege) {
			PosTicket checker = ticketDAO.fetch(orig.getBelegnr());
			if (checker != null && updateTicket(orig, checker)) {
				// es hat sich was geändert;
				ticketDAO.update(checker);
			}
		}
	}

	private PosTicket createNewTicket(KassenBeleg beleg) {
		PosTicket tx = new PosTicket();
		tx.setBelegNr(beleg.getBelegnr());
		updateTicket(beleg, tx);
		tx.setTimestamp(beleg.getZahlungszeit());
		return tx;
	}


	private boolean updateTicket(KassenBeleg beleg, PosTicket tx) {
		boolean changed = false;
		changed |= updMoney(tx::setTotal, tx.getTotal(), beleg.getZahlungsBetrag());
		changed |= updBool(tx::setCancelled, tx.isCancelled(), beleg.isStorniert());
		changed |= updBool(tx::setCancel, tx.isCancel(), (beleg.getBemerkung() != null && beleg.getBemerkung().startsWith("STORNO")));
		changed |= updEnum(tx::setPaymentMethod, tx.getPaymentMethod(), PaymentMethod.mappingFrom(beleg.getZahlungsart()));
		changed |= updBool(tx::setToBeCheckedAgain, tx.isToBeCheckedAgain(), beleg.isGeparkt());
		return changed;
	}


}
