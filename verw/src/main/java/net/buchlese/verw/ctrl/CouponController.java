package net.buchlese.verw.ctrl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.verw.repos.CouponRepository;

@RestController
@RequestMapping("/coupons")
public class CouponController {
	@Autowired
	private CouponRepository repo;

	@RequestMapping(method = RequestMethod.GET)
	public List<?> findCoupons(@RequestParam(name="pupil") Optional<String> pupil) {
		if (pupil.isPresent()) {
			return repo.findByPupilsname(pupil.get());
		}
		return repo.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Coupon addCoupon(@RequestBody Coupon item) {
		item.setId(null);
		return repo.saveAndFlush(item);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Coupon updateCoupon(@RequestBody Coupon updatedItem, @PathVariable Long id) {
		updatedItem.setId(id);
		return repo.saveAndFlush(updatedItem);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteCoupon(@PathVariable Long id) {
		repo.delete(id);
	}
}
