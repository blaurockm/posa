package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrProductDetailView extends AbstractBofcView{

	private final SubscrProduct p;
	private final List<Subscription> subscriptions;
	private final SubscrDAO dao;
	
	public SubscrProductDetailView(SubscrDAO dao, SubscrProduct p, List<Subscription> subs) {
		super("productdetail.ftl");
		this.dao = dao;
		this.subscriptions = subs;
		this.p = p;
	}


	public SubscrProduct getP() {
		return p;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<SubscrDelivery> deliveries(Subscription s) {
		return dao.getDeliveriesForSubscription(s.getId());
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


}