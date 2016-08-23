package net.buchlese.verw.ctrl;


import net.buchlese.bofc.api.subscr.QSubscrProduct;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.verw.repos.SubscrProductRepository;

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
import com.querydsl.core.types.dsl.BooleanExpression;


@RestController
@RequestMapping(path="subscrproduct")
public class SubscrProductController {

	@Autowired SubscrProductRepository productRepository;
	
	@ResponseBody
	@RequestMapping(path="subscrproductsDyn", method = RequestMethod.GET)
	public Page<Subscriber> productsDynamic(@QuerydslPredicate(root = SubscrProduct.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {
		BooleanExpression a = QSubscrProduct.subscrProduct.payPerDelivery.isFalse();
		return productRepository.findAll(a.and(predicate), pageable);
	}

	@ResponseBody
	@RequestMapping(path="continuationsDyn", method = RequestMethod.GET)
	public Page<Subscriber> continuationsDynamic(@QuerydslPredicate(root = SubscrProduct.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		BooleanExpression a = QSubscrProduct.subscrProduct.payPerDelivery.isTrue();
		return productRepository.findAll(a.and(predicate), pageable);
	}


}
