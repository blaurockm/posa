package net.buchlese.verw.ctrl;


import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import net.buchlese.bofc.api.subscr.QSubscrProduct;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.SubscrArticleRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalRepository;
import net.buchlese.verw.repos.SubscrProductRepository;
import net.buchlese.verw.repos.SubscriptionRepository;


@RestController
@RequestMapping(path="subscrproducts")
public class SubscrProductController {

	@Autowired SubscrProductRepository productRepository;
	
	@Autowired SubscrIntervalRepository subscrIntervalRepository;

	@Autowired SubscrIntervalDeliveryRepository subscrIntervalDeliveryRepository;

	@Autowired CustomerRepository subscriberRepository;

	@Autowired SubscriptionRepository subscriptionRepository;

	@Autowired SubscrArticleRepository subscrArticleRepository;

	@Autowired SubscrDeliveryRepository subscrDeliveryRepository;

	@ResponseBody
	@RequestMapping(path="subscrproductsDyn", method = RequestMethod.GET)
	public Page<SubscrProduct> productsDynamic(@QuerydslPredicate(root = SubscrProduct.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {
		BooleanExpression a = QSubscrProduct.subscrProduct.payPerDelivery.isFalse();
		return productRepository.findAll(a.and(predicate), pageable);
	}

	@ResponseBody
	@RequestMapping(path="continuationsDyn", method = RequestMethod.GET)
	public Page<SubscrProduct> continuationsDynamic(@QuerydslPredicate(root = SubscrProduct.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		BooleanExpression a = QSubscrProduct.subscrProduct.payPerDelivery.isTrue();
		return productRepository.findAll(a.and(predicate), pageable);
	}

	@ResponseBody
	@RequestMapping(path="createinterval", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<SubscrInterval> createSubscrInterval(@RequestBody SubscrInterval intvl) {
		SubscrProduct p = productRepository.findOne(intvl.getProductId());
		if (p.isPayPerDelivery()) {
			return ResponseEntity.unprocessableEntity().body(intvl);  
		}
		intvl.setProduct(p);
		intvl.setIntervalType(p.getIntervalType());
		intvl.setStartDate(p.getLastInterval().plusDays(1));
		intvl.updateEndDate();
		intvl.setName(intvl.initializeName(p.getIntervalPattern() != null ? p.getIntervalPattern() : p.getNamePattern()));
		p.setLastInterval(intvl.getEndDate());
		productRepository.save(p);
		subscrIntervalRepository.saveAndFlush(intvl);
		return new ResponseEntity<SubscrInterval>(intvl, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path="createintervaldelivery", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<SubscrIntervalDelivery> createSubscrIntervalDelivery(@RequestBody SubscrIntervalDelivery intvldeliv) {
		Subscription s = subscriptionRepository.findOne(intvldeliv.getSubscriptionId());
		Subscriber sub = subscriberRepository.findOne(intvldeliv.getSubscriberId());
		SubscrInterval intvl = subscrIntervalRepository.findOne(intvldeliv.getIntervalId());
		intvldeliv.setSubscription(s);
		intvldeliv.setSubscriber(sub);
		intvldeliv.setInterval(intvl);
		intvldeliv.setDeliveryDate(LocalDate.now());
		intvldeliv.setCreationDate(LocalDateTime.now());
		intvldeliv.setQuantity(s.getQuantity());
		intvldeliv.setIntervalName(intvl.getName());
		intvldeliv.setTotal(intvl.getBrutto());
		intvldeliv.setTotalFull(intvl.getBrutto_full());
		intvldeliv.setTotalHalf(intvl.getBrutto_half());
		intvldeliv.setShipmentCost(0);
		// Jetzt wird es heikel...
		intvldeliv.setPayed(false);
		subscrIntervalDeliveryRepository.saveAndFlush(intvldeliv);
		return new ResponseEntity<SubscrIntervalDelivery>(intvldeliv, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path="createarticle", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<SubscrArticle> createSubscrArticle(@RequestBody SubscrArticle art) {
		SubscrProduct p = productRepository.findOne(art.getProductId());
		art.setProduct(p);
		p.setCount(p.getCount()+1);
		art.setIssueNo(p.getCount());
		art.setName(art.initializeName(p.getNamePattern()));
		productRepository.save(p);
		subscrArticleRepository.saveAndFlush(art);
		return new ResponseEntity<SubscrArticle>(art, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path="createarticledelivery", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<SubscrDelivery> createSubscrArticleDelivery(@RequestBody SubscrDelivery deliv) {
		Subscription s = subscriptionRepository.findOne(deliv.getSubscriptionId());
		Subscriber sub = subscriberRepository.findOne(deliv.getSubscriberId());
		SubscrArticle art = subscrArticleRepository.findOne(deliv.getArticleId());
		deliv.setSubscription(s);
		deliv.setSubscriber(sub);
		deliv.setArticle(art);
		deliv.setDeliveryDate(LocalDate.now());
		deliv.setCreationDate(LocalDateTime.now());
		deliv.setQuantity(s.getQuantity());
		deliv.setArticleName(art.getName());
		deliv.setTotal(art.getBrutto());
		deliv.setTotalFull(art.getBrutto_full());
		deliv.setTotalHalf(art.getBrutto_half());
		deliv.setShipmentCost(0);
		// Jetzt wird es heikel...
		deliv.setPayed(false);
		if (s.getProduct().isPayPerDelivery() == false) {
			deliv.setPayed(true); // es wird nich pro lieferung bezahlt, also ist diese Lieferung nicht extra zu bezahlen
		}
		deliv.setSlipped(false);
		if (sub.isNeedsDeliveryNote() == false) {
			deliv.setSlipped(true); // der Kunde möchte keine Lieferscheine, d.h. wir tun so als hätten wir schon einen erzeugt.
		}
		
		subscrDeliveryRepository.saveAndFlush(deliv);
		return new ResponseEntity<SubscrDelivery>(deliv, HttpStatus.OK);
	}


	
}
