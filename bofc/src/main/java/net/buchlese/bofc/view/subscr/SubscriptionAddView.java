package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriptionAddView extends AbstractBofcView{

	private final Subscriber subscriber;
	private final SubscrProduct product;
	
	public SubscriptionAddView(SubscrDAO dao, Subscriber sub, SubscrProduct prod) {
		super("subscriptionadd.ftl");
		this.subscriber = sub;
		this.product = prod;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public SubscrProduct getSubscrProduct() {
		return product;
	}

	public String kunde(Subscription s) {
		return s.getSubscriber().getName();
	}

	public String product(Subscription s) {
		return s.getProduct().getName();
	}


}
