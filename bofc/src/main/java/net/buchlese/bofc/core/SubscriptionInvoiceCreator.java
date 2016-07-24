package net.buchlese.bofc.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

	private  static class InvoiceSubComparator implements Comparator<Subscription> {
		@Override
		public int compare(Subscription o1, Subscription o2) {
			String di1sub1 = o1.getDeliveryInfo1();
			String di1sub2 = o2.getDeliveryInfo1();
			int idCompare = (int) (o1.getId() - o2.getId());
			if (di1sub1 == null) {
				return di1sub2 != null ? -1 : idCompare;
			} else if (di1sub1.equals(di1sub2)) {
				// compare second di
				String di2sub1 = o1.getDeliveryInfo2();
				String di2sub2 = o2.getDeliveryInfo2();
				if (di2sub1 == null) {
					return di2sub2 != null ? -1 : idCompare;
				} else if (di2sub1.equals(di2sub2)) {
					return idCompare;
				} 
				return di2sub1.compareTo(di2sub2);
			} 
			return di1sub1.compareTo(di1sub2);
		}

	}

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
				if (iad.getPayType() != null && iad.getPayType().equals(PayIntervalType.EACHDELIVERY)) {
					dao.recordDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
				} else {
					dao.recordIntervalDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
				}
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
				if (iad.getPayType() != null && iad.getPayType().equals(PayIntervalType.EACHDELIVERY)) {
					dao.resetDetailsOfInvoice(iad.getDeliveryIds());
				} else {
					dao.resetIntervalDetailsOfInvoice(iad.getDeliveryIds());
				}
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
		// die Abos nach den Lieferhinweisen sortieren
		Collections.sort(subs, new InvoiceSubComparator());
		// details
		String lastDetailInfo = null;
		for(Subscription sub : subs) {
			if (PayIntervalType.EACHDELIVERY.equals(sub.getPaymentType())) {
				lastDetailInfo = addDeliveriesToInvoice(dao, sub, inv, dao.getDeliveriesForSubscriptionPayflag(sub.getId(), false), lastDetailInfo);
			} else {
				lastDetailInfo = addIntervalDeliveriesToInvoice(dao, sub, inv, dao.getIntervalDeliveriesForSubscriptionPayflag(sub.getId(), false), lastDetailInfo);
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
	private static String addDeliveriesToInvoice(SubscrDAO dao, Subscription sub, PosInvoice inv, List<SubscrDelivery> deliveries, String lastDetailinfo) {
		if (deliveries.isEmpty()) {
			// es gibt nix zu adden - back
			return null;
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		String newDetail = null;
		if (sub.getDeliveryInfo1() != null) {
			newDetail = sub.getDeliveryInfo1() + sub.getDeliveryInfo2();
		}
		if ( lastDetailinfo == null || lastDetailinfo.equals(newDetail) == false) {
			addTextDetail(inv, sub.getDeliveryInfo1());
			addTextDetail(inv, sub.getDeliveryInfo2());
		}
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
		iad.setPayType(sub.getPaymentType());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.dayOfMonth().withMaximumValue());  // immer der letzte des Monats
		iad.setDeliveryIds(deliveries.stream().mapToLong(SubscrDelivery::getId).boxed().collect(Collectors.toList()));
		inv.addAgreementDetail(iad);
		return newDetail;
	}

	/**
	 * fügt die Lieferung eines Artikel zur Rechnung hinzu
	 * @param dao
	 * @param sub
	 * @param inv
	 * @param deliveries
	 * @return
	 */
	private static String addIntervalDeliveriesToInvoice(SubscrDAO dao, Subscription sub, PosInvoice inv, List<SubscrIntervalDelivery> deliveries, String lastDetailinfo) {
		if (deliveries.isEmpty()) {
			// es gibt nix zu adden - back
			return null;
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		String newDetail = null;
		if (sub.getDeliveryInfo1() != null) {
			newDetail = sub.getDeliveryInfo1() + sub.getDeliveryInfo2();
		}
		if ( lastDetailinfo == null || lastDetailinfo.equals(newDetail) == false) {
			addTextDetail(inv, sub.getDeliveryInfo1());
			addTextDetail(inv, sub.getDeliveryInfo2());
		}
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
		iad.setPayType(sub.getPaymentType());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.dayOfMonth().withMaximumValue());  // immer der letzte des Monats
		iad.setDeliveryIds(deliveries.stream().mapToLong(SubscrIntervalDelivery::getId).boxed().collect(Collectors.toList()));
		inv.addAgreementDetail(iad);
		return newDetail;
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