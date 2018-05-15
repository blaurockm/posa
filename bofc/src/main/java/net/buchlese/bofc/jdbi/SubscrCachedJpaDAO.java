package net.buchlese.bofc.jdbi;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import net.buchlese.bofc.jpa.JpaPosInvoiceDAO;
import net.buchlese.bofc.jpa.JpaPosIssueSlipDAO;
import net.buchlese.bofc.jpa.JpaSubscrArticleDAO;
import net.buchlese.bofc.jpa.JpaSubscrDeliveryDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDeliveryDAO;
import net.buchlese.bofc.jpa.JpaSubscrProductDAO;
import net.buchlese.bofc.jpa.JpaSubscriberDAO;
import net.buchlese.bofc.jpa.JpaSubscriptionDAO;

public class SubscrCachedJpaDAO implements SubscrDAO {
	private final JpaPosInvoiceDAO jpaPosInvoiceDao;
	private final JpaPosIssueSlipDAO jpaPosIssueSlipDao;
	private final JpaSubscriberDAO jpaSubscriberDao;
	private final JpaSubscriptionDAO jpaSubscriptionDao;
	private final JpaSubscrProductDAO jpaSubscrProductDao;
	private final JpaSubscrArticleDAO jpaSubscrArticleDao;
	private final JpaSubscrDeliveryDAO jpaSubscrDeliveryDao;
	private final JpaSubscrIntervalDAO jpaSubscrIntervalDao;
	private final JpaSubscrIntervalDeliveryDAO jpaSubscrIntervalDeliveryDao;
	
	
	public SubscrCachedJpaDAO(JpaSubscriberDAO j1, JpaSubscriptionDAO j2, JpaSubscrProductDAO j3, JpaSubscrArticleDAO j4,
			JpaSubscrDeliveryDAO j5, JpaSubscrIntervalDAO j6, JpaSubscrIntervalDeliveryDAO j7, JpaPosInvoiceDAO j8,
			JpaPosIssueSlipDAO j9) {
		this.jpaSubscriberDao = j1;
		this.jpaSubscriptionDao = j2;
		this.jpaSubscrProductDao = j3;
		this.jpaSubscrArticleDao = j4;
		this.jpaSubscrDeliveryDao = j5;
		this.jpaSubscrIntervalDao = j6;
		this.jpaSubscrIntervalDeliveryDao = j7;
		this.jpaPosIssueSlipDao = j9;
		this.jpaPosInvoiceDao = j8;
	}
	
	@Override
	public void deleteDelivery(long delId) {
		jpaSubscrDeliveryDao.delete(jpaSubscrDeliveryDao.findById(delId));
	}

	@Override
	public List<SubscrArticle> getArticlesOfProduct(long prodid) {
		return jpaSubscrArticleDao.getArticlesOfProduct(prodid);
	}

	@Override
	public List<SubscrDelivery> getDeliveries(Date now) {
		return jpaSubscrDeliveryDao.getDeliveries(now);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscription(Subscription id) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscription(id);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscription(Subscription id, LocalDate from, LocalDate till) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscription(id, from, till);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscriptionPayflag(Subscription id, boolean payed) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscriptionPayflag(id, payed);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscriptionSlipflag(Subscription id, boolean slipped) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscriptionSlipflag(id, slipped);
	}

	@Override
	public List<PosInvoice> getInvoicesForDetails(Subscription id) {
		return jpaPosInvoiceDao.getInvoicesForDetails(id);
	}
	
	@Override
	public List<PosIssueSlip> getSubscriberIssueSlips(int debId) {
		return jpaPosIssueSlipDao.getSubscriberIssueSlips(debId);
	}


	@Override
	public List<PosInvoice> getSubscrInvoices(int id, LocalDate num) {
		return jpaPosInvoiceDao.getSubscrInvoices(id, java.sql.Date.valueOf(num));
	}

	@Override
	public SubscrDelivery getLastDeliveryForSubscription(Subscription id) {
		return jpaSubscrDeliveryDao.getLastDeliveryForSubscription(id);
	}

	@Override
	public SubscrArticle getNewestArticleOfProduct(SubscrProduct prod) {
		return jpaSubscrArticleDao.getNewestArticleOfProduct(prod);
	}

	@Override
	public List<SubscrProduct> getProductsForTimespan(LocalDate from, LocalDate till) {
		return jpaSubscrProductDao.getProductsForTimespan(from, till);
	}

	@Override
	public SubscrArticle getSubscrArticle(long id) {
		SubscrArticle art = jpaSubscrArticleDao.findById(id);
		return art;
	}

	@Override
	public SubscrDelivery getSubscrDelivery(long delId) {
		return jpaSubscrDeliveryDao.findById(delId);
	}

	@Override
	public Subscriber getSubscriber(long id) {
		Subscriber art = jpaSubscriberDao.findById(id);
		return art;
	}

	@Override
	public List<Subscriber> getSubscribers() {
		return jpaSubscriberDao.findAll();
	}

	@Override
	public Subscription getSubscription(long id) {
		Subscription art = jpaSubscriptionDao.findById(id);
		return art;
	}

	@Override
	public List<Subscription> getSubscriptions() {
		return jpaSubscriptionDao.findAll();
	}
	
	@Override
	public List<Subscription> getSubscriptionsWithMemo() {
		return Collections.emptyList();
//		return getSubscriptionCache().values().stream().filter(s -> s.getMemo() != null && s.getMemo().isEmpty() == false).collect(Collectors.toList());
	}

	@Override
	public List<Subscription> getSubscriptionsForProduct(long productId) {
		return jpaSubscriptionDao.getSubscriptionsForProduct(productId);
	}

	@Override
	public List<Subscription> getSubscriptionsForSubscriber(long id) {
		return jpaSubscriptionDao.getSubscriptionsForSubscriber(id);
	}

	@Override
	public List<Subscription> getSubscriptionsForTimespan(LocalDate from, LocalDate till) {
		return jpaSubscriptionDao.getSubscriptionsForTimespan(java.sql.Date.valueOf(from), java.sql.Date.valueOf(till));
	}

	@Override
	public SubscrProduct getSubscrProduct(long id) {
		SubscrProduct art = jpaSubscrProductDao.findById(id);
		return art;
	}

	@Override
	public List<SubscrProduct> getSubscrProducts() {
		return jpaSubscrProductDao.findAll();
	}

	@Override
	public long insertArticle(SubscrArticle art) {
		long x = jpaSubscrArticleDao.create(art);
		art.setId(x);
		return x;
	}

	@Override
	public void insertDelivery(SubscrDelivery d) {
		jpaSubscrDeliveryDao.create(d);
	}

	@Override
	public long insertSubscriber(Subscriber p) {
		long x = jpaSubscriberDao.create(p);
		p.setId(x);
		return x;
	}

	@Override
	public long insertSubscription(Subscription p) {
		return jpaSubscriptionDao.create(p);
	}

	@Override
	public long insertSubscrProduct(SubscrProduct p) {
		long x = jpaSubscrProductDao.create(p);
		p.setId(x);
		return x;
	}

	@Override
	public List<Subscriber> querySubscribers(String query) {
		return jpaSubscriberDao.querySubscribers(query);
	}

	@Override
	public List<SubscrProduct> querySubscrProducts(String query) {
		return jpaSubscrProductDao.querySubscrProducts(query);
	}

	@Override
	public void recordDetailsOnInvoice(Set<SubscrDelivery> deliveryIds, String invNumber) {
		jpaSubscrDeliveryDao.recordDetailsOnInvoice(deliveryIds, invNumber);
	}

	@Override
	public void resetDetailsOfInvoice(Set<SubscrDelivery> deliveryIds) {
		jpaSubscrDeliveryDao.resetDetailsOfInvoice(deliveryIds);
	}

	@Override
	public void recordDetailsOnSlip(Set<SubscrDelivery> deliveryIds, String invNumber) {
		jpaSubscrDeliveryDao.recordDetailsOnSlip(deliveryIds, invNumber);
	}

	@Override
	public void resetDetailsOfSlip(Set<SubscrDelivery> deliveryIds) {
		jpaSubscrDeliveryDao.resetDetailsOfSlip(deliveryIds);
	}

	@Override
	public void updateArticle(SubscrArticle art) {
		jpaSubscrArticleDao.update(art);
	}

	@Override
	public void updateDelivery(SubscrDelivery art) {
		jpaSubscrDeliveryDao.update(art);
	}

	@Override
	public void updateSubscriber(Subscriber art) {
		jpaSubscriberDao.update(art);
	}

	@Override
	public void updateSubscription(Subscription art) {
		jpaSubscriptionDao.update(art);
	}

	@Override
	public void updateSubscrProduct(SubscrProduct p) {
		jpaSubscrProductDao.update(p);
	}

	@Override
	public void insert(UserChange u) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public List<PosInvoice> getSubscriberInvoices(int debId) {
		return jpaPosInvoiceDao.findByDebitorNumber(debId);
//		return dao.getSubscriberInvoices(debId);
	}

	@Override
	public List<PosIssueSlip> findIssueSlipsToAdd(int id) {
		return jpaPosIssueSlipDao.findIssueSlipsToAdd(id);
	}

	@Override
	public PosIssueSlip getIssueSlip(long id) {
		return jpaPosIssueSlipDao.findById(id);
	}

	@Override
	public void updateIssueSlip(PosIssueSlip inv) {
		jpaPosIssueSlipDao.update(inv);
	}

	@Override
	public void deleteIntervalDelivery(long delId) {
		jpaSubscrIntervalDeliveryDao.delete(jpaSubscrIntervalDeliveryDao.findById(delId));
	}

	@Override
	public List<SubscrInterval> getIntervalsOfProduct(SubscrProduct prodid) {
		return jpaSubscrIntervalDao.getIntervalsOfProduct(prodid);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(Subscription id) {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesForSubscription(id);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(Subscription id, LocalDate from, LocalDate till) {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesForSubscription(id, from, till);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionPayflag(Subscription id, boolean payed) {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesForSubscriptionPayflag(id, payed);
	}

	@Override
	public List<PosInvoice> getInvoicesForIntervalDetails(Subscription id) {
		return jpaPosInvoiceDao.getInvoicesForIntervalDetails(id);
	}

	@Override
	public SubscrIntervalDelivery getLastIntervalDeliveryForSubscription(Subscription id) {
		return jpaSubscrIntervalDeliveryDao.getLastIntervalDeliveryForSubscription(id);
	}

	@Override
	public SubscrInterval getNewestIntervalOfProduct(SubscrProduct prodid) {
		return jpaSubscrIntervalDao.getNewestIntervalOfProduct(prodid);
	}

	@Override
	public SubscrInterval getSubscrInterval(long id) {
		SubscrInterval art = jpaSubscrIntervalDao.findById(id);
		return art;
	}

	@Override
	public SubscrIntervalDelivery getSubscrIntervalDelivery(long delId) {
		return jpaSubscrIntervalDeliveryDao.findById(delId);
	}

	@Override
	public long insertInterval(SubscrInterval art) {
		long x = jpaSubscrIntervalDao.create(art);
		art.setId(x);
		return x;
	}

	@Override
	public void insertIntervalDelivery(SubscrIntervalDelivery d) {
		jpaSubscrIntervalDeliveryDao.create(d);
	}

	@Override
	public void recordIntervalDetailsOnInvoice(Set<SubscrIntervalDelivery> deliveryIds,	String invNumber) {
		jpaSubscrIntervalDeliveryDao.recordIntervalDetailsOnInvoice(deliveryIds, invNumber);
	}

	@Override
	public void resetIntervalDetailsOfInvoice(Set<SubscrIntervalDelivery> deliveryIds) {
		jpaSubscrIntervalDeliveryDao.resetIntervalDetailsOfInvoice(deliveryIds);
	}

	@Override
	public void updateInterval(SubscrInterval art) {
		jpaSubscrIntervalDao.update(art);
	}

	@Override
	public void updateIntervalDelivery(SubscrIntervalDelivery art) {
		jpaSubscrIntervalDeliveryDao.update(art);
	}

	@Override
	public List<SubscrIntervalDelivery> getIntervalDeliveriesUnrecorded() {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesUnrecorded();
	}

	@Override
	public List<SubscrDelivery> getDeliveriesPayflag(boolean payed) {
		return jpaSubscrDeliveryDao.getDeliveriesPayflag(payed);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesSlipflag(boolean slipped) {
		return jpaSubscrDeliveryDao.getDeliveriesSlipflag(slipped);
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscriberSlipflag(Subscriber id,	Date n, boolean slipped) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscriberSlipflag(id, n, slipped);
	}


	@Override
	public void createInvoice(PosInvoice inv) {
		this.jpaPosInvoiceDao.create(inv);
	}

}
