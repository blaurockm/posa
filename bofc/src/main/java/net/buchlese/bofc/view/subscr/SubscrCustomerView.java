package net.buchlese.bofc.view.subscr;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrCustomerView extends AbstractBofcView {

	private List<Subscriber> subscribers;
	
	public SubscrCustomerView(SubscrDAO dao) {
		super("subscrcustomer.ftl");
		this.subscribers = dao.getSubscribers().stream().sorted(Comparator.comparing(Subscriber::getName)).collect(Collectors.toList());
	}

	public List<Subscriber> getSubscribers() {
		return subscribers;
	}



}
