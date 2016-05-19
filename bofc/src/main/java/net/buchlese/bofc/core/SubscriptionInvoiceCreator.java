package net.buchlese.bofc.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

public class SubscriptionInvoiceCreator {

	/**
	 * Sammelrechnung für einen Abonnenten
	 * @param dao
	 * @param subscriber
	 * @return
	 */
	public static PosInvoice createCollectiveSubscription(SubscrDAO dao, Subscriber subscriber) {
		PosInvoice inv = createSubscription(dao, subscriber, dao.getSubscriptionsForSubscriber(subscriber.getId()));
		inv.setCollective(true);
		return inv;
	}

	/**
	 * Einzelrechnung für ein Abo 
	 * @param dao
	 * @param sub
	 * @return
	 */
	public static PosInvoice createSubscription(SubscrDAO dao, Subscription sub) {
		return createSubscription(dao, dao.getSubscriber(sub.getSubscriberId()), Arrays.asList(sub));
	}

	/**
	 * festschreiben einer Rechnung
	 * @param dao
	 * @param invDao
	 * @param inv
	 */
	public static void recordInvoiceOnAgreements(SubscrDAO dao, PosInvoiceDAO invDao, PosInvoice inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			Subscription sub = dao.getSubscription(iad.getAgreementId());
			sub.setPayedUntil(iad.getDeliveryTill());
			dao.recordDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
			dao.updateSubscription(sub);
		}
		inv.setTemporary(false);
		invDao.insert(inv);
		dao.deleteTempInvoice(inv.getNumber());
	}

	/**
	 * festschreiben einer Rechnung wieder rückgängig machen
	 * @param dao
	 * @param invDao
	 * @param inv
	 */
	public static void undoRecordInvoiceOnAgreements(SubscrDAO dao, PosInvoiceDAO invDao, PosInvoice inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			Subscription sub = dao.getSubscription(iad.getAgreementId());
			sub.setPayedUntil(iad.getDeliveryFrom().minusDays(1));
			dao.recordDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
			dao.resetDetailsOfInvoice(iad.getDeliveryIds());
			dao.updateSubscription(sub);
		}
		inv.setTemporary(false);
		inv.setCancelled(true);
	//	invDao.updateInv(inv);
	}
	

	
	public static PosInvoice createSubscription(SubscrDAO dao, Subscriber subscriber, List<Subscription> subs) {
		PosInvoice inv = createInvoice(subscriber.getPointid(), subscriber);
		inv.setTemporary(true);
		dao.insertTempInvoice(inv);
		// details
		for(Subscription sub : subs) {
			InvoiceAgrDetail iad = null;
			switch(sub.getPaymentType()) {
			case EACHDELIVERY : iad = addDeliveriesToInvoice(dao, sub, inv, dao.getDeliveriesForSubscriptionUnrecorded(sub.getId()));
			break;
			case MONTHLY  :  
			case HALFYEARLY  :  
			case YEARLY : iad = addIntervalPayment(dao, sub, inv);
			}
			if (iad != null) {
				inv.addAgreementDetail(iad);
			}
		}
		inv.setNettoHalf((long) (inv.getAmountHalf() / 1.07));
		inv.setTaxHalf(inv.getAmountHalf() - inv.getNettoHalf());
		inv.setTax(inv.getTaxHalf());
		if (inv.getAmountFull() != null) {
			inv.setNettoFull((long) (inv.getAmountFull() / 1.19));
			inv.setTaxFull(inv.getAmountFull() - inv.getNettoFull());
			inv.setTax(inv.getTax() + inv.getTaxFull());
		}
		
		return inv;
	}

	private static InvoiceAgrDetail addIntervalPayment(SubscrDAO dao, Subscription sub, PosInvoice inv) {
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		LocalDate from = sub.getPayedUntil().plusMonths(1).dayOfMonth().withMinimumValue(); // immer der erste des nächsten monats
		LocalDate till = from.plus(sub.getPaymentType().getPeriod()).minusMonths(1); // und period -1 monat drauf
		addTextDetail(inv, sub.getDeliveryInfo1());
		addTextDetail(inv, sub.getDeliveryInfo2());
		inv.addInvoiceDetail(createInvoiceDetailForInterval(sub,dao.getNewestArticleOfProduct(sub.getProductId()), new YearMonth(from), new YearMonth(till)));

		iad.setAgreementId(sub.getId());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.dayOfMonth().withMaximumValue());  // immer der letzte des Monats
		iad.setDeliveryIds(dao.getDeliveriesForSubscription(sub.getId(), from, till).stream().mapToLong(SubscrDelivery::getId).boxed().collect(Collectors.toList()));
		return iad;
	}
	
	private static PosInvoiceDetail createInvoiceDetailForInterval(Subscription sub, SubscrArticle art, YearMonth from, YearMonth till) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(sub.getQuantity());
		if (from.equals(till)) {
			detail.setText(art.getName() + "  Zeitraum " + from.toString("MM/yyyy"));
		} else {
			detail.setText(art.getName() + "  Zeitraum von " + from.toString("MM/yyyy") + " bis " + till.toString("MM/yyyy"));
		}
		detail.setSinglePrice(art.getBrutto());
		detail.setAmount(art.getBrutto() * sub.getQuantity());
		detail.setAmountHalf(art.getBrutto_half() * sub.getQuantity());
		detail.setAmountFull(detail.getAmount() - detail.getAmountHalf());
		detail.setAmountNone(0L); // Abos ohne MwSt können wir noch nicht.
		return detail;
	}

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
		return iad;
	}
	
	
	
	private static PosInvoice createInvoice(int pointId, Subscriber subscri) {
		PosInvoice inv = new PosInvoice();
		
		// formalia
		inv.setNumber(NumberGenerator.createNumber(pointId));
		inv.setDate(LocalDate.now());
		inv.setCreationTime(DateTime.now());
		inv.setCustomerId(subscri.getCustomerId());
		inv.setDebitorId(subscri.getDebitorId());
		inv.setCancelled(false);
		inv.setPayed(false);
		inv.setPointid(pointId);
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