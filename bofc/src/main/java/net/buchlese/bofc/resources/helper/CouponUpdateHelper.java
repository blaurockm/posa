package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.bofc.jdbi.bofc.CouponDAO;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class CouponUpdateHelper {

	private final CouponDAO dao;

	public CouponUpdateHelper(CouponDAO dao) {
		super();
		this.dao = dao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		Coupon art = dao.fetchCoupon(id);
		if ("pupilsname".equals(field)) {
			art.setPupilsname(value);
			res.success = true;
		}
		if ("reason".equals(field)) {
			art.setReason(value);
			res.success = true;
		}
		if ("pupilyear".equals(field)) {
			art.setPupilyear(value);
			res.success = true;
		}
		if ("pupilclass".equals(field)) {
			art.setPupilclass(value);
			res.success = true;
		}
		if ("acceptDate".equals(field)) {
			art.setAcceptDate(LocalDate.parse(value, DateTimeFormat.forPattern("dd.MM.yyyy") ));
			res.success = true;
		}
		if ("payed".equals(field)) {
			art.setPayed(Boolean.valueOf(value));
			res.success = true;
		}
		if ("amount".equals(field)) {
			art.setAmount(Long.parseLong(value));
			res.success = true;
		}
		if (res.success) {
			dao.updateCoupon(art);
		}
		return res;
	}
	
	
}
