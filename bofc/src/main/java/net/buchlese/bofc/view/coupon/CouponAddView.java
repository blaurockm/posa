package net.buchlese.bofc.view.coupon;

import net.buchlese.bofc.jdbi.bofc.CouponDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class CouponAddView extends AbstractBofcView{

	private final CouponDAO dao;
	
	public CouponAddView(CouponDAO dao) {
		super("couponadd.ftl");
		this.dao = dao;
	}


}
