package net.buchlese.verw.ctrl;

import java.util.List;
import java.util.Optional;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.verw.repos.BalanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashbalance")
public class BalanceController {
	@Autowired
	private BalanceRepository repo;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public PosCashBalance findBalance(@PathVariable Long id) {
		return repo.findOne(id);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<?> findAllBalances(@RequestParam(name="pupil") Optional<String> pupil) {
		return repo.findAll();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public PosCashBalance addPosCashBalance(@RequestBody PosCashBalance item) {
		item.setId(null);
		return repo.saveAndFlush(item);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public PosCashBalance updatePosCashBalance(@RequestBody PosCashBalance updatedItem, @PathVariable Long id) {
		updatedItem.setId(id);
		return repo.saveAndFlush(updatedItem);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteCoupon(@PathVariable Long id) {
		repo.delete(id);
	}
}
