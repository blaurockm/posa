package net.buchlese.bofc.core.reports;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import net.buchlese.bofc.api.bofc.ReportDeliveryNote;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.core.NumberGenerator;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

public class ReportDeliveryNoteCreator {

	public static ReportDeliveryNote create(SubscrDAO dao, NumberGenerator numgen, long deliveryId) {
		ReportDeliveryNote rep = new ReportDeliveryNote();
		SubscrDelivery fdeli = dao.getSubscrDelivery(deliveryId);
		Subscriber s = dao.getSubscriber(fdeli.getSubscriberId());
		rep.customerId = s.getCustomerId();
		rep.details = new ArrayList<ReportDeliveryNote.ReportDeliveryNoteDetail>();
		
		rep.deliveryAddress = s.getInvoiceAddress();
		rep.deliveryDate = fdeli.getDeliveryDate();
		rep.delivNum  = numgen.getNextNumber();
		rep.creationTime = LocalDate.now();
		rep.pointId = s.getPointid();
		for (SubscrDelivery deli : dao.getDeliveries(fdeli.getDeliveryDate())) {
			if (deli.getSubscriberId() == fdeli.getSubscriberId()) {
				Subscription sub = dao.getSubscription(deli.getSubscriptionId());
				if (sub.getDeliveryInfo1() != null & sub.getDeliveryInfo1().isEmpty() == false) {
					ReportDeliveryNote.ReportDeliveryNoteDetail det = new  ReportDeliveryNote.ReportDeliveryNoteDetail();
					det.textonly = true;
					det.text = sub.getDeliveryInfo1();
					rep.details.add(det);
				}
				if (sub.getDeliveryInfo2() != null & sub.getDeliveryInfo2().isEmpty() == false) {
					ReportDeliveryNote.ReportDeliveryNoteDetail det = new  ReportDeliveryNote.ReportDeliveryNoteDetail();
					det.textonly = true;
					det.text = sub.getDeliveryInfo2();
					rep.details.add(det);
				}
				ReportDeliveryNote.ReportDeliveryNoteDetail det = new  ReportDeliveryNote.ReportDeliveryNoteDetail();
				det.textonly = false;
				det.quantity = deli.getQuantity();
				det.text = deli.getArticleName();
				det.weight = 0.1d;
				rep.details.add(det);
			}
		}
		return rep;
	}

}
