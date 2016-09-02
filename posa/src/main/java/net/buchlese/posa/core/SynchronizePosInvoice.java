package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.bofc.PosInvoiceDetail;
import net.buchlese.posa.api.bofc.PosIssueSlip;
import net.buchlese.posa.api.pos.KleinteilElement;
import net.buchlese.posa.api.pos.KleinteilKopf;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.jdbi.pos.KleinteilDAO;

import com.google.common.base.Optional;

public class SynchronizePosInvoice extends AbstractSynchronizer {

	private final PosInvoiceDAO invDAO;

	private final KleinteilDAO rechnungsDAO;
	private final Integer limit;

	public SynchronizePosInvoice(PosInvoiceDAO cashBalanceDAO, KleinteilDAO ticketDAO, Integer limit) {
		this.invDAO = cashBalanceDAO;
		this.rechnungsDAO = ticketDAO;
		this.limit = limit;
	}
	
	/**
	 * erzeuge Invoices für die neu angelegten Rechnungen
	 * 
	 * Wird vom Sync-Timer aufgerufen.
	 * 
	 * @param syncStart
	 */
	public BigDecimal fetchNewAndChangedInvoices(BigDecimal rowver) {
		BigDecimal res = rowver;
		Optional<Integer> maxId = Optional.fromNullable(invDAO.getLastErfasst());

		List<KleinteilKopf> rechs = rechnungsDAO.fetchAllRechnungenAfter(maxId.or(1160000), limit);
		createNewInvoices(rechs);
		if (rechs.isEmpty() == false) {
			res = rechs.get(rechs.size()-1).getZeitmarke();
			if (rowver == null) {
				rowver = res;
			}
		}
		
		List<KleinteilKopf> rechsChg = rechnungsDAO.fetchAllChangedRechnungenAfter(rowver);
		if (rechsChg.isEmpty() == false) {
			BigDecimal newRes = rechsChg.get(rechsChg.size()-1).getZeitmarke();
			if (newRes.compareTo(res) > 0) {
				res = newRes;
			}
		}
		updateInvoices(rechsChg);
		return res;
	}

	/**
	 * erzeuge Invoices für die neu angelegten Rechnungen
	 * 
	 * Wird vom Sync-Timer aufgerufen.
	 * 
	 * @param syncStart
	 */
	public BigDecimal fetchNewAndChangedIssueSlips(BigDecimal rowver) {
		BigDecimal res = rowver;
		Optional<Integer> maxId = Optional.fromNullable(invDAO.getLastErfasstLieferschein());

		List<KleinteilKopf> isss = rechnungsDAO.fetchAllLieferscheinAfter(maxId.or(1160000), limit);
		createNewIssueSlips(isss);
		if (isss.isEmpty() == false) {
			res = isss.get(isss.size()-1).getZeitmarke();
			if (rowver == null) {
				rowver = res;
			}
		}
		
		List<KleinteilKopf> isssChg = rechnungsDAO.fetchAllChangedLieferscheineAfter(rowver);
		if (isssChg.isEmpty() == false) {
			BigDecimal newRes = isssChg.get(isssChg.size()-1).getZeitmarke();
			if (newRes.compareTo(res) > 0) {
				res = newRes;
			}
		}
		updateIssueSlips(isssChg);
		
		return res;
	}

	public List<PosIssueSlip> createNewIssueSlips(List<KleinteilKopf> rechnungen) {
		List<PosIssueSlip> slips = new ArrayList<>();
		for (KleinteilKopf rechn : rechnungen) {
			if (rechn.getRechnungsNummer() != null && rechn.getRechnungsDatum() != null &&  rechn.getBrutto() != null) {
				slips.add(createIssueSlip(rechn));
			}
		}
		invDAO.insertAllIssueSlip(slips.iterator());
		PosAdapterApplication.homingQueue.addAll(slips); // sync the new ones back home
		return slips;
	}

	public List<PosIssueSlip> updateIssueSlips(List<KleinteilKopf> rechnungen) {
		List<PosIssueSlip> slips = new ArrayList<>();
		for (KleinteilKopf rechn : rechnungen) {
			if (rechn.getRechnungsNummer() != null && rechn.getRechnungsDatum() != null &&  rechn.getBrutto() != null) {
				List<PosIssueSlip> slip = invDAO.fetchIssueSlip(rechn.getRechnungsNummer());
				if (slip.isEmpty() == false) {
					PosIssueSlip i = updateIssueSlip(slip.get(0), rechn);
					slips.add(i);
					invDAO.updateIssueSlip(i);
				}
			}
		}
		PosAdapterApplication.homingQueue.addAll(slips); // sync the new ones back home
		return slips;
	}

	public List<PosInvoice> createNewInvoices(List<KleinteilKopf> rechnungen) {
		List<PosInvoice> invs = new ArrayList<>();
		for (KleinteilKopf rechn : rechnungen) {
			if (rechn.getRechnungsNummer() != null && rechn.getRechnungsDatum() != null &&  rechn.getBrutto() != null) {
				invs.add(createInvoice(rechn));
			}
		}
		invDAO.insertAll(invs.iterator());
		PosAdapterApplication.homingQueue.addAll(invs); // sync the new ones back home
		return invs;
	}

	public List<PosInvoice> updateInvoices(List<KleinteilKopf> rechnungen) {
		List<PosInvoice> invs = new ArrayList<>();
		for (KleinteilKopf rechn : rechnungen) {
			if (rechn.getRechnungsNummer() != null && rechn.getRechnungsDatum() != null &&  rechn.getBrutto() != null) {
				List<PosInvoice> inv = invDAO.fetchInvoice(rechn.getRechnungsNummer());
				if (inv.isEmpty() == false) {
					PosInvoice i = updateInvoice(inv.get(0), rechn);
					invs.add(i);
					invDAO.updateInvoice(i);
				}
			}
		}
		PosAdapterApplication.homingQueue.addAll(invs); // sync the new ones back home
		return invs;
	}
	
	
	
	public PosInvoice createInvoice(KleinteilKopf rech) {
		PosInvoice inv = new PosInvoice();
		inv.setActionum(rech.getId());
		inv.setNumber(rech.getRechnungsNummer());
		updDate(inv::setCreationTime, inv.getCreationTime(), rech.getErfassungsDatum());
		return updateInvoice(inv, rech);
	}
	
	
	public PosInvoice updateInvoice(PosInvoice inv, KleinteilKopf rech) {
		inv.setPayed(rech.getBezahlt());
		inv.setDate(rech.getRechnungsDatum().toLocalDate());
		inv.setCustomerId(rech.getKundenNummer());
		updDate(inv::setPrintTime, inv.getPrintTime(), rech.getDruckDatum());

		updStr(inv::setName1, inv.getName1(), rech.getName1());
		updStr(inv::setName2, inv.getName2(), rech.getName2());
		updStr(inv::setName3, inv.getName3(), rech.getName3());
		updStr(inv::setStreet, inv.getStreet(), rech.getStrasse());
		updStr(inv::setCity, inv.getCity(), rech.getOrt());
		
		List<KleinteilElement> elems = rechnungsDAO.fetchElemente(rech.getId());
		List<PosInvoiceDetail> details = inv.getDetails() != null ? inv.getDetails() : Collections.emptyList();
		for (KleinteilElement e : elems) {
			java.util.Optional<PosInvoiceDetail> match = details.stream().filter(d -> d != null && d.getLfdNr() != null && d.getLfdNr().intValue() == e.getLaufendeNummer()).findAny();
			if (match.isPresent()) {
				updateInvoiceDetail(match.get(), e);
			} else {
				inv.addDetail(createInvoiceDetail(e));
			}
		}
		// 1 = rechnung, 2 = lieferschein, 8= gutschrift, 10 = storno-rech, 12 = remission
		updBool(inv::setCancelled, inv.getCancelled(), rech.getArt() != 1);
		updMoney(inv::setAmount, inv.getAmount(), rech.getBrutto());
		updMoney(inv::setAmountHalf, inv.getAmountHalf(), rech.getBrutto7());
		updMoney(inv::setAmountFull, inv.getAmountFull(), rech.getBrutto19());
		updMoney(inv::setAmountNone, inv.getAmountNone(), rech.getBrutto0());
		return inv;
	}

	public PosIssueSlip createIssueSlip(KleinteilKopf rech) {
		PosIssueSlip inv = new PosIssueSlip();
		inv.setActionum(rech.getId());
		inv.setNumber(rech.getRechnungsNummer());
		updDate(inv::setCreationTime, inv.getCreationTime(), rech.getErfassungsDatum());
		return updateIssueSlip(inv, rech);
	}
	
	
	public PosIssueSlip updateIssueSlip(PosIssueSlip inv, KleinteilKopf rech) {
		inv.setPayed(rech.getBezahlt());
		inv.setDate(rech.getRechnungsDatum().toLocalDate());
		inv.setCustomerId(rech.getKundenNummer());
		updDate(inv::setPrintTime, inv.getPrintTime(), rech.getDruckDatum());

		updStr(inv::setName1, inv.getName1(), rech.getName1());
		updStr(inv::setName2, inv.getName2(), rech.getName2());
		updStr(inv::setName3, inv.getName3(), rech.getName3());
		updStr(inv::setStreet, inv.getStreet(), rech.getStrasse());
		updStr(inv::setCity, inv.getCity(), rech.getOrt());

		List<KleinteilElement> elems = rechnungsDAO.fetchElemente(rech.getId());
		List<PosInvoiceDetail> details = inv.getDetails() != null ? inv.getDetails() : Collections.emptyList();
		for (KleinteilElement e : elems) {
			java.util.Optional<PosInvoiceDetail> match = details.stream().filter(d -> d != null && d.getLfdNr() != null && d.getLfdNr().intValue() == e.getLaufendeNummer()).findAny();
			if (match.isPresent()) {
				updateInvoiceDetail(match.get(), e);
			} else {
				inv.addDetail(createInvoiceDetail(e));
			}
		}

		updMoney(inv::setAmount, inv.getAmount(), rech.getBrutto());
		updMoney(inv::setAmountHalf, inv.getAmountHalf(), rech.getBrutto7());
		updMoney(inv::setAmountFull, inv.getAmountFull(), rech.getBrutto19());
		updMoney(inv::setAmountNone, inv.getAmountNone(), rech.getBrutto0());
		
		return inv;
	}

	private PosInvoiceDetail createInvoiceDetail(KleinteilElement e) {
		PosInvoiceDetail pd = new PosInvoiceDetail();
		pd.setLfdNr(Long.valueOf(e.getLaufendeNummer()));
		return updateInvoiceDetail(pd, e);
	}
	
	private PosInvoiceDetail updateInvoiceDetail(PosInvoiceDetail pd, KleinteilElement e) {
		if (e.getMenge() == null) {
			pd.setText(e.getBezeichnung());
			pd.setTextonly(true);
			return pd;
		}
		switch (e.getKennziffer()) {
		case "AP" : if (e.getBezeichnung() != null) { 
			updStr(pd::setText, pd.getText(), e.getBezeichnung());   
		} else {
			updStr(pd::setText, pd.getText(), e.getMatchCode());
		} 
		break;
		case "TB" : pd.setText(rechnungsDAO.getTextbaustein(e.getTextbaustein())); pd.setTextonly(true); return pd; 
		case "LZ" : pd.setText("    "); pd.setTextonly(true); return pd;
		default: pd.setText("unbekannte Kennziffer " + e.getKennziffer());  pd.setTextonly(true); return pd;
		}
		updInt(pd::setQuantity, pd.getQuantity(), e.getMenge());
		BigDecimal betrag = e.getBruttoEinzel();
		if (betrag == null || betrag.intValue() == 0) {
			betrag = e.getMenge(); // es handelt sich hierbei um eine freie preiseingabe
		}
		updMoney(pd::setSinglePrice, pd.getSinglePrice(), betrag);
		if (pd.getQuantity() > 0 && e.getBruttoEinzel() != null && e.getBruttoEinzel().intValue() != 0) { // sonst wird menge mit menge multipliziert
			betrag = betrag.multiply(new BigDecimal(pd.getQuantity()));
		}
		updMoney(pd::setAmount, pd.getAmount(), betrag);
		switch (e.getMwstkz()) {
		case '2' : updMoney(pd::setAmountHalf, pd.getAmountHalf(), betrag); break;
		case '1' : updMoney(pd::setAmountFull, pd.getAmountFull(), betrag); break;
		case '3' : updMoney(pd::setAmountNone, pd.getAmountNone(), betrag); break; 
		default : betrag = new BigDecimal(0); pd.setAmountNone(0L);
		}
		if (e.getRabattSatz() != null) {
			updMoney(pd::setRebate, pd.getRebate(), e.getRabattSatz());
		}
		updMoney(pd::setRebatePrice, pd.getRebatePrice(), e.getRabattEinzel());
		return pd;
	}
}
