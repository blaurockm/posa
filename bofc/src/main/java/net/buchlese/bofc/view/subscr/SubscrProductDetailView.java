package net.buchlese.bofc.view.subscr;

import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class SubscrProductDetailView extends AbstractBofcView{

	private final SubscrProduct p;
	private final List<Subscription> subscriptions;
	private final List<SubscrArticle> articles;
	private final List<SubscrInterval> intervals;
	private final SubscrArticle lastArticle;
	private final SubscrInterval lastInterval;
	private final SubscrDAO dao;
	
	public SubscrProductDetailView(SubscrDAO dao, SubscrProduct p, List<Subscription> subs) {
		super("productdetail.ftl");
		this.dao = dao;
		this.subscriptions = subs;
		this.articles = dao.getArticlesOfProduct(p.getId());
		this.intervals = dao.getIntervalsOfProduct(p.getId());
		this.lastArticle = articles.isEmpty() ? null : articles.get(articles.size()-1);
		this.lastInterval = intervals.isEmpty() ? null : intervals.get(intervals.size()-1);
		this.p = p;
	}


	public SubscrProduct getP() {
		return p;
	}

	public SubscrArticle getLastArticle() {
		return lastArticle;
	}

	public SubscrInterval getLastInterval() {
		return lastInterval;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<SubscrArticle> getArticles() {
		return articles;
	}
	
	public List<SubscrInterval> getIntervals() {
		return intervals;
	}
	
	public List<SubscrDelivery> deliveries(Subscription s) {
		return dao.getDeliveriesForSubscription(s.getId());
	}

	public List<PosInvoice> invoices() {
		return Collections.emptyList();
	}

	public boolean willBeSettled(Subscription s) {
		switch (s.getPaymentType()) {
		case EACHDELIVERY : return hasUnpayedDeliveries(s);
			default : return s.getPayedUntil() == null || s.getPayedUntil().isBefore(LocalDate.now());
		}
	}

	private boolean hasUnpayedDeliveries(Subscription s) {
		return dao.getDeliveriesForSubscriptionUnrecorded(s.getId()).isEmpty() == false;
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


}
