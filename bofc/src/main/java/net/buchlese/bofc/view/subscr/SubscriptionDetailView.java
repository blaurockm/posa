package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriptionDetailView extends AbstractBofcView{

	private final Subscription sub;
	private final SubscrDAO dao;
	private final SubscrDelivery lastDeliv;
	
	public SubscriptionDetailView(SubscrDAO dao, Subscription s ) {
		super("subscriptiondetail.ftl");
		this.dao = dao;
		this.sub = s;
		this.lastDeliv = dao.getLastDeliveryForSubscription(s.getId());
	}


	public SubscrProduct getP() {
		return dao.getSubscrProduct(sub.getProductId());
	}

	public Subscription getSub() {
		return sub;
	}

	public SubscrDelivery getLastDelivery() {
		return lastDeliv;
	}

	public PosInvoice getLastInvoice() {
		return null;
	}

	public List<SubscrDelivery> getDeliveriesWithout() {
		return dao.getDeliveriesForSubscriptionUnrecorded(sub.getId());
	}

	public List<SubscrDelivery> getDeliveriesWith() {
		return dao.getDeliveriesForSubscriptionRecorded(sub.getId());
	}

	public String kunde() {
		if (sub.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(sub.getSubscriberId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + sub.getSubscriberId();
		}
		return "keine subId";
	}


}
