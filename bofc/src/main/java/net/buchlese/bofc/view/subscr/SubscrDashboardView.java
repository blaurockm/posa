package net.buchlese.bofc.view.subscr;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrDashboardView extends AbstractBofcView {

	private final List<SubscrDelivery> deliveries1;
	private final List<SubscrDelivery> deliveries2;
	private final List<SubscrIntervalDelivery> intervalDeliveries;
	private final List<Subscription> subscriptions;
	private final List<Subscription> subscriptionsWithMemo;
	
	public SubscrDashboardView(SubscrDAO dao, LocalDate d) {
		super("subscrdashboard.ftl");
		List<SubscrDelivery> tempdelivs = dao.getDeliveriesPayflag(false);
		tempdelivs.forEach( s -> s.getSubscription().getDeliveryInfo1());
		tempdelivs.sort(Comparator.comparing(SubscrDelivery::getSubscriberName, Comparator.nullsFirst(Comparator.naturalOrder())));
		this.deliveries1 = tempdelivs;
		tempdelivs = dao.getDeliveriesSlipflag(false);
		tempdelivs.forEach( s -> s.getSubscription().getDeliveryInfo1());
		tempdelivs.sort(Comparator.comparing(SubscrDelivery::getSubscriberName, Comparator.nullsFirst(Comparator.naturalOrder()) ));
		this.deliveries2 = tempdelivs;
		List<SubscrIntervalDelivery> tempintdelivs = dao.getIntervalDeliveriesUnrecorded();
		tempintdelivs.forEach( s -> s.getSubscription().getDeliveryInfo1());
		tempintdelivs.sort(Comparator.comparing(SubscrIntervalDelivery::getSubscriberName, Comparator.nullsFirst(Comparator.naturalOrder())));
		this.intervalDeliveries = tempintdelivs;
		this.subscriptions = dao.getSubscriptionsForTimespan(d, d.plusMonths(1)).stream().filter(s -> s.getPaymentType().equals(PayIntervalType.EACHDELIVERY) == false).collect(Collectors.toList());
		this.subscriptionsWithMemo = dao.getSubscriptionsWithMemo();
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
	
	public String kunde(SubscrDelivery d) {
		return d.getSubscriberName();
	}

	public Subscription abo(SubscrDelivery d) {
		return d.getSubscription();
	}

	public String kunde(SubscrIntervalDelivery d) {
		return d.getSubscriberName();
	}

	public Subscription abo(SubscrIntervalDelivery d) {
		return d.getSubscription();
	}

	public String kunde(Subscription s) {
		return s.getSubscriber().getName();
	}
	
	public String product(Subscription s) {
		return s.getProduct().getName();
	}

}
