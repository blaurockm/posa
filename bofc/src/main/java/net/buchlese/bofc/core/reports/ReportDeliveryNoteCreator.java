package net.buchlese.bofc.core.reports;

import java.util.ArrayList;
import java.util.List;

import net.buchlese.bofc.api.bofc.ReportDeliveryNote;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.core.DateUtils;

public class ReportDeliveryNoteCreator {

	public static ReportDeliveryNote create(long num, List<SubscrDelivery> deliveries) {
		if (deliveries.isEmpty()) {
			return null;
		}
		ReportDeliveryNote rep = new ReportDeliveryNote();
		SubscrDelivery fdeli = deliveries.get(0);
		Subscriber s = fdeli.getSubscriber();
		Subscription sub1 = fdeli.getSubscription();
		rep.customerId = s.getCustomerId();
		rep.details = new ArrayList<ReportDeliveryNote.ReportDeliveryNoteDetail>();
		
		rep.deliveryAddress = sub1.getDeliveryAddress() != null ? sub1.getDeliveryAddress() : s.getInvoiceAddress();
		rep.deliveryDate = fdeli.getDeliveryDate();
		rep.delivNum  = num;
		rep.creationTime = DateUtils.nowTime();
		rep.pointId = s.getPointid();
		for (SubscrDelivery deli : deliveries) {
			Subscription sub = deli.getSubscription();
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
		return rep;
	}

}
