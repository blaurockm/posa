package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscrProductsView extends AbstractBofcView {

	private final List<SubscrProduct> products;
	
	public SubscrProductsView(SubscrDAO dao, List<SubscrProduct> products) {
		super("subscrproducts.ftl");
		this.products = products;
//		this.products.forEach(p -> p.getSubscriptionCount());
	}


	public List<SubscrProduct> getProducts() {
		return products;
	}

}
