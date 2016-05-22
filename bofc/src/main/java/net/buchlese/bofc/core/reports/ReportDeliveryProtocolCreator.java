package net.buchlese.bofc.core.reports;

import java.util.ArrayList;
import java.util.List;

import net.buchlese.bofc.api.bofc.ReportDeliveryProtocol;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

import org.joda.time.LocalDate;

public class ReportDeliveryProtocolCreator {

	public static ReportDeliveryProtocol create(SubscrDAO dao, LocalDate date) {
		ReportDeliveryProtocol rep = new ReportDeliveryProtocol();
		rep.protocolDate = date;
		List<ReportDeliveryProtocol.ProtocolDetail> dets = new ArrayList<>();
		for (SubscrDelivery del : dao.getDeliveries(date)) {
			ReportDeliveryProtocol.ProtocolDetail det = new ReportDeliveryProtocol.ProtocolDetail();
			det.article = dao.getSubscrArticle(del.getArticleId());
			Subscription sub = dao.getSubscription(del.getSubscriptionId());
			Subscriber s = dao.getSubscriber(del.getSubscriberId());
			if (sub.getDeliveryAddress() != null) {
				det.deliveryAddress = sub.getDeliveryAddress();
			} else {
				det.deliveryAddress = s.getInvoiceAddress();
			}
			det.needsInvoice = sub.getPaymentType().equals(PayIntervalType.EACHDELIVERY);
			det.needsDeliveryNote = s.isNeedsDeliveryNote();
			det.subscriber = s;
			det.subscription = sub;
			det.quantity = del.getQuantity();
			dets.add(det);
		}
		rep.details = dets;
		return rep;
	}

}
