package net.buchlese.bofc.jdbi.bofc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
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

public class SubscrDAO {
	private final JpaPosInvoiceDAO jpaPosInvoiceDao;
	private final JpaPosIssueSlipDAO jpaPosIssueSlipDao;
	private final JpaSubscriberDAO jpaSubscriberDao;
	private final JpaSubscriptionDAO jpaSubscriptionDao;
	private final JpaSubscrProductDAO jpaSubscrProductDao;
	private final JpaSubscrArticleDAO jpaSubscrArticleDao;
	private final JpaSubscrDeliveryDAO jpaSubscrDeliveryDao;
	private final JpaSubscrIntervalDAO jpaSubscrIntervalDao;
	private final JpaSubscrIntervalDeliveryDAO jpaSubscrIntervalDeliveryDao;
	
	
	public SubscrDAO(JpaSubscriberDAO j1, JpaSubscriptionDAO j2, JpaSubscrProductDAO j3, JpaSubscrArticleDAO j4,
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

	
	public List<SubscrDelivery> getDeliveries(Date now) {
		return jpaSubscrDeliveryDao.getDeliveries(now);
	}

	
	public List<SubscrDelivery> getDeliveriesForSubscription(Subscription id) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscription(id);
	}

	
	public List<SubscrDelivery> getDeliveriesForSubscription(Subscription id, LocalDate from, LocalDate till) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscription(id, from, till);
	}

	
	public List<SubscrDelivery> getDeliveriesForSubscriptionPayflag(Subscription id, boolean payed) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscriptionPayflag(id, payed);
	}

	
	public List<SubscrDelivery> getDeliveriesForSubscriptionSlipflag(Subscription id, boolean slipped) {
		return jpaSubscrDeliveryDao.getDeliveriesForSubscriptionSlipflag(id, slipped);
	}

	
	public List<PosInvoice> getInvoicesForDetails(Subscription id) {
		return jpaPosInvoiceDao.getInvoicesForDetails(id);
	}
	
	
	public List<PosIssueSlip> getSubscriberIssueSlips(int debId) {
		return jpaPosIssueSlipDao.getSubscriberIssueSlips(debId);
	}
	
	public SubscrDelivery getLastDeliveryForSubscription(Subscription id) {
		return jpaSubscrDeliveryDao.getLastDeliveryForSubscription(id);
	}

	
	public SubscrArticle getNewestArticleOfProduct(SubscrProduct prod) {
		return jpaSubscrArticleDao.getNewestArticleOfProduct(prod);
	}

	
	public List<SubscrProduct> getProductsForTimespan(LocalDate from, LocalDate till) {
		return jpaSubscrProductDao.getProductsForTimespan(from, till);
	}

	
	public SubscrArticle getSubscrArticle(long id) {
		return jpaSubscrArticleDao.findById(id);
	}

	
	public SubscrDelivery getSubscrDelivery(long delId) {
		return jpaSubscrDeliveryDao.findById(delId);
	}

	
	public Subscriber getSubscriber(long id) {
		return jpaSubscriberDao.findById(id);
	}

	
	public List<Subscriber> getSubscribers() {
		return jpaSubscriberDao.findAll();
	}
	
	public List<Subscription> getSubscriptionsWithMemo() {
		return Collections.emptyList();
//		return getSubscriptionCache().values().stream().filter(s -> s.getMemo() != null && s.getMemo().isEmpty() == false).collect(Collectors.toList());
	}

	
	public List<Subscription> getSubscriptionsForTimespan(LocalDate from, LocalDate till) {
		return jpaSubscriptionDao.getSubscriptionsForTimespan(java.sql.Date.valueOf(from), java.sql.Date.valueOf(till));
	}

	
	public SubscrProduct getSubscrProduct(long id) {
		return jpaSubscrProductDao.findById(id);
	}

	
	public List<Subscriber> querySubscribers(String query) {
		return jpaSubscriberDao.querySubscribers(query);
	}

	
	public List<SubscrProduct> querySubscrProducts(String query) {
		return jpaSubscrProductDao.querySubscrProducts(query);
	}

	
	public void recordDetailsOnInvoice(Set<SubscrDelivery> deliveryIds, String invNumber) {
		jpaSubscrDeliveryDao.recordDetailsOnInvoice(deliveryIds, invNumber);
	}

	
	public void resetDetailsOfInvoice(Set<SubscrDelivery> deliveryIds) {
		jpaSubscrDeliveryDao.resetDetailsOfInvoice(deliveryIds);
	}
	
	public void updateSubscription(Subscription art) {
		jpaSubscriptionDao.update(art);
	}

	
	public void updateSubscrProduct(SubscrProduct p) {
		jpaSubscrProductDao.update(p);
	}

	
	public List<PosInvoice> getSubscriberInvoices(int debId) {
		return jpaPosInvoiceDao.findByDebitorNumber(debId);
	}

	
	public List<PosIssueSlip> findIssueSlipsToAdd(int id) {
		return jpaPosIssueSlipDao.findIssueSlipsToAdd(id);
	}

	
	public PosIssueSlip getIssueSlip(long id) {
		return jpaPosIssueSlipDao.findById(id);
	}

	
	public void updateIssueSlip(PosIssueSlip inv) {
		jpaPosIssueSlipDao.update(inv);
	}

	
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(Subscription id) {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesForSubscription(id);
	}

	
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(Subscription id, LocalDate from, LocalDate till) {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesForSubscription(id, from, till);
	}

	
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionPayflag(Subscription id, boolean payed) {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesForSubscriptionPayflag(id, payed);
	}

	
	public List<PosInvoice> getInvoicesForIntervalDetails(Subscription id) {
		return jpaPosInvoiceDao.getInvoicesForIntervalDetails(id);
	}

	
	public SubscrIntervalDelivery getLastIntervalDeliveryForSubscription(Subscription id) {
		return jpaSubscrIntervalDeliveryDao.getLastIntervalDeliveryForSubscription(id);
	}

	
	public SubscrInterval getNewestIntervalOfProduct(SubscrProduct prodid) {
		return jpaSubscrIntervalDao.getNewestIntervalOfProduct(prodid);
	}

	
	public SubscrInterval getSubscrInterval(long id) {
		return jpaSubscrIntervalDao.findById(id);
	}

	
	public SubscrIntervalDelivery getSubscrIntervalDelivery(long delId) {
		return jpaSubscrIntervalDeliveryDao.findById(delId);
	}

	
	public void recordIntervalDetailsOnInvoice(Set<SubscrIntervalDelivery> deliveryIds,	String invNumber) {
		jpaSubscrIntervalDeliveryDao.recordIntervalDetailsOnInvoice(deliveryIds, invNumber);
	}

	
	public void resetIntervalDetailsOfInvoice(Set<SubscrIntervalDelivery> deliveryIds) {
		jpaSubscrIntervalDeliveryDao.resetIntervalDetailsOfInvoice(deliveryIds);
	}

	
	public List<SubscrIntervalDelivery> getIntervalDeliveriesUnrecorded() {
		return jpaSubscrIntervalDeliveryDao.getIntervalDeliveriesUnrecorded();
	}

	
	public List<SubscrDelivery> getDeliveriesPayflag(boolean payed) {
		return jpaSubscrDeliveryDao.getDeliveriesPayflag(payed);
	}

	
	public List<SubscrDelivery> getDeliveriesSlipflag(boolean slipped) {
		return jpaSubscrDeliveryDao.getDeliveriesSlipflag(slipped);
	}

	public void createInvoice(PosInvoice inv) {
		this.jpaPosInvoiceDao.create(inv);
	}

}
