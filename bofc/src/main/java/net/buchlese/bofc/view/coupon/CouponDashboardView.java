package net.buchlese.bofc.view.coupon;

import java.util.List;

import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.CouponDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class CouponDashboardView extends AbstractBofcView {

	private final List<Coupon> coupons;
	private final CouponDAO dao;
	private final SubscrDAO sdao;
	
	public CouponDashboardView(CouponDAO dao, SubscrDAO sdao, LocalDate d) {
		super("coupondashboard.ftl");
		this.sdao = sdao;
		this.dao = dao;
		coupons = dao.fetchCoupons();
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public String kunde(Coupon d) {
		if (d.getCustomerId() > 0) {
			Subscriber x = sdao.getSubscriber(d.getCustomerId());
			if (x != null) {
				return x.getName();
			}
			return "not found " + d.getCustomerId();
		}
		return "keine subId";
	}

}
