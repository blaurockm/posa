package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrCustomerView extends AbstractBofcView {

	private final SubscrDAO dao;
	private List<Subscriber> subscribers;
	
	public SubscrCustomerView(SubscrDAO dao) {
		super("subscrcustomer.ftl");
		this.dao = dao;
		this.subscribers = dao.getSubscribers();
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
