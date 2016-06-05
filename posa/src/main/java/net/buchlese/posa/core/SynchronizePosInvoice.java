package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.bofc.PosInvoiceDetail;
import net.buchlese.posa.api.pos.KleinteilElement;
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
		Optional<Integer> maxId = Optional.fromNullable(invDAO.getLastErfasst());

		List<KleinteilKopf> rechnungen = rechnungsDAO.fetchAllAfter(maxId.or(1160000));

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
		inv.setActionum(rech.getId());
		inv.setPayed(rech.getBezahlt());
		inv.setNumber(rech.getRechnungsNummer());
		inv.setDate(rech.getRechnungsDatum().toLocalDate());
		inv.setCustomerId(rech.getKundenNummer());
		updDate(inv::setCreationTime, inv.getCreationTime(), rech.getErfassungsDatum());
		updDate(inv::setPrintTime, inv.getPrintTime(), rech.getDruckDatum());

		updStr(inv::setName1, inv.getName1(), rech.getName1());
		updStr(inv::setName2, inv.getName2(), rech.getName2());
		updStr(inv::setName3, inv.getName3(), rech.getName3());
		updStr(inv::setStreet, inv.getStreet(), rech.getStrasse());
		updStr(inv::setCity, inv.getCity(), rech.getOrt());
		
		List<KleinteilElement> elems = rechnungsDAO.fetchElemente(rech.getId());
		for (KleinteilElement e : elems) {
			inv.addDetail(createInvoiceDetail(e));
		}
		updMoney(inv::setAmount, inv.getAmount(), rech.getBrutto());
		updMoney(inv::setAmountHalf, inv.getAmountHalf(), rech.getBrutto7());
		updMoney(inv::setAmountFull, inv.getAmountFull(), rech.getBrutto19());
		updMoney(inv::setAmountNone, inv.getAmountNone(), rech.getBrutto0());
		
		return inv;
	}
	
	private PosInvoiceDetail createInvoiceDetail(KleinteilElement e) {
		PosInvoiceDetail pd = new PosInvoiceDetail();
		if (e.getMenge() == null) {
			pd.setText(e.getBezeichnung());
			pd.setTextonly(true);
			return pd;
		}
		switch (e.getKennziffer()) {
		case "AP" : if (e.getBezeichnung() != null) { 
			pd.setText(e.getBezeichnung());   
		} else {
			pd.setText(e.getMatchCode());
		} 
		break;
		case "TB" : pd.setText(rechnungsDAO.getTextbaustein(e.getTextbaustein())); pd.setTextonly(true); return pd; 
		case "LZ" : pd.setText("    "); pd.setTextonly(true); return pd;
		default: pd.setText("unbekannte Kennziffer " + e.getKennziffer());  pd.setTextonly(true); return pd;
		}
		pd.setQuantity(e.getMenge().intValue());
		BigDecimal betrag = e.getBruttoEinzel();
		if (betrag.intValue() == 0) {
			betrag = e.getMenge(); // es handelt sich hierbei um eine freie preiseingabe
		}
		switch (e.getMwstkz()) {
		case '2' : updMoney(pd::setAmountHalf, pd.getAmountHalf(), betrag); break;
		case '1' : updMoney(pd::setAmountFull, pd.getAmountFull(), betrag); break;
		case '3' : updMoney(pd::setAmountNone, pd.getAmountNone(), betrag); break; 
		default : betrag = new BigDecimal(0); pd.setAmountNone(0L);
		}
		updMoney(pd::setSinglePrice, pd.getSinglePrice(), betrag);
		if (pd.getQuantity() > 0) {
			pd.setAmount(pd.getSinglePrice() * pd.getQuantity());
		} else {
			pd.setAmount(pd.getSinglePrice());
		}
		if (e.getRabattSatz() != null) {
			updMoney(pd::setRebate, pd.getRebate(), e.getRabattSatz());
		}
		updMoney(pd::setRebatePrice, pd.getRebatePrice(), e.getRabattEinzel());
		return pd;
	}
	
	
	
	
}
