package net.buchlese.bofc.view.coupon;

import java.util.List;

import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.CouponDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class CouponCustomerDetailView extends AbstractBofcView{

	private final Subscriber sub;
	private final CouponDAO dao;
	private final PosInvoiceDAO invDao;

	public CouponCustomerDetailView(CouponDAO dao, PosInvoiceDAO invd, Subscriber s ) {
		super("couponcustomerdetail.ftl");
		this.dao = dao;
		this.sub = s;
		this.invDao = invd;
	}


	public List<Coupon> getCoupons() {
		return dao.fetchAllCouponsForCustomer(sub.getId());
	}

	public Subscriber getSub() {
		return sub;
	}

	


}
