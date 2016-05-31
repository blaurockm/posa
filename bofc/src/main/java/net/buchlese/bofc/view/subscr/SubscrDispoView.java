package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class SubscrDispoView extends AbstractBofcView{

	private final SubscrProduct p;
	private final List<Subscription> subscriptions;
	private final SubscrDAO dao;
	private final SubscrArticle article;
	private final LocalDate dispoDate;


	public SubscrDispoView(SubscrDAO dao, SubscrProduct p, SubscrArticle art, LocalDate dispoDate) {
		super("subscrdispo.ftl");
		this.dao = dao;
		this.p = p;
		this.subscriptions = dao.getSubscriptionsForProduct(p.getId());
		if (art == null) {
			this.article = dao.getNewestArticleOfProduct(p.getId());
		} else {
			this.article = art;
		}
		this.dispoDate = dispoDate;
	}

	public LocalDate getDispoDate() {
		return dispoDate;
	}
	
	public SubscrProduct getP() {
		return p;
	}

	public SubscrArticle getArt() {
		return article;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<SubscrDelivery> deliveries(Subscription s) {
		return dao.getDeliveriesForSubscriptionUnrecorded(s.getId());
	}

	public SubscrDelivery delivery(Subscription sub, SubscrArticle art) {
		return dao.getDeliveriesForSubscription(sub.getId()).stream().filter(d -> d.getArticleId() == art.getId() && d.getDeliveryDate().equals(dispoDate)).findFirst().orElse(null);
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
		return article.getErschTag().equals(dispoDate) == false;
	}
}
