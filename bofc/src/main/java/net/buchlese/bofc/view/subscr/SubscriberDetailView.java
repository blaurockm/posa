package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriberDetailView extends AbstractBofcView{

	private final Subscriber sub;
	private final SubscrDAO dao;
	
	public SubscriberDetailView(SubscrDAO dao, Subscriber s ) {
		super("subscriberdetail.ftl");
		this.dao = dao;
		this.sub = s;
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
		return dao.getDeliveriesForSubscriptionUnrecorded(s.getId());
	}

	public List<SubscrDelivery> deliveriesWith(Subscription s) {
		return dao.getDeliveriesForSubscriptionRecorded(s.getId());
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
