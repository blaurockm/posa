package net.buchlese.verw.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.buchlese.bofc.api.coupon.Coupon;

@RepositoryRestResource(collectionResourceRel = "coupon", path = "coupon")
public interface CouponRepository extends JpaRepository<Coupon, Long> {

	List<Coupon> findByPupilsname(String firstName);

    List<Coupon> findByCustomer_Id(int custId);
}
