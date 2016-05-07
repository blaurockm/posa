package net.buchlese.bofc.view;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

public class SubscrDashboardView extends AbstractBofcView {

	private final List<SubscrProduct> products;
	private final List<SubscrDelivery> deliveries;
	private final SubscrDAO dao;
	
	public SubscrDashboardView(SubscrDAO dao, List<SubscrProduct> products,List<SubscrDelivery> deliveries) {
		super("subscrdashboard.ftl");
		this.dao = dao;
		this.products = products;
		this.deliveries = deliveries;
	}


	public List<SubscrProduct> getProducts() {
		return products;
	}

	public List<Subscriber> getSubscribers() {
		return dao.getSubscribers();
	}

	public List<SubscrDelivery> getDeliveries() {
		return deliveries;
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

	public String artikelbez(SubscrDelivery d) {
		if (d.getArticle() != null) {
			return d.getArticle().getName();
		}
		return "keine artId";
	}

}
