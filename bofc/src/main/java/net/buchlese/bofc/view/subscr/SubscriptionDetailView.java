package net.buchlese.bofc.view.subscr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaPosInvoiceDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriptionDetailView extends AbstractBofcView{

	private final Subscription sub;
	private final SubscrDAO dao;
	private final SubscrProduct prod;
	private final SubscrDelivery lastDeliv;
	private final SubscrArticle newestArticle;
	private final List<SubscrDelivery> unpayedDelivs;
	private final List<SubscrDelivery> payedDelivs;
	private final List<SubscrIntervalDelivery> unpayedIntDelivs;
	private final List<SubscrIntervalDelivery> payedIntDelivs;
	private final SubscrIntervalDelivery lastIntDeliv;
	private final SubscrInterval newestInterval;
	private final SessionFactory sessFact;

	
	public SubscriptionDetailView(SubscrDAO dao, Subscription s, SessionFactory sessFact ) {
		super("subscriptiondetail.ftl");
		this.dao = dao;
		this.sub = s;
		this.prod =  dao.getSubscrProduct(sub.getProductId());
		this.lastDeliv = dao.getLastDeliveryForSubscription(s.getId());
		this.newestArticle = dao.getNewestArticleOfProduct(s.getProductId());
		this.lastIntDeliv = dao.getLastIntervalDeliveryForSubscription(s.getId());
		this.newestInterval = dao.getNewestIntervalOfProduct(s.getProductId());
		this.unpayedDelivs = dao.getDeliveriesForSubscriptionPayflag(sub.getId(),false);
		this.payedDelivs = dao.getDeliveriesForSubscriptionPayflag(sub.getId(), true);
		this.unpayedIntDelivs = dao.getIntervalDeliveriesForSubscriptionPayflag(sub.getId(), false);
		this.payedIntDelivs = dao.getIntervalDeliveriesForSubscriptionPayflag(sub.getId(), true);
		this.sessFact = sessFact;
	}


	public SubscrProduct getP() {
		return prod;
	}

	public Subscription getSub() {
		return sub;
	}

	public SubscrDelivery getLastDelivery() {
		return lastDeliv;
	}

	public SubscrArticle getNewestArticle() {
		return newestArticle;
	}

	public SubscrIntervalDelivery getLastIntDelivery() {
		return lastIntDeliv;
	}

	public SubscrInterval getNewestInterval() {
		return newestInterval;
	}

	public PosInvoice getLastInvoice() {
		return null;
	}

	public boolean needsInvoice() {
		switch (sub.getPaymentType()) {
		case EACHDELIVERY : return hasUnpayedDeliveries(sub);
		default : return hasUnpayedIntervalDeliveries(sub);
		}
	}

	private boolean hasUnpayedDeliveries(Subscription s) {
		return unpayedDelivs.isEmpty() == false;
	}
	
	public List<SubscrDelivery> getDeliveriesWithout() {
		return unpayedDelivs;
	}

	public List<SubscrDelivery> getDeliveriesWith() {
		return payedDelivs;
	}
	
	private boolean hasUnpayedIntervalDeliveries(Subscription s) {
		return unpayedIntDelivs.isEmpty() == false;
	}
	
	public List<SubscrIntervalDelivery> getIntervalDeliveriesWithout() {
		return unpayedIntDelivs;
	}

	public List<SubscrIntervalDelivery> getIntervalDeliveriesWith() {
		return payedIntDelivs;
	}


	public List<PosInvoice> getInvoices() {
		org.hibernate.Session s = sessFact.openSession();
		ManagedSessionContext.bind(s);
		JpaPosInvoiceDAO jpaDao = new JpaPosInvoiceDAO(sessFact);
		Collection<String> invNums = dao.getInvoiceNumsForSubscription(sub.getId());
		List<PosInvoice> invs = new ArrayList<PosInvoice>();
		for (String num : invNums) {
			invs.addAll(jpaDao.findByNumber(num));
		}
		invNums = dao.getInvoiceNumsForSubscriptionIntervals(sub.getId());
		for (String num : invNums) {
			invs.addAll(jpaDao.findByNumber(num));
		}
		s.close();
		return invs;

	}

	public Subscriber kunde() {
		if (sub.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(sub.getSubscriberId());
			return x;
		}
		return null;
	}


}
