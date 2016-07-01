package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrIntervalDeliveryView extends AbstractBofcView{

	private final SubscrIntervalDelivery del;
	private final SubscrDAO dao;
	
	public SubscrIntervalDeliveryView(SubscrDAO dao, SubscrIntervalDelivery d) {
		super("subscrintervaldelivery.ftl");
		this.dao = dao;
		this.del = d;
	}


	public SubscrIntervalDelivery getD() {
		return del;
	}

	public String kunde(SubscrIntervalDelivery s) {
		if (s.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(s.getSubscriberId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + s.getSubscriberId();
		}
		return "keine subId";
	}
	
	public Subscription abo(SubscrIntervalDelivery d) {
		if (d.getSubscriptionId() > 0) {
			Subscription x = dao.getSubscription(d.getSubscriptionId());
			if (x != null) {
				return x;
			}
			return null;
		}
		return null;
	}

}
