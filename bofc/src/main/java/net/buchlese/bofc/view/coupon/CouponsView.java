package net.buchlese.bofc.view.coupon;

import java.util.List;

import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.CouponDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class CouponsView extends AbstractBofcView {

	private final List<Coupon> coupons;
	private final CouponDAO dao;
	
	public CouponsView(CouponDAO dao, List<Coupon> products) {
		super("coupons.ftl");
		this.dao = dao;
		this.coupons = products;
	}


	public List<Coupon> getCoupons() {
		return coupons;
	}


}
