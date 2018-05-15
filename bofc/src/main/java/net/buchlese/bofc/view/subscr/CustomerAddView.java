package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class CustomerAddView extends AbstractBofcView{

	public CustomerAddView(SubscrDAO dao) {
		super("customeradd.ftl");
	}


	public String kunde(Subscription s) {
		return s.getSubscriber().getName();
	}

	public String product(Subscription s) {
		return s.getProduct().getName();
	}


}
