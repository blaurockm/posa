package net.buchlese.bofc.view.subscr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrProductDetailView extends AbstractBofcView{

	private final SubscrProduct p;
	private final List<Subscription> subscriptions;
	private final List<Subscription> invalidSubscriptions;
	private final List<SubscrArticle> articles;
	private final List<SubscrInterval> intervals;
	private final SubscrArticle lastArticle;
	private final SubscrInterval lastInterval;
	private final SubscrDAO dao;
	
	public SubscrProductDetailView(SubscrDAO dao, SubscrProduct p) {
		super("productdetail.ftl");
		this.dao = dao;
		this.subscriptions = new ArrayList<>();
		this.invalidSubscriptions = new ArrayList<>();
		if (p.getSubscriptions() != null) {
			for (Subscription su : p.getSubscriptions()) {
				if (su.isValid()) {
					this.subscriptions.add(su);
				} else {
					this.invalidSubscriptions.add(su);
				}
			}
		}
		this.articles = new ArrayList<>(p.getArticles());
		Collections.sort(this.articles, Collections.reverseOrder());
		this.intervals = new ArrayList<>(p.getIntervals()); 
		Collections.sort(this.intervals, Collections.reverseOrder());
		this.lastArticle = (articles == null || articles.isEmpty()) ? null : articles.iterator().next();
		this.lastInterval = (intervals == null || intervals.isEmpty()) ? null : intervals.iterator().next();
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
	public List<Subscription> getInvalidSubscriptions() {
		return invalidSubscriptions;
	}

	public List<SubscrArticle> getArticles() {
		return articles;
	}
	
	public List<SubscrInterval> getIntervals() {
		return intervals;
	}
	
	public List<SubscrDelivery> deliveries(Subscription s) {
		return dao.getDeliveriesForSubscription(s);
	}

	public List<PosInvoice> invoices() {
		return Collections.emptyList();
	}

	public String kunde(Subscription s) {
		return s.getSubscriber().getName();
	}


}
