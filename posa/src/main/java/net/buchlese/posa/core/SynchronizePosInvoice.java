package net.buchlese.posa.core;

import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.pos.KleinteilKopf;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.jdbi.pos.KleinteilDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

public class SynchronizePosInvoice extends AbstractSynchronizer {

	private final PosInvoiceDAO invDAO;

	private final KleinteilDAO rechnungsDAO;

	public SynchronizePosInvoice(PosInvoiceDAO cashBalanceDAO, KleinteilDAO ticketDAO) {
		this.invDAO = cashBalanceDAO;
		this.rechnungsDAO = ticketDAO;
	}

	/**
	 * erzeuge Invoices für die neu angelegten Rechnungen
	 * 
	 * Wird vom Sync-Timer aufgerufen.
	 * 
	 * @param syncStart
	 */
	public void fetchNewInvoices(DateTime syncStart) {
		Optional<DateTime> maxId = Optional.fromNullable(invDAO.getLastErfasst());

		List<KleinteilKopf> rechnungen = rechnungsDAO.fetchAllAfter(maxId.or(syncStart));

		// für jeden nicht vorhandenen neuen Abschluss eine CashBalance anlegen.
		List<PosInvoice> pcb = createNewInvoices(rechnungen);
		invDAO.insertAll(pcb.iterator());
		PosAdapterApplication.homingQueue.addAll(pcb); // sync the new ones back home
	}

	
	
	public List<PosInvoice> createNewInvoices(List<KleinteilKopf> rechnungen) {
		List<PosInvoice> invs = new ArrayList<>();
		for (KleinteilKopf rechn : rechnungen) {
			if (rechn.getRechnungsNummer() != null && rechn.getRechnungsDatum() != null &&  rechn.getBrutto() != null) {
				invs.add(createInvoice(rechn));
			}
		}
		return invs;
	}

	
	public PosInvoice createInvoice(KleinteilKopf rech) {
		PosInvoice inv = new PosInvoice();
		inv.setNumber(rech.getRechnungsNummer());
		inv.setDate(rech.getRechnungsDatum().toLocalDate());
		updDate(inv::setCreationTime, inv.getCreationTime(), rech.getErfassungsDatum());
		updDate(inv::setPrintTime, inv.getPrintTime(), rech.getDruckDatum());
		updMoney(inv::setAmount, inv.getAmount(), rech.getBrutto());
		updMoney(inv::setAmountHalf, inv.getAmountHalf(), rech.getBrutto7());
		updMoney(inv::setAmountFull, inv.getAmountFull(), rech.getBrutto19());
		updMoney(inv::setAmountNone, inv.getAmountNone(), rech.getBrutto0());

		updStr(inv::setName1, inv.getName1(), rech.getName1());
		updStr(inv::setName2, inv.getName2(), rech.getName2());
		updStr(inv::setName3, inv.getName3(), rech.getName3());
		updStr(inv::setStreet, inv.getStreet(), rech.getStrasse());
		updStr(inv::setCity, inv.getCity(), rech.getOrt());
		return inv;
	}
	
}
