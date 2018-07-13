package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrIntervalDeliveryView extends AbstractBofcView{

	private final SubscrIntervalDelivery del;
	private final Subscription sub;
	
	public SubscrIntervalDeliveryView(SubscrDAO dao, SubscrIntervalDelivery d) {
		super("subscrintervaldelivery.ftl");
		this.del = d;
		this.sub = del.getSubscription();
	}


	public SubscrIntervalDelivery getD() {
		return del;
	}

	public Subscription getSub() {
		return sub;
	}

	public String kunde(SubscrIntervalDelivery s) {
		return s.getSubscriber().getName();
	}
	

}
