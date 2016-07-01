package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class SubscrIntervalDispoView extends AbstractBofcView{

	private final SubscrProduct p;
	private final List<Subscription> subscriptions;
	private final SubscrDAO dao;
	private final SubscrInterval article;
	private final LocalDate dispoDate;


	public SubscrIntervalDispoView(SubscrDAO dao, SubscrProduct p, SubscrInterval art, LocalDate dispoDate) {
		super("subscrintervaldispo.ftl");
		this.dao = dao;
		this.p = p;
		this.subscriptions = dao.getSubscriptionsForProduct(p.getId());
		this.article = art;
		this.dispoDate = dispoDate;
	}

	public LocalDate getDispoDate() {
		return dispoDate;
	}
	
	public SubscrProduct getP() {
		return p;
	}

	public SubscrInterval getArt() {
		return article;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<SubscrIntervalDelivery> deliveries(Subscription s) {
		return dao.getIntervalDeliveriesForSubscriptionUnrecorded(s.getId());
	}

	public SubscrIntervalDelivery delivery(Subscription sub, SubscrInterval art) {
		return dao.getIntervalDeliveriesForSubscription(sub.getId()).stream().filter(d -> d.getIntervalId() == art.getId()).findFirst().orElse(null);
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

	public boolean isShowArticlePlusEins() {
		return article.getEndDate().isAfter(dispoDate) == false;
	}
}
