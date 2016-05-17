package net.buchlese.bofc.jdbi.bofc;

import java.util.Collection;
import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;

import org.joda.time.LocalDate;

import com.google.common.base.Optional;

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

	void recordDetailsOnvInvoice(List<Long> deliveryIds, String invNumber);

	List<Subscriber> getSubscribers();

	SubscrDelivery getLastDeliveryForSubscription(long id);

	Collection<String> getInvoiceNumsForSubscription(long id);

	void insertSubscrProduct(SubscrProduct p);

	void insertSubscription(Subscription p);

	void insertSubscriber(Subscriber p);

	void insertArticle(SubscrArticle art);

	List<SubscrProduct> querySubscrProducts(Optional<String> query);

	List<Subscriber> querySubscribers(Optional<String> query);

	List<SubscrDelivery> getDeliveriesForSubscription(long id, LocalDate from,	LocalDate till);

	List<SubscrProduct> getProductsForTheNextWeek(LocalDate d);

	List<Subscription> getSubscriptionsForThisMonth(LocalDate d);

	void deleteDelivery(long delId);

	void updateSubscrProduct(SubscrProduct p);

	void updateDelivery(SubscrDelivery art);

	void updateArticle(SubscrArticle art);

	void updateSubscriber(Subscriber art);

	void updateSubscription(Subscription art);



}
