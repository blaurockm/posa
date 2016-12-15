package net.buchlese.verw.ctrl;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.core.DeliveryNoteCreator;
import net.buchlese.verw.core.SettlementCreator;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalDeliveryRepository;
import net.buchlese.verw.repos.SubscriptionRepository;


@RestController
@RequestMapping(path="subscriptions")
public class SubscriptionsController {

	@Autowired SubscriptionRepository subscriptionRepository;

	@Autowired SubscrDeliveryRepository deliveryRepository;

	@Autowired SubscrIntervalDeliveryRepository intervalDeliveryRepository;

	@Autowired SettlementCreator settlementCreator;

	@Autowired DeliveryNoteCreator deliveryNoteCreator;

	@ResponseBody
	@RequestMapping(path="subscriptionsDyn", method = RequestMethod.GET)
	public Page<Subscription> subscriptionsDynamic(@QuerydslPredicate(root = Subscription.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return subscriptionRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="deliveriesDyn", method = RequestMethod.GET)
	public Page<SubscrDelivery> deliveriesDynamic(@QuerydslPredicate(root = SubscrDelivery.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return deliveryRepository.findAll(predicate, pageable);
	}

	@RequestMapping(path="settleunsettled", method = RequestMethod.GET)
	@Transactional
	public void settleUnsettled() {
		List<SubscrDelivery> delivs = deliveryRepository.findByPayed(false);
		// zähle, wie oft jeder Abonnent vorkommt
		Map<Subscriber, Long> counted = delivs.stream().collect(Collectors.groupingBy(SubscrDelivery::getSubscriber, Collectors.counting()));
		for (SubscrDelivery deli : delivs) {
			if (deli.isPayed()) {
				continue;
			}
			Subscriber sub = deli.getSubscriber();
			if (sub.isCollectiveInvoice() || counted.get(sub).intValue() > 1) {
				settlementCreator.createCollectiveSettlement(sub);
			} else {
				settlementCreator.createSettlement(deli.getSubscription());
			}
		}
		List<SubscrIntervalDelivery> intvlDelivs = intervalDeliveryRepository.findByPayed(false);
		counted = intvlDelivs.stream().collect(Collectors.groupingBy(SubscrIntervalDelivery::getSubscriber, Collectors.counting()));
		for (SubscrIntervalDelivery deli : intvlDelivs) {
			if (deli.isPayed()) {
				continue;
			}
			Subscriber sub = deli.getSubscriber();
			if (sub.isCollectiveInvoice() || counted.get(sub).intValue() > 1) {
				settlementCreator.createCollectiveSettlement(sub);
			} else {
				settlementCreator.createSettlement(deli.getSubscription());
			}
		}
	}

	
	@RequestMapping(path="noteunnoted", method = RequestMethod.GET)
	@Transactional
	public void noteUnnoted() {
		List<SubscrDelivery> delivs = deliveryRepository.findBySlipped(false);
		for (SubscrDelivery deli : delivs) {
			if (deli.isSlipped()) {
				continue;
			}
			deliveryNoteCreator.createDeliveryNote(deli);
		}
	}

}
