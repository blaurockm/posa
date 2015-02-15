package net.buchlese.posa.core;

import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.api.bofc.PaymentMethod;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Optional;

public class SynchronizePosTicket extends AbstractSynchronizer {

	private final PosTicketDAO ticketDAO;
	private final KassenBelegDAO belegDao;
	private LocalDate syncStart;


	public SynchronizePosTicket(PosTicketDAO txDAO, KassenBelegDAO vorgangsDao, LocalDate syncStart) {
		this.ticketDAO = txDAO;
		this.belegDao = vorgangsDao;
		this.syncStart = syncStart;
	}

	public List<PosTicket> fetchNewTickets() throws Exception {
		Optional<DateTime> maxDatum = Optional.fromNullable(ticketDAO.getMaxTimestamp());

		List<KassenBeleg> belege = belegDao.fetchAllAfter(maxDatum.or(syncStart.toDateTimeAtStartOfDay()));

		// convert KassenVorgang to posTx
		return createTickets(belege);
	}

	public List<PosTicket> createTickets(List<KassenBeleg> belege) {
		Optional<Integer> maxId = Optional.fromNullable(ticketDAO.getMaxId());

		long id = maxId.or(0);
		
		// convert KassenVorgang to posTx
		List<PosTicket> res = new ArrayList<>();
		for (KassenBeleg beleg : belege) {
			// wir legen den Beleg auf jeden Fall an,
			// geparkte Belege bekommen das Kennzeichen "toBeCheckedAgain"
			PosTicket tx = createNewTicket(beleg, ++id);
			res.add(tx);
		}

		ticketDAO.insertAll(res.iterator());
		
		return res;
	}
	
	
	public void updateExistingTickets() throws Exception {
		List<KassenBeleg> lastBelege = belegDao.fetchLast();
		for (KassenBeleg orig : lastBelege) {
			PosTicket checker = ticketDAO.fetch(orig.getBelegnr());
			if (checker != null && updateTicket(orig, checker)) {
				// es hat sich was geändert;
				ticketDAO.update(checker);
			}
		}
	}

	public void updateTickets(List<PosTicket> toBeCheckedAgain) {
		for (PosTicket checker : toBeCheckedAgain) {
			KassenBeleg orig = belegDao.fetch(checker.getBelegNr());
			if (updateTicket(orig, checker)) {
				// es hat sich was geändert;
				ticketDAO.update(checker);
			}
		}
	}
	

	private PosTicket createNewTicket(KassenBeleg beleg, long nextId) {
		PosTicket tx = new PosTicket();
		tx.setId(nextId);
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
