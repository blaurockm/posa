package net.buchlese.verw.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.buchlese.bofc.api.coupon.Coupon;

public interface CouponRepository extends CrudRepository<Coupon, Long> {

    Coupon findByPupilsname(String firstName);

    List<Coupon> findByCustomerId(int custId);
}
