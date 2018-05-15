package net.buchlese.bofc.view.subscr;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrDispoView extends AbstractBofcView{

	private final SubscrProduct p;
	private final Set<Subscription> subscriptions;
	private final SubscrArticle article;
	private final SubscrArticle lastArticle;
	private final LocalDate dispoDate;
	private final Map<Subscription, SubscrDelivery> deliveries;


	public SubscrDispoView(SubscrDAO dao, SubscrProduct p, SubscrArticle art, LocalDate dispoDate) {
		super("subscrdispo.ftl");
		this.p =  p;
		this.subscriptions = p.getSubscriptions();
		this.lastArticle = dao.getNewestArticleOfProduct(p);
		if (art == null) {
			this.article = lastArticle;
		} else {
			this.article = art;
		}
		this.dispoDate = dispoDate;
		deliveries = new HashMap<>();
		for (Subscription sub : subscriptions) {
			deliveries.put(sub, dao.getDeliveriesForSubscription(sub).stream().filter(d -> d.getArticle().getId() == article.getId()).findFirst().orElse(null));
		}
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

	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public SubscrDelivery delivery(Subscription sub) {
		return  deliveries.get(sub);
	}
	
	public String kunde(Subscription s) {
		return s.getSubscriber().getName();
	}

	public boolean isShowArticlePlusEins() {
		return article.getErschTag().equals(java.sql.Date.valueOf(dispoDate)) == false && article.getId() == lastArticle.getId();
	}
}
