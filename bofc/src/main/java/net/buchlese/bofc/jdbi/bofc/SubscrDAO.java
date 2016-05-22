package net.buchlese.bofc.jdbi.bofc;

import java.util.Collection;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;

import org.joda.time.LocalDate;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface SubscrDAO {

	@SqlUpdate("delete from subscrDelivery where id = :id")
	void deleteDelivery(@Bind("id") long delId);

	@SqlUpdate("delete from tempInvoice where num = :invNumber ")
	void deleteTempInvoice(@Bind("invNumber") String invNumber);

	@RegisterMapper(SubscrArticleMapper.class)
	@SqlQuery("select * from subscrArticle where productId = :prodid")
	List<SubscrArticle> getArticlesOfProduct(@Bind("prodid") long prodid);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where deliveryDate = :dd")
	List<SubscrDelivery> getDeliveries(@Bind("dd") LocalDate now);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid")
	List<SubscrDelivery> getDeliveriesForSubscription(@Bind("subid") long id);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and deliveryDate between :from and :till")
	List<SubscrDelivery> getDeliveriesForSubscription(@Bind("subid") long id, @Bind("from") LocalDate from,	@Bind("till")LocalDate till);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and payed = true")
	List<SubscrDelivery> getDeliveriesForSubscriptionRecorded(@Bind("subid") long id);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and payed = false")
	List<SubscrDelivery> getDeliveriesForSubscriptionUnrecorded(@Bind("subid") long id);

	@SqlQuery("select invoiceNumber from subscrDelivery where subscriptionId = :subid and invoiceNumber is not null")
	Collection<String> getInvoiceNumsForSubscription(@Bind("subid") long id);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where deliveryDate = (select max(deliveryDate) from subscrDelivery where subscriptionId = :subid)")
	SubscrDelivery getLastDeliveryForSubscription(@Bind("subid") long id);

	@RegisterMapper(SubscrArticleMapper.class)
	@SqlQuery("select * from subscrArticle where id = (select max(id) from subscrArticle where productId = :subid)")
	SubscrArticle getNewestArticleOfProduct(@Bind("subid") long prodid);
	
	@RegisterMapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct where nextDelivery is null or nextDelivery between :from and :till ")
	List<SubscrProduct> getProductsForTimespan(@Bind("from") LocalDate from,	@Bind("till")LocalDate till);

	@RegisterMapper(SubscrArticleMapper.class)
	@SqlQuery("select * from subscrArticle where id = :id")
	SubscrArticle getSubscrArticle(@Bind("id") long id);

	@RegisterMapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where id = :id")
	SubscrDelivery getSubscrDelivery(@Bind("id") long delId);

	@RegisterMapper(SubscriberMapper.class)
	@SqlQuery("select * from subscriber where id = :id")
	Subscriber getSubscriber(@Bind("id") long id);

	@RegisterMapper(SubscriberMapper.class)
	@SqlQuery("select * from subscriber")
	List<Subscriber> getSubscribers();

	@RegisterMapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where id = :id")
	Subscription getSubscription(@Bind("id") long subId);

	@RegisterMapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription ")
	List<Subscription> getSubscriptions();

	@RegisterMapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where productId = :id ")
	List<Subscription> getSubscriptionsForProduct(@Bind("id") long productId);

	@RegisterMapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where subscriberId = :id ")
	List<Subscription> getSubscriptionsForSubscriber(@Bind("id") long id);

	@RegisterMapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where payedUntil is null or payedUntil < :till")
	List<Subscription> getSubscriptionsForTimespan(@Bind("from") LocalDate from, @Bind("till")LocalDate till);

	@RegisterMapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct where id = :id")
	SubscrProduct getSubscrProduct(@Bind("id") long id);

	@RegisterMapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct")
	List<SubscrProduct> getSubscrProducts();

	@RegisterMapper(PosInvoiceMapper.class)
	@SqlQuery("select * from tempInvoices ")
	List<PosInvoice> getTempInvoices();

	@RegisterMapper(PosInvoiceMapper.class)
	@SqlQuery("select * from tempInvoices where num = :invNum ")
	PosInvoice getTempInvoice(@Bind("invNum") String invNUm);

	@SqlUpdate("insert into subscrArticle (complJson, productId, erschTag) " +
		    " values (:complJson, :productId, :erschTag)")
	void insertArticle(@BindBean SubscrArticle art);

	@SqlUpdate("insert into subscrDelivery (complJson, subscriptionId, articleId, subscriberId, deliveryDate, payed, invoiceNumber) " +
		    " values (:complJson, :subscriptionId, :articleId, :subscriberId, :deliveryDate, :payed, :invoiceNumber)")
	void insertDelivery(@BindBean SubscrDelivery d);

	@SqlUpdate("insert into subscriber (complJson, pointId, customerId, name1, name2) " +
		    " values (:complJson, :pointid, :customerId, :name1, :name2)")
	void insertSubscriber(@BindBean Subscriber p);

	@SqlUpdate("insert into subscription (complJson, subscriberId, productId, startDate, endDate, payedUntil, pointId) " +
		    " values (:complJson, :subscriberId, :productId, :startDate, :endDate, :payedUntil, :pointid)")
	void insertSubscription(@BindBean Subscription p);

	@SqlUpdate("insert into subscrProduct (complJson, startDate, endDate, nextDelivery) " +
		    " values (:complJson, :startDate, :endDate, :nextDelivery)")
	void insertSubscrProduct(@BindBean SubscrProduct p);
	
	@SqlUpdate("insert into tempInvoices (complJson, num) " +
		    " values (:complJson, :number)")
	void insertTempInvoice(@BindBean PosInvoice ti); // should be moved to PosInvoiceDAO

	@RegisterMapper(SubscriberMapper.class)
	@SqlQuery("select * from subscriber where name1 like :q or name2 like :q or to_char(customerId) like :q ")
	List<Subscriber> querySubscribers(@Bind("q") String query);

	@RegisterMapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct where name like :q or to_char(id) like :q ")
	List<SubscrProduct> querySubscrProducts(@Bind("q") String query);

	@SqlBatch("update subscrDelivery set invoiceNumber = :invNum, payed = true where id = :id")
	void recordDetailsOnInvoice(@Bind("id") List<Long> deliveryIds, @Bind("invNum") String invNumber);

	@SqlBatch("update subscrDelivery set invoiceNumber = null, payed = false where id = :id")
	void resetDetailsOfInvoice(@Bind("id") List<Long> deliveryIds);

	@SqlUpdate("update subscrArticle set (complJson, productId, erschTag) " +
		    " = (:complJson, :productId, :erschTag) where id = :id")
	void updateArticle(@BindBean SubscrArticle art);

	@SqlUpdate("update subscrDelivery set (complJson, subscriptionId, articleId, subscriberId, deliveryDate, payed, invoiceNumber) " +
		    " = (:complJson, :subscriptionId, :articleId, :subscriberId, :deliveryDate, :payed, :invoiceNumber) where id = :id")
	void updateDelivery(@BindBean SubscrDelivery art);

	@SqlUpdate("update subscriber set (complJson, pointId, customerId, name1, name2) " +
		    " = (:complJson, :pointid, :customerId, :name1, :name2) where id = :id")
	void updateSubscriber(@BindBean Subscriber art);

	@SqlUpdate("update subscription set (complJson, subscriberId, productId, startDate, endDate, payedUntil, pointId) " +
		    " = (:complJson, :subscriberId, :productId, :startDate, :endDate, :payedUntil, :pointid) where id = :id")
	void updateSubscription(@BindBean Subscription art);

	@SqlUpdate("update subscrProduct set (complJson, startDate, endDate, nextDelivery) " +
		    " = (:complJson, :startDate, :endDate, :nextDelivery) where id = :id")
	void updateSubscrProduct(@BindBean SubscrProduct p);

}
