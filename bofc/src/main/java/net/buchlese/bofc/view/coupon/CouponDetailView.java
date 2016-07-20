package net.buchlese.bofc.view.coupon;

import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.bofc.view.AbstractBofcView;

public class CouponDetailView extends AbstractBofcView{

	private final Coupon p;
	
	public CouponDetailView(Coupon p) {
		super("coupondetail.ftl");
		this.p = p;
	}


	public Coupon getP() {
		return p;
	}


	public List<PosInvoice> invoices() {
		return Collections.emptyList();
	}



}
