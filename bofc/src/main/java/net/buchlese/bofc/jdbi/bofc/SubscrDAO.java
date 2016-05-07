package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;

import org.joda.time.LocalDate;

public interface SubscrDAO {
	Subscriber getSubscriber(final long id);

	SubscrProduct getSubscrProduct(final long id);

	List<SubscrProduct> getSubscrProducts();

	List<Subscription> getSubscriptions();

	List<SubscrArticle> getArticlesOfProduct(final long prodid);

	SubscrArticle getNewestArticleOfProduct(final long prodid);

	List<SubscrDelivery> getDeliveries(LocalDate now);

	List<SubscrDelivery> getDeliveriesForSubscriptionUnrecorded(long id);

	List<SubscrDelivery> getDeliveriesForSubscriptionRecorded(long id);

	List<SubscrDelivery> getDeliveriesForSubscription(long id);

	List<Subscription> getSubscriptionsForProduct(long productId);

	List<Subscription> getSubscriptionsForSubscriber(long id);
	
	SubscrArticle getSubscrArticle(long id);

	SubscrDelivery getSubscrDelivery(long delId);

	Subscription getSubscription(long subId);

	SubscrDelivery createDelivery(Subscription subscription, SubscrArticle subscrArticle, LocalDate d);

	void recordDetailsOnvInvoice(List<SubscrDelivery> deliveries, String invNumber);

	List<Subscriber> getSubscribers();


}
