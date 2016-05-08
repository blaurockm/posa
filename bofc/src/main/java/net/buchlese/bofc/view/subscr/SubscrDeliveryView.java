package net.buchlese.bofc.view.subscr;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrDeliveryView extends AbstractBofcView{

	private final SubscrDelivery del;
	private final SubscrDAO dao;
	
	public SubscrDeliveryView(SubscrDAO dao, SubscrDelivery d) {
		super("subscrdelivery.ftl");
		this.dao = dao;
		this.del = d;
	}


	public SubscrDelivery getD() {
		return del;
	}

	public String kunde(SubscrDelivery s) {
		if (s.getSubscriberId() > 0) {
			Subscriber x = dao.getSubscriber(s.getSubscriberId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + s.getSubscriberId();
		}
		return "keine subId";
	}

}
