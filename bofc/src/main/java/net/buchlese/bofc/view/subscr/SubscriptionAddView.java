package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriptionAddView extends AbstractBofcView{

	private final SubscrDAO dao;
	private final Subscriber subscriber;
	private final SubscrProduct product;
	
	public SubscriptionAddView(SubscrDAO dao, Subscriber sub, SubscrProduct prod) {
		super("subscriptionadd.ftl");
		this.dao = dao;
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
