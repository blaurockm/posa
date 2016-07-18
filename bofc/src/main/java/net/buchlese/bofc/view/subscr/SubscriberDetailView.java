package net.buchlese.bofc.view.subscr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class SubscriberDetailView extends AbstractBofcView{

	private final Subscriber sub;
	private final SubscrDAO dao;
	private final PosInvoiceDAO invDao;

	public SubscriberDetailView(SubscrDAO dao, PosInvoiceDAO invd, Subscriber s ) {
		super("subscriberdetail.ftl");
		this.dao = dao;
		this.sub = s;
		this.invDao = invd;
	}


	public List<Subscription> getSubscriptions() {
		return dao.getSubscriptionsForSubscriber(sub.getId());
	}

	public Subscriber getSub() {
		return sub;
	}

	public List<SubscrDelivery> deliveries(Subscription s) {
		return dao.getDeliveriesForSubscription(s.getId());
	}

	public List<SubscrDelivery> deliveriesWithout(Subscription s) {
		return dao.getDeliveriesForSubscriptionPayflag(s.getId(), false);
	}

	public List<SubscrDelivery> deliveriesWith(Subscription s) {
		return dao.getDeliveriesForSubscriptionPayflag(s.getId(), true);
	}

	public boolean willBeSettled(Subscription s) {
		switch (s.getPaymentType()) {
		case EACHDELIVERY : return hasUnpayedDeliveries(s);
			default : return s.getPayedUntil() == null || s.getPayedUntil().isBefore(LocalDate.now());
		}
	}

	public List<PosIssueSlip> getIssueSlips() {
		List<PosIssueSlip> invs = new ArrayList<PosIssueSlip>();
		if (sub.getDebitorId() > 0) {
			invs.addAll(invDao.getSubscriberIssueSlips(sub.getDebitorId()));
		}
		return invs;
	}
	
	public List<PosInvoice> getInvoices() {
		Collection<String> invNums = dao.getInvoiceNumsForSubscription(sub.getId());
		List<PosInvoice> invs = new ArrayList<PosInvoice>();
		for (String num : invNums) {
			invs.addAll(invDao.fetch(num));
		}
		if (sub.getDebitorId() > 0) {
			invs.addAll(dao.getSubscriberInvoices(sub.getDebitorId()));
		}
		return invs;
	}

	private boolean hasUnpayedDeliveries(Subscription s) {
		return dao.getDeliveriesForSubscriptionPayflag(s.getId(), false).isEmpty() == false;
	}
	
	public String kunde(Subscription s) {
		if (s.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(s.getSubscriberId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + s.getSubscriberId();
		}
		return "keine subId";
	}

	public String product(Subscription s) {
		if (s.getProductId() > 0) {
			SubscrProduct x = dao.getSubscrProduct(s.getProductId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + s.getProductId();
		}
		return "keine subId";
	}


}
