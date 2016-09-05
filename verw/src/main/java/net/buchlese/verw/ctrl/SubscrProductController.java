package net.buchlese.verw.ctrl;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import net.buchlese.bofc.api.subscr.QSubscrProduct;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.SubscrProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;


@RestController
@RequestMapping(path="subscrproducts")
public class SubscrProductController {

	@Autowired SubscrProductRepository productRepository;
	
	@PersistenceContext	EntityManager em;

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

	@ResponseBody
	@RequestMapping(path="createinterval", method = RequestMethod.POST)
	@Transactional
	public void createSubscrInterval(@RequestBody SubscrInterval intvl) {
		SubscrProduct p = em.find(SubscrProduct.class, intvl.getProductId());
		intvl.setProduct(p);
		em.persist(intvl);
	}

	@ResponseBody
	@RequestMapping(path="createintervaldelivery", method = RequestMethod.POST)
	@Transactional
	public void createSubscrIntervalDelivery(@RequestBody SubscrIntervalDelivery intvldeliv) {
		Subscription s = em.find(Subscription.class, intvldeliv.getSubscriptionId());
		Subscriber sub = em.find(Subscriber.class, intvldeliv.getSubscriberId());
		SubscrInterval intvl = em.find(SubscrInterval.class, intvldeliv.getIntervalId());
		intvldeliv.setSubscription(s);
		intvldeliv.setSubscriber(sub);
		intvldeliv.setInterval(intvl);
		em.persist(intvldeliv);
	}

	@ResponseBody
	@RequestMapping(path="createarticle", method = RequestMethod.POST)
	@Transactional
	public void createSubscrArticle(@RequestBody SubscrArticle intvl) {
		SubscrProduct p = em.find(SubscrProduct.class, intvl.getProductId());
		intvl.setProduct(p);
		em.persist(intvl);
	}

	@ResponseBody
	@RequestMapping(path="createarticledelivery", method = RequestMethod.POST)
	@Transactional
	public void createSubscrArticleDelivery(@RequestBody SubscrDelivery intvldeliv) {
		Subscription s = em.find(Subscription.class, intvldeliv.getSubscriptionId());
		Subscriber sub = em.find(Subscriber.class, intvldeliv.getSubscriberId());
		SubscrArticle intvl = em.find(SubscrArticle.class, intvldeliv.getArticleId());
		intvldeliv.setSubscription(s);
		intvldeliv.setSubscriber(sub);
		intvldeliv.setArticle(intvl);
		em.persist(intvldeliv);
	}


	
}
