package net.buchlese.verw.ctrl;


import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.verw.core.AccountingExportFile;
import net.buchlese.verw.repos.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;


@RestController
@RequestMapping(path="customers")
public class CustomersController {

	@Autowired CustomerRepository customerRepository;


	@Autowired AccountingExportFile exportFileCreator;

//	@PersistenceContext	EntityManager em;
	
	@ResponseBody
	@RequestMapping(path="customersDyn", method = RequestMethod.GET)
	public Page<Subscriber> invoicesDynamic(@QuerydslPredicate(root = Subscriber.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return customerRepository.findAll(predicate, pageable);
	}


}
