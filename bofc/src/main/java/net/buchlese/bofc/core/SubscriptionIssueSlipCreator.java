package net.buchlese.bofc.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class SubscriptionIssueSlipCreator {

	/**
	 * lieferschein für einen Tag und einen Abonnenten 
	 * @param dao
	 * @param sub
	 * @return
	 */
	public static PosIssueSlip createSubscription(SubscrDAO dao, PosInvoiceDAO invDao, Subscriber subscriber, NumberGenerator numgen) {
//		final PosIssueSlip inv = createSlipSkeleton(subscriber);
//		// details
//		List<SubscrDelivery> deliveries = dao.getDeliveriesForSubscriberSlipflag(subscriber.getId(), false);
//		for(SubscrDelivery sub : deliveries) {
//			addDeliveriesToSlip(dao, sub, inv, dao.getDeliveriesForSubscriptionPayflag(sub.getId(), false));
//		}
//		
//		inv.setNumber(String.valueOf(numgen.getNextNumber()));
//		invDao.insertIssueSlip(inv);
//		
//		dao.recordDetailsOnInvoice(deliveries.stream().mapToLong(SubscrDelivery::getId).boxed().collect(Collectors.toList()), inv.getNumber());
//		return inv;
		return null;
	}


	/**
	 * festschreiben einer Rechnung wieder rückgängig machen (bei den Abos die von der Rechnung betroffen sind.
	 * Die Rechnung selbst wird nicht angefasst
	 * @param dao
	 * @param inv
	 */
	public static void cancelInvoice(SubscrDAO dao, PosInvoice inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = dao.getSubscription(iad.getAgreementId());
				sub.setPayedUntil(iad.getDeliveryFrom().minusDays(1));
				dao.resetDetailsOfInvoice(iad.getDeliveryIds());
				dao.updateSubscription(sub);
			} else {
				PosIssueSlip slip = dao.getIssueSlip(iad.getAgreementId());
				slip.setPayed(Boolean.FALSE);
				dao.updateIssueSlip(slip); // TODO wir sollten den auch im Libras als unbezahlt markieren
			}
		}
	}
	


	
	/**
	 * fügt die Lieferung eines Artikel zur Rechnung hinzu
	 * @param dao
	 * @param sub
	 * @param inv
	 * @param deliveries
	 * @return
	 */
	private static InvoiceAgrDetail addDeliveriesToInvoice(SubscrDAO dao, Subscription sub, PosInvoice inv, List<SubscrDelivery> deliveries) {
		if (deliveries.isEmpty()) {
			// es gibt nix zu adden - back
			return null;
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		addTextDetail(inv, sub.getDeliveryInfo1());
		addTextDetail(inv, sub.getDeliveryInfo2());
		LocalDate from = null;
		LocalDate till = null;
		// details per Delivery;
		for (SubscrDelivery deliv : deliveries) {
			inv.addInvoiceDetail(createInvoiceDetailForDelivery(deliv,dao.getSubscrArticle(deliv.getArticleId())));
			// Versandkosten
			if (deliv.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, deliv.getShipmentCost());
			}
			if (from == null || deliv.getDeliveryDate().isBefore(from)) {
				from = deliv.getDeliveryDate();
			}
			if (till == null || deliv.getDeliveryDate().isAfter(till)) {
				till = deliv.getDeliveryDate();
			}
		}
		iad.setAgreementId(sub.getId());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.dayOfMonth().withMaximumValue());  // immer der letzte des Monats
		iad.setDeliveryIds(deliveries.stream().mapToLong(SubscrDelivery::getId).boxed().collect(Collectors.toList()));
		inv.addAgreementDetail(iad);
		return iad;
	}

	/**
	 * fügt die Lieferung eines Artikel zur Rechnung hinzu
	 * @param dao
	 * @param sub
	 * @param inv
	 * @param deliveries
	 * @return
	 */
	private static InvoiceAgrDetail addIntervalDeliveriesToInvoice(SubscrDAO dao, Subscription sub, PosInvoice inv, List<SubscrIntervalDelivery> deliveries) {
		if (deliveries.isEmpty()) {
			// es gibt nix zu adden - back
			return null;
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		addTextDetail(inv, sub.getDeliveryInfo1());
		addTextDetail(inv, sub.getDeliveryInfo2());
		LocalDate from = null;
		LocalDate till = null;
		// details per Delivery;
		for (SubscrIntervalDelivery deliv : deliveries) {
			SubscrInterval interval = dao.getSubscrInterval(deliv.getIntervalId());
			inv.addInvoiceDetail(createInvoiceDetailForInterval(deliv, interval));
			// Versandkosten
			if (deliv.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, deliv.getShipmentCost());
			}
			if (from == null || interval.getStartDate().isBefore(from)) {
				from = interval.getStartDate();
			}
			if (till == null || interval.getEndDate().isAfter(till)) {
				till = interval.getEndDate();
			}
		}
		sub.setPayedUntil(till);
		iad.setAgreementId(sub.getId());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.dayOfMonth().withMaximumValue());  // immer der letzte des Monats
		iad.setDeliveryIds(deliveries.stream().mapToLong(SubscrIntervalDelivery::getId).boxed().collect(Collectors.toList()));
		inv.addAgreementDetail(iad);
		return iad;
	}

	private static PosInvoice createInvoiceSkeleton(Subscriber subscri) {
		PosInvoice inv = new PosInvoice();
		
		// formalia
		inv.setDate(LocalDate.now());
		inv.setCreationTime(DateTime.now());
		inv.setCustomerId(subscri.getCustomerId());
		inv.setDebitorId(subscri.getDebitorId());
		inv.setCancelled(false);
		inv.setPayed(false);
		inv.setPointid(subscri.getPointid());
		inv.setAmount(0L);
		inv.setAmountFull(0L);
		inv.setAmountHalf(0L);
		inv.setAmountNone(0L);
		inv.setPrinted(false);
		inv.setCollective(false);
		inv.setType("subscr");
		inv.setTemporary(false);
		
		// Rechnungsadresse
		inv.setName1(subscri.getInvoiceAddress().getName1());
		inv.setName2(subscri.getInvoiceAddress().getName2());
		inv.setName3(subscri.getInvoiceAddress().getName3());
		inv.setStreet(subscri.getInvoiceAddress().getStreet());
		inv.setCity(subscri.getInvoiceAddress().getPostalcode() + " " + subscri.getInvoiceAddress().getCity());
		return inv;
	}
	
	private static PosInvoiceDetail createInvoiceDetailForInterval(SubscrIntervalDelivery deliv, SubscrInterval interval) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(deliv.getQuantity());
		detail.setText(interval.getName());
		detail.setSinglePrice(deliv.getTotal() / deliv.getQuantity());
		detail.setAmount(deliv.getTotal());
		detail.setAmountFull(deliv.getTotalFull());
		detail.setAmountHalf(deliv.getTotalHalf());
		detail.setAmountNone(0L); // Abos ohne MwSt können wir noch nicht.
		return detail;
	}

	private static PosInvoiceDetail addShipmentCostDetail(PosInvoice inv, long shipmentCost) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(1);
		detail.setText("Versandkosten");
		detail.setSinglePrice(shipmentCost);
		detail.setAmount(shipmentCost);
		// die Nebenleistung ereilt das Schicksal der Hauptleistung
		if (inv.getAmountFull() > inv.getAmountHalf()) {
			detail.setAmountFull(shipmentCost);
		} else {
			detail.setAmountHalf(shipmentCost);
		}
		inv.addInvoiceDetail(detail);
		return detail;
	}

	private static PosInvoiceDetail createInvoiceDetailForDelivery(SubscrDelivery deliv, SubscrArticle art) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(deliv.getQuantity());
		detail.setText(art.getName());
		detail.setSinglePrice(deliv.getTotal() / deliv.getQuantity());
		detail.setAmount(deliv.getTotal());
		detail.setAmountFull(deliv.getTotalFull());
		detail.setAmountHalf(deliv.getTotalHalf());
		detail.setAmountNone(0L); // Abos ohne MwSt können wir noch nicht.
		return detail;
	}

	private static void addTextDetail(PosInvoice inv, String deliveryInfo1) {
		if (deliveryInfo1 == null || deliveryInfo1.isEmpty()) {
			return;
		}
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setTextonly(true);
		detail.setText(deliveryInfo1);
		inv.addInvoiceDetail(detail);
	}


}