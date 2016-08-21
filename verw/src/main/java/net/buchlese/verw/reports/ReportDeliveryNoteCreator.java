package net.buchlese.verw.reports;

import java.time.LocalDate;
import java.util.ArrayList;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.reports.obj.ReportDeliveryNote;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.SubscrArticleRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscriptionRepository;
import net.buchlese.verw.util.NumberGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportDeliveryNoteCreator {

	@Autowired CustomerRepository custRepo;
	@Autowired SubscriptionRepository subscrRepo;
	@Autowired SubscrDeliveryRepository delivRepo;
	@Autowired SubscrArticleRepository articleRepo;

	@Autowired NumberGenerator numberGenerator;
	
	public ReportDeliveryNote create(long deliveryId) {
		ReportDeliveryNote rep = new ReportDeliveryNote();
		SubscrDelivery fdeli = delivRepo.findOne(deliveryId);
		Subscriber s = custRepo.findOne(fdeli.getSubscriberId());
		Subscription sub1 = subscrRepo.findOne(fdeli.getSubscriptionId());
		rep.customerId = s.getCustomerId();
		rep.details = new ArrayList<ReportDeliveryNote.ReportDeliveryNoteDetail>();
		
		rep.deliveryAddress = sub1.getDeliveryAddress() != null ? sub1.getDeliveryAddress() : s.getInvoiceAddress();
		rep.deliveryDate = fdeli.getDeliveryDate();
		rep.delivNum  = numberGenerator.getNextNumber();
		rep.creationTime = LocalDate.now();
		rep.pointId = s.getPointid();
		for (SubscrDelivery deli : delivRepo.findByDeliveryDate(fdeli.getDeliveryDate())) {
			if (deli.getSubscriberId() == fdeli.getSubscriberId()) {
				Subscription sub = subscrRepo.findOne(deli.getSubscriptionId());
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
