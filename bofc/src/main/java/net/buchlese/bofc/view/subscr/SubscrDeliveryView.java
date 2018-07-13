package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrDeliveryView extends AbstractBofcView{

	private final SubscrDelivery del;
	private final SubscrProduct product;
	private final Subscription sub;
	
	public SubscrDeliveryView(SubscrDAO dao, SubscrDelivery d) {
		super("subscrdelivery.ftl");
		this.del = d;
		this.sub = d.getSubscription();
		this.product = d.getSubscription().getProduct();
		d.getSubscription().getShipmentType();
	}


	public SubscrDelivery getD() {
		return del;
	}

	public String kunde(SubscrDelivery d) {
		return d.getSubscriber().getName();
	}
	
	public Subscription abo(SubscrDelivery d) {
		return d.getSubscription();
	}

	public SubscrProduct getProduct() {
		return product;
	}


	public Subscription getSub() {
		return sub;
	}

}
