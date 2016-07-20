package net.buchlese.bofc.view.coupon;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class CouponCustomerView extends AbstractBofcView {

	private final SubscrDAO dao;
	private List<Subscriber> subscribers;
	
	public CouponCustomerView(SubscrDAO dao) {
		super("couponcustomer.ftl");
		this.dao = dao;
		this.subscribers = dao.getSubscribers().stream().sorted(Comparator.comparing(Subscriber::getName)).collect(Collectors.toList());
	}



	public List<Subscriber> getSubscribers() {
		return subscribers;
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

}
