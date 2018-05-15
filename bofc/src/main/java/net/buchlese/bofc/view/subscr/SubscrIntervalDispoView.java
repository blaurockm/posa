package net.buchlese.bofc.view.subscr;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrIntervalDispoView extends AbstractBofcView{

	private final SubscrProduct p;
	private final Set<Subscription> subscriptions;
	private final SubscrInterval interval;
	private final LocalDate dispoDate;
	private final Map<Subscription, SubscrIntervalDelivery> deliveries;


	public SubscrIntervalDispoView(SubscrDAO dao, SubscrProduct p, SubscrInterval art, LocalDate dispoDate) {
		super("subscrintervaldispo.ftl");
		this.p = p;
		this.subscriptions = p.getSubscriptions();
		this.interval = art;
		this.dispoDate = dispoDate;
		deliveries = new HashMap<>();
		for (Subscription sub : subscriptions) {
			deliveries.put(sub, dao.getIntervalDeliveriesForSubscription(sub).stream().filter(d -> d.getInterval().getId() == interval.getId()).findFirst().orElse(null));
		}
	}

	public LocalDate getDispoDate() {
		return dispoDate;
	}
	
	public SubscrProduct getP() {
		return p;
	}

	public SubscrInterval getArt() {
		return interval;
	}

	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public SubscrIntervalDelivery delivery(Subscription sub) {
		return  deliveries.get(sub);
	}
	
	public String kunde(Subscription s) {
		return s.getSubscriber().getName();
	}

	public boolean isShowArticlePlusEins() {
		return interval.getEndDate().getTime() > System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30);
	}
}
