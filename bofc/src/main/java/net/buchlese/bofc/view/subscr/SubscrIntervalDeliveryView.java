package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrIntervalDeliveryView extends AbstractBofcView{

	private final SubscrIntervalDelivery del;
	private final SubscrDAO dao;
	private final Subscription sub;
	
	public SubscrIntervalDeliveryView(SubscrDAO dao, SubscrIntervalDelivery d) {
		super("subscrintervaldelivery.ftl");
		this.dao = dao;
		this.del = d;
		this.sub = dao.getSubscription(del.getSubscriptionId());
	}


	public SubscrIntervalDelivery getD() {
		return del;
	}

	public Subscription getSub() {
		return sub;
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
	

}
