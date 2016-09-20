package net.buchlese.verw.reports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.reports.obj.ReportDeliveryProtocol;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.SubscrArticleRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscriptionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportDeliveryProtocolCreator {

	@Autowired CustomerRepository custRepo;
	@Autowired SubscriptionRepository subscrRepo;
	@Autowired SubscrDeliveryRepository delivRepo;
	@Autowired SubscrArticleRepository articleRepo;
	
	public ReportDeliveryProtocol create(LocalDate date) {
		ReportDeliveryProtocol rep = new ReportDeliveryProtocol();
		rep.protocolDate = date;
		List<ReportDeliveryProtocol.ProtocolDetail> dets = new ArrayList<>();
		for (SubscrDelivery del : delivRepo.findByDeliveryDate(date)) {
			ReportDeliveryProtocol.ProtocolDetail det = new ReportDeliveryProtocol.ProtocolDetail();
			det.article = del.getArticle();
			Subscription sub = subscrRepo.findOne(del.getSubscriptionId());
			Subscriber s = custRepo.findOne(del.getSubscriberId());
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
