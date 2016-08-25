package net.buchlese.verw.ctrl;


import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.SubscriptionRepository;

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
@RequestMapping(path="subscriptions")
public class SubscriptionsController {

	@Autowired SubscriptionRepository subscriptionRepository;


//	@Autowired AccountingExportFile exportFileCreator;

//	@PersistenceContext	EntityManager em;
	
	@ResponseBody
	@RequestMapping(path="subscriptionsDyn", method = RequestMethod.GET)
	public Page<Subscription> subscriptionsDynamic(@QuerydslPredicate(root = Subscription.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return subscriptionRepository.findAll(predicate, pageable);
	}


}
