package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import net.buchlese.bofc.api.coupon.Coupon;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface CouponDAO {

	@RegisterMapper(CouponMapper.class)
	@SqlQuery("select * from coupon ")
	List<Coupon> fetchCoupons();
	
	@RegisterMapper(CouponMapper.class)
	@SqlQuery("select * from coupon where id = :id")
	Coupon fetchCoupon(@Bind("id") long id);

	@RegisterMapper(CouponMapper.class)
	@SqlQuery("select * from coupon where customerId = :cid")
	List<Coupon> fetchAllCouponsForCustomer(@Bind("cid") long customer);

	@RegisterMapper(CouponMapper.class)
	@SqlQuery("select * from coupon where customerId = :cid and payed = :pay")
	List<Coupon> fetchAllCouponsForCustomer(@Bind("cid") long customer, @Bind("pay") boolean payed);

	@SqlBatch("update coupon set invoiceNumber = :invNum, payed = true where id = :id")
	void recordCouponsOnInvoice(@Bind("id") List<Long> couponIds, @Bind("invNum") String invNumber);

	@SqlBatch("update coupon set invoiceNumber = null, payed = false where id = :id")
	void resetCouponsOfInvoice(@Bind("id") List<Long> couponIds);

	@SqlUpdate("update coupon set (complJson, customerId, payed) " +
		    " = (:complJson, :customerId, :payed) where id = :id")
	void updateCoupon(@BindBean Coupon art);

	@GetGeneratedKeys
	@SqlUpdate("insert into coupon (complJson, customerId, payed) " +
		    " values (:complJson, :customerId, :payed)")
	long insertCoupon(@BindBean Coupon art);

}
