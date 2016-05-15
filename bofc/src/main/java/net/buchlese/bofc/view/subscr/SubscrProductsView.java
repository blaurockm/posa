package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrProductsView extends AbstractBofcView {

	private final List<SubscrProduct> products;
	private final SubscrDAO dao;
	
	public SubscrProductsView(SubscrDAO dao, List<SubscrProduct> products) {
		super("subscrproducts.ftl");
		this.dao = dao;
		this.products = products;
	}


	public List<SubscrProduct> getProducts() {
		return products;
	}

	public List<Subscriber> getSubscribers() {
		return dao.getSubscribers();
	}

	public String kunde(SubscrDelivery d) {
		if (d.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(d.getSubscriberId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + d.getSubscriberId();
		}
		return "keine subId";
	}

}
