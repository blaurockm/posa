package net.buchlese.bofc.core;

import java.util.Arrays;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
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
	public static PosInvoice createCollectiveSubscription(SubscrDAO dao, Subscriber subscriber) {
		return createSubscription(dao, subscriber, dao.getSubscriptionsForSubscriber(subscriber.getId()));
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

	public static PosInvoice createSubscription(SubscrDAO dao, Subscriber subscriber, List<Subscription> subs) {
		PosInvoice inv = createInvoice(subscriber.getPointid(), subscriber);
		
		// details
		for(Subscription sub : subs) {
			List<SubscrDelivery> deliveries = dao.getDeliveriesForSubscriptionUnrecorded(sub.getId());
			if (deliveries.isEmpty()) {
				continue;
			}
			addTextDetail(inv, sub.getDeliveryInfo1());
			addTextDetail(inv, sub.getDeliveryInfo2());
			// details per Delivery;
			for (SubscrDelivery deliv : deliveries) {
				inv.addInvoiceDetail(createInvoiceDetailForDelivery(deliv,dao.getSubscrArticle(deliv.getArticleId())));
				deliv.setInvoiceNumber(inv.getNumber());
				deliv.setPayed(true);
			}
			dao.recordDetailsOnvInvoice(deliveries, inv.getNumber());
			// Versandkosten
			if (sub.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, sub.getShipmentCost());
			}

		}
		return inv;
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