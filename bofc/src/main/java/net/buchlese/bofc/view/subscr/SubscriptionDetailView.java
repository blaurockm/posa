package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriptionDetailView extends AbstractBofcView{

	private final Subscription sub;
	private final SubscrDAO dao;
	
	public SubscriptionDetailView(SubscrDAO dao, Subscription s ) {
		super("subscriptiondetail.ftl");
		this.dao = dao;
		this.sub = s;
	}


	public SubscrProduct getP() {
		return dao.getSubscrProduct(sub.getProductId());
	}

	public Subscription getSub() {
		return sub;
	}

	public List<SubscrDelivery> getDeliveriesWithout() {
		return dao.getDeliveriesForSubscription(sub.getId());
	}

	public List<SubscrDelivery> getDeliveriesWith() {
		return dao.getDeliveriesForSubscription(sub.getId());
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

	public String artikelbez(SubscrDelivery d) {
		if (d.getArticle() != null) {
			return d.getArticle().getName();
		}
		return "keine artId";
	}

}
