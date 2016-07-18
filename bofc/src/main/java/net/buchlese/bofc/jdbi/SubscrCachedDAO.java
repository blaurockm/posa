package net.buchlese.bofc.jdbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.UserChange;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

public class SubscrCachedDAO implements SubscrDAO {
	private SubscrDAO dao;
	private final Map<Long, Subscription> subscriptionCache;
	private final Map<Long, Subscriber> subscriberCache;
	private final Map<Long, SubscrProduct> productCache;
	private final Map<Long, SubscrArticle> articleCache;
	private final Map<Long, SubscrInterval> intervalCache;
	
	
	public SubscrCachedDAO(SubscrDAO dec) {
		this.dao = dec;
		this.subscriptionCache = new HashMap<>(); 
		this.subscriberCache = new HashMap<>(); 
		this.productCache = new HashMap<>(); 
		this.articleCache = new HashMap<>(); 
		this.intervalCache = new HashMap<>(); 
		System.err.println("firing SubscrDAO Cache");
		List<Subscription> ls = dao.getSubscriptions();
		ls.stream().forEach(s -> subscriptionCache.put(s.getId(), s));
		List<Subscriber> lsub = dao.getSubscribers();
		lsub.stream().forEach(s -> subscriberCache.put(s.getId(), s));
	}
	
	@Override
	public void deleteDelivery(long delId) {
		dao.deleteDelivery(delId);
	}

	@Override
	public void deleteTempInvoice(String invNumber) {
		dao.deleteTempInvoice(invNumber);
	}

	@Override
	public List<SubscrArticle> getArticlesOfProduct(long prodid) {
		return dao.getArticlesOfProduct(prodid);
	}

	@Override
	public List<SubscrDelivery> getDeliveries(LocalDate now) {
		return dao.getDeliveries(now);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscription(long id) {
		return dao.getDeliveriesForSubscription(id);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscription(long id, LocalDate from, LocalDate till) {
		return dao.getDeliveriesForSubscription(id, from, till);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscriptionPayflag(long id, boolean payed) {
		return dao.getDeliveriesForSubscriptionPayflag(id, payed);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscriptionSlipflag(long id, boolean slipped) {
		return dao.getDeliveriesForSubscriptionSlipflag(id, slipped);
	}

	@Override
	public Collection<String> getInvoiceNumsForSubscription(long id) {
		return dao.getInvoiceNumsForSubscription(id);
	}

	@Override
	public List<PosInvoice> getSubscrInvoices(int id, LocalDate num) {
		return dao.getSubscrInvoices(id, num);
	}

	@Override
	public SubscrDelivery getLastDeliveryForSubscription(long id) {
		return dao.getLastDeliveryForSubscription(id);
	}

	@Override
	public SubscrArticle getNewestArticleOfProduct(long prodid) {
		return dao.getNewestArticleOfProduct(prodid);
	}

	@Override
	public List<SubscrProduct> getProductsForTimespan(LocalDate from, LocalDate till) {
		return dao.getProductsForTimespan(from, till);
	}

	@Override
	public SubscrArticle getSubscrArticle(long id) {
		if (articleCache.containsKey(id)) {
			return articleCache.get(id);
		}
		SubscrArticle art = dao.getSubscrArticle(id);
		articleCache.put(id, art);
		return art;
	}

	@Override
	public SubscrDelivery getSubscrDelivery(long delId) {
		return dao.getSubscrDelivery(delId);
	}

	@Override
	public Subscriber getSubscriber(long id) {
		if (subscriberCache.containsKey(id)) {
			return subscriberCache.get(id);
		}
		Subscriber art = dao.getSubscriber(id);
		subscriberCache.put(id, art);
		return art;
	}

	@Override
	public List<Subscriber> getSubscribers() {
		return new ArrayList<>(subscriberCache.values());
	}

	@Override
	public Subscription getSubscription(long id) {
		if (subscriptionCache.containsKey(id)) {
			return subscriptionCache.get(id);
		}
		Subscription art = dao.getSubscription(id);
		subscriptionCache.put(id, art);
		return art;
	}

	@Override
	public List<Subscription> getSubscriptions() {
		return new ArrayList<>(subscriptionCache.values());
	}
	
	@Override
	public List<Subscription> getSubscriptionsWithMemo() {
		return subscriptionCache.values().stream().filter(s -> s.getMemo() != null && s.getMemo().isEmpty() == false).collect(Collectors.toList());
	}

	@Override
	public List<Subscription> getSubscriptionsForProduct(long productId) {
		return dao.getSubscriptionsForProduct(productId);
	}

	@Override
	public List<Subscription> getSubscriptionsForSubscriber(long id) {
		return dao.getSubscriptionsForSubscriber(id);
	}

	@Override
	public List<Subscription> getSubscriptionsForTimespan(LocalDate from, LocalDate till) {
		return dao.getSubscriptionsForTimespan(from, till);
	}

	@Override
	public SubscrProduct getSubscrProduct(long id) {
		if (productCache.containsKey(id)) {
			return productCache.get(id);
		}
		SubscrProduct art = dao.getSubscrProduct(id);
		productCache.put(id, art);
		return art;
	}

	@Override
	public List<SubscrProduct> getSubscrProducts() {
		return dao.getSubscrProducts();
	}

	@Override
	public List<PosInvoice> getTempInvoices() {
		return dao.getTempInvoices();
	}

	@Override
	public PosInvoice getTempInvoice(String invNUm) {
		return dao.getTempInvoice(invNUm);
	}

	@Override
	public long insertArticle(SubscrArticle art) {
		long x = dao.insertArticle(art);
		art.setId(x);
		articleCache.put(x, art);
		return x;
	}

	@Override
	public void insertDelivery(SubscrDelivery d) {
		dao.insertDelivery(d);
	}

	@Override
	public long insertSubscriber(Subscriber p) {
		return dao.insertSubscriber(p);
	}

	@Override
	public long insertSubscription(Subscription p) {
		return dao.insertSubscription(p);
	}

	@Override
	public long insertSubscrProduct(SubscrProduct p) {
		long x = dao.insertSubscrProduct(p);
		p.setId(x);
		productCache.put(x, p);
		return x;
	}

	@Override
	public void insertTempInvoice(PosInvoice ti) {
		dao.insertTempInvoice(ti);
	}

	@Override
	public List<Subscriber> querySubscribers(String query) {
		return dao.querySubscribers(query);
	}

	@Override
	public List<SubscrProduct> querySubscrProducts(String query) {
		return dao.querySubscrProducts(query);
	}

	@Override
	public void recordDetailsOnInvoice(List<Long> deliveryIds, String invNumber) {
		dao.recordDetailsOnInvoice(deliveryIds, invNumber);
	}

	@Override
	public void resetDetailsOfInvoice(List<Long> deliveryIds) {
		dao.resetDetailsOfInvoice(deliveryIds);
	}

	@Override
	public void recordDetailsOnSlip(List<Long> deliveryIds, String invNumber) {
		dao.recordDetailsOnSlip(deliveryIds, invNumber);
	}

	@Override
	public void resetDetailsOfSlip(List<Long> deliveryIds) {
		dao.resetDetailsOfSlip(deliveryIds);
	}

	@Override
	public void updateArticle(SubscrArticle art) {
		dao.updateArticle(art);
		articleCache.put(art.getId(), art);
	}

	@Override
	public void updateDelivery(SubscrDelivery art) {
		dao.updateDelivery(art);
	}

	@Override
	public void updateSubscriber(Subscriber art) {
		dao.updateSubscriber(art);
		subscriberCache.put(art.getId(), art);
	}

	@Override
	public void updateSubscription(Subscription art) {
		dao.updateSubscription(art);
		subscriptionCache.put(art.getId(), art);
	}

	@Override
	public void updateSubscrProduct(SubscrProduct p) {
		dao.updateSubscrProduct(p);
		productCache.put(p.getId(), p);
	}

	@Override
	public void insert(UserChange u) {
		dao.insert(u);	
	}

	@Override
	public List<PosInvoice> getSubscriberInvoices(int debId) {
		return dao.getSubscriberInvoices(debId);
	}

	@Override
	public List<PosIssueSlip> findIssueSlipsToAdd(long id) {
		return dao.findIssueSlipsToAdd(id);
	}

	@Override
	public PosIssueSlip getIssueSlip(long id) {
		return dao.getIssueSlip(id);
	}

	@Override
	public void updateIssueSlip(PosIssueSlip inv) {
		dao.updateIssueSlip(inv);
	}

	@Override
	public void deleteIntervalDelivery(long delId) {
		dao.deleteIntervalDelivery(delId);
	}

	@Override
	public List<SubscrInterval> getIntervalsOfProduct(long prodid) {
		return dao.getIntervalsOfProduct(prodid);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(long id) {
		return dao.getIntervalDeliveriesForSubscription(id);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(long id, LocalDate from, LocalDate till) {
		return dao.getIntervalDeliveriesForSubscription(id, from, till);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionPayflag(long id, boolean payed) {
		return dao.getIntervalDeliveriesForSubscriptionPayflag(id, payed);
	}

	@Override
	public Collection<String> getInvoiceNumsForSubscriptionIntervals(long id) {
		return dao.getInvoiceNumsForSubscriptionIntervals(id);
	}

	@Override
	public SubscrIntervalDelivery getLastIntervalDeliveryForSubscription(long id) {
		return dao.getLastIntervalDeliveryForSubscription(id);
	}

	@Override
	public SubscrInterval getNewestIntervalOfProduct(long prodid) {
		return dao.getNewestIntervalOfProduct(prodid);
	}

	@Override
	public SubscrInterval getSubscrInterval(long id) {
		if (intervalCache.containsKey(id)) {
			return intervalCache.get(id);
		}
		SubscrInterval art = dao.getSubscrInterval(id);
		intervalCache.put(id, art);
		return art;
	}

	@Override
	public SubscrIntervalDelivery getSubscrIntervalDelivery(long delId) {
		return dao.getSubscrIntervalDelivery(delId);
	}

	@Override
	public long insertInterval(SubscrInterval art) {
		long x = dao.insertInterval(art);
		art.setId(x);
		intervalCache.put(x, art);
		return x;
	}

	@Override
	public void insertIntervalDelivery(SubscrIntervalDelivery d) {
		dao.insertIntervalDelivery(d);
	}

	@Override
	public void recordIntervalDetailsOnInvoice(List<Long> deliveryIds,	String invNumber) {
		dao.recordIntervalDetailsOnInvoice(deliveryIds, invNumber);
	}

	@Override
	public void resetIntervalDetailsOfInvoice(List<Long> deliveryIds) {
		dao.resetIntervalDetailsOfInvoice(deliveryIds);
	}

	@Override
	public void updateInterval(SubscrInterval art) {
		dao.updateInterval(art);
		intervalCache.put(art.getId(), art);
	}

	@Override
	public void updateIntervalDelivery(SubscrIntervalDelivery art) {
		dao.updateIntervalDelivery(art);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesUnrecorded() {
		return dao.getIntervalDeliveriesUnrecorded();
	}

	@Override
	public List<SubscrDelivery> getDeliveriesPayflag(boolean payed) {
		return dao.getDeliveriesPayflag(payed);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesSlipflag(boolean slipped) {
		return dao.getDeliveriesSlipflag(slipped);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscriberSlipflag(long id,	LocalDate n, boolean slipped) {
		return dao.getDeliveriesForSubscriberSlipflag(id, n, slipped);
	}

}
