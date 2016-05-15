package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class SubscrDashboardView extends AbstractBofcView {

	private final List<SubscrProduct> products;
	private final List<SubscrDelivery> deliveries;
	private final List<Subscription> subscriptions;
	private final SubscrDAO dao;
	
	public SubscrDashboardView(SubscrDAO dao, LocalDate d) {
		super("subscrdashboard.ftl");
		this.dao = dao;
		this.products = dao.getProductsForTheNextWeek(d);
		this.deliveries = dao.getDeliveries(d);
		this.subscriptions = dao.getSubscriptionsForThisMonth(d);
	}


	public List<SubscrProduct> getProducts() {
		return products;
	}

	public List<Subscriber> getSubscribers() {
		return dao.getSubscribers();
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
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

	public Subscription abo(SubscrDelivery d) {
		if (d.getSubscriptionId() > 0) {
			Subscription x = dao.getSubscription(d.getSubscriptionId());
			if (x != null) {
				return x;
			}
			return null;
		}
		return null;
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
