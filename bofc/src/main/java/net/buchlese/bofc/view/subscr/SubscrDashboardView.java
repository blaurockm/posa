package net.buchlese.bofc.view.subscr;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class SubscrDashboardView extends AbstractBofcView {

	private final List<SubscrDelivery> deliveries1;
	private final List<SubscrDelivery> deliveries2;
	private final List<SubscrIntervalDelivery> intervalDeliveries;
	private final List<Subscription> subscriptions;
	private final List<Subscription> subscriptionsWithMemo;
	private final List<PosInvoice> tempInvoices;
	private final SubscrDAO dao;
	
	public SubscrDashboardView(SubscrDAO dao, LocalDate d) {
		super("subscrdashboard.ftl");
		this.dao = dao;
		List<SubscrDelivery> tempdelivs = dao.getDeliveriesPayflag(false);
		tempdelivs.forEach(i -> i.subscriberName = kunde(i));
		tempdelivs.sort(Comparator.comparing(SubscrDelivery::getSubscriberName));
		this.deliveries1 = tempdelivs;
		tempdelivs = dao.getDeliveriesSlipflag(false);
		tempdelivs.forEach(i -> i.subscriberName = kunde(i));
		tempdelivs.sort(Comparator.comparing(SubscrDelivery::getSubscriberName));
		this.deliveries2 = tempdelivs;
		List<SubscrIntervalDelivery> tempintdelivs = dao.getIntervalDeliveriesUnrecorded();
		tempintdelivs.forEach(i -> i.subscriberName = kunde(i));
		tempintdelivs.sort(Comparator.comparing(SubscrIntervalDelivery::getSubscriberName));
		this.intervalDeliveries = tempintdelivs;
		this.subscriptions = dao.getSubscriptionsForTimespan(d, d.plusMonths(1)).stream().filter(s -> s.getPaymentType().equals(PayIntervalType.EACHDELIVERY) == false).collect(Collectors.toList());
		this.subscriptionsWithMemo = dao.getSubscriptionsWithMemo();
		this.tempInvoices = dao.getTempInvoices();
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<Subscription> getSubscriptionsWithMemo() {
		return subscriptionsWithMemo;
	}

	public List<SubscrDelivery> getDeliveries() {
		return deliveries1;
	}

	public List<SubscrDelivery> getUnslippedDeliveries() {
		return deliveries2;
	}

	public List<SubscrIntervalDelivery> getIntervalDeliveries() {
		return intervalDeliveries;
	}

	public List<PosInvoice> getInvoices() {
		return tempInvoices;
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

	public String kunde(SubscrIntervalDelivery d) {
		if (d.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(d.getSubscriberId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + d.getSubscriberId();
		}
		return "keine subId";
	}

	public Subscription abo(SubscrIntervalDelivery d) {
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
