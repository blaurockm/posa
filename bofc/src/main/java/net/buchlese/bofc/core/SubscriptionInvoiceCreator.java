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

public class SubscriptionInvoiceCreator {

	/**
	 * Sammelrechnung für einen Abonnenten
	 * @param dao
	 * @param subscriber
	 * @return
	 */
	public static PosInvoice createCollectiveSubscription(SubscrDAO dao, Subscriber subscriber, NumberGenerator numgen) {
		List<PosIssueSlip> issueSlips = dao.findIssueSlipsToAdd(subscriber.getDebitorId());
		PosInvoice inv = createTemporaryInvoice(dao, subscriber, dao.getSubscriptionsForSubscriber(subscriber.getId()), issueSlips, numgen);
		inv.setCollective(true);
		return inv;
	}

	/**
	 * Einzelrechnung für ein Abo 
	 * @param dao
	 * @param sub
	 * @return
	 */
	public static PosInvoice createSubscription(SubscrDAO dao, Subscription sub, NumberGenerator numgen) {
		return createTemporaryInvoice(dao, dao.getSubscriber(sub.getSubscriberId()), Arrays.asList(sub), null, numgen);
	}

	/**
	 * festschreiben einer Rechnung bei den Abos die dvon der Rechnung betroffen sind.
	 * Die Rechnung selbst wird nicht angefasst
	 * @param dao
	 * @param inv
	 */
	private static void recordInvoiceOnAgreements(SubscrDAO dao, PosInvoice inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = dao.getSubscription(iad.getAgreementId());
				sub.setPayedUntil(iad.getDeliveryTill());
				dao.recordDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
				dao.updateSubscription(sub);
			} else {
				PosIssueSlip slip = dao.getIssueSlip(iad.getAgreementId());
				slip.setPayed(Boolean.TRUE);
				dao.updateIssueSlip(slip);  // TODO wir sollten den auch im Libras als bezahlt markieren
			}
		}
	}

	/**
	 * festschreiben einer Rechnung
	 * @param dao
	 * @param invDao
	 * @param inv
	 */
	public static void fakturiereInvoice(SubscrDAO dao, PosInvoiceDAO invDao, PosInvoice inv) {
		inv.setTemporary(false);
		invDao.insert(inv);
		dao.deleteTempInvoice(inv.getNumber());
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
	 * erzeugt eine komplette Rechnung und legt sie als temporär ab.
	 * 
	 * @param dao
	 * @param subscriber
	 * @param subs
	 * @param issueSlips
	 * @param numgen
	 * @return
	 */
	private static PosInvoice createTemporaryInvoice(final SubscrDAO dao, Subscriber subscriber, List<Subscription> subs, List<PosIssueSlip> issueSlips, NumberGenerator numgen) {
		final PosInvoice inv = createInvoiceSkeleton(subscriber);
		// details
		for(Subscription sub : subs) {
			if (PayIntervalType.EACHDELIVERY.equals(sub.getPaymentType())) {
				addDeliveriesToInvoice(dao, sub, inv, dao.getDeliveriesForSubscriptionUnrecorded(sub.getId()));
			} else {
				addIntervalDeliveriesToInvoice(dao, sub, inv, dao.getIntervalDeliveriesForSubscriptionUnrecorded(sub.getId()));
			}
		}
		if (issueSlips != null) {
			issueSlips.forEach(s -> addIssueSlipToInvoice(dao, s, inv));
		}
		
		updateTaxValues(inv);
		
		inv.setTemporary(true);
		inv.setNumber(numgen.getNextInvoiceNumber(inv.getPointid()));
		dao.insertTempInvoice(inv);
		
		recordInvoiceOnAgreements(dao, inv);
		return inv;
	}

	/**
	 * errechnet die Steuerbeträge
	 * @param inv
	 */
	private static void updateTaxValues(PosInvoice inv) {
		inv.setNettoHalf((long) (inv.getAmountHalf() / 1.07));
		inv.setTaxHalf(inv.getAmountHalf() - inv.getNettoHalf());
		inv.setTax(inv.getTaxHalf());
		if (inv.getAmountFull() != null) {
			inv.setNettoFull((long) (inv.getAmountFull() / 1.19));
			inv.setTaxFull(inv.getAmountFull() - inv.getNettoFull());
			inv.setTax(inv.getTax() + inv.getTaxFull());
		}
	}
	
	/**
	 * fügt einen Lieferschein aus Libras der Rechnung hinzu
	 * @param dao
	 * @param slip
	 * @param inv
	 * @return
	 */
	private static InvoiceAgrDetail addIssueSlipToInvoice(SubscrDAO dao, PosIssueSlip slip, PosInvoice inv) {
		if (slip.isIncludeOnInvoice() == false) {
			return null;
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		iad.setAgreementId(slip.getId());
		iad.setType(InvoiceAgrDetail.TYPE.ISSUESLIP);
		addTextDetail(inv, "Artikel des Lieferscheins " + slip.getNumber() + " vom " + slip.getDate().toString("dd.MM.yyyy"));
		for (PosInvoiceDetail detail : slip.getDetails()) {
			inv.addInvoiceDetail(detail);
		}
		iad.setDeliveryFrom(slip.getDate());
		iad.setDeliveryTill(slip.getDate());
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
		detail.setSinglePrice(interval.getBrutto());
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
		detail.setSinglePrice(art.getBrutto());
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