package net.buchlese.bofc.jdbi.bofc;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.UserChange;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;

import org.joda.time.LocalDate;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface SubscrDAO {

	@SqlUpdate("delete from subscrDelivery where id = :id")
	void deleteDelivery(@Bind("id") long delId);

	@SqlUpdate("delete from subscrIntervalDelivery where id = :id")
	void deleteIntervalDelivery(@Bind("id") long delId);

	@SqlUpdate("delete from tempInvoices where num = :invNumber ")
	void deleteTempInvoice(@Bind("invNumber") String invNumber);

	@Mapper(SubscrArticleMapper.class)
	@SqlQuery("select * from subscrArticle where productId = :prodid order by id asc")
	List<SubscrArticle> getArticlesOfProduct(@Bind("prodid") long prodid);

	@Mapper(SubscrIntervalMapper.class)
	@SqlQuery("select * from subscrInterval where productId = :prodid order by id asc")
	List<SubscrInterval> getIntervalsOfProduct(@Bind("prodid") long prodid);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where deliveryDate = :dd")
	List<SubscrDelivery> getDeliveries(@Bind("dd") LocalDate now);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid")
	List<SubscrDelivery> getDeliveriesForSubscription(@Bind("subid") long id);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where subscriptionId = :subid")
	List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(@Bind("subid") long id);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and deliveryDate between :from and :till")
	List<SubscrDelivery> getDeliveriesForSubscription(@Bind("subid") long id, @Bind("from") LocalDate from,	@Bind("till")LocalDate till);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where subscriptionId = :subid and startDate between :from and :till")
	List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(@Bind("subid") long id, @Bind("from") LocalDate from,	@Bind("till")LocalDate till);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where subscriptionId = :subid and payed = true")
	List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionRecorded(@Bind("subid") long id);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where subscriptionId = :subid and payed = false")
	List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionUnrecorded(@Bind("subid") long id);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where payed = false")
	List<SubscrIntervalDelivery> getIntervalDeliveriesUnrecorded();

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and payed = true")
	List<SubscrDelivery> getDeliveriesForSubscriptionRecorded(@Bind("subid") long id);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and payed = false")
	List<SubscrDelivery> getDeliveriesForSubscriptionUnrecorded(@Bind("subid") long id);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where payed = false")
	List<SubscrDelivery> getDeliveriesUnrecorded();

	@SqlQuery("select invoiceNumber from subscrDelivery where subscriptionId = :subid and invoiceNumber is not null")
	Collection<String> getInvoiceNumsForSubscription(@Bind("subid") long id);

	@SqlQuery("select invoiceNumber from subscrIntervalDelivery where subscriptionId = :subid and invoiceNumber is not null")
	Collection<String> getInvoiceNumsForSubscriptionIntervals(@Bind("subid") long id);

	@Mapper(PosInvoiceMapper.class)
	@SqlQuery("select * from posInvoice where pointid = :pointid and invDate > :date order by number asc")
	List<PosInvoice> getSubscrInvoices(@Bind("pointid") int id, @Bind("date") LocalDate  num);

	@Mapper(PosInvoiceMapper.class)
	@SqlQuery("select * from posInvoice where debitor = :debitorId order by number desc")
	List<PosInvoice> getSubscriberInvoices(@Bind("debitorId") int debId);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and deliveryDate = (select max(deliveryDate) from subscrDelivery where subscriptionId = :subid)")
	SubscrDelivery getLastDeliveryForSubscription(@Bind("subid") long id);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where id = (select max(id) from subscrIntervalDelivery where subscriptionId = :subid)")
	SubscrIntervalDelivery getLastIntervalDeliveryForSubscription(@Bind("subid") long id);

	@Mapper(SubscrArticleMapper.class)
	@SqlQuery("select * from subscrArticle where id = (select max(id) from subscrArticle where productId = :subid)")
	SubscrArticle getNewestArticleOfProduct(@Bind("subid") long prodid);

	@Mapper(SubscrIntervalMapper.class)
	@SqlQuery("select * from subscrInterval where id = (select max(id) from subscrInterval where productId = :subid)")
	SubscrInterval getNewestIntervalOfProduct(@Bind("subid") long prodid);

	@Mapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct where nextDelivery between :from and :till order by name")
	List<SubscrProduct> getProductsForTimespan(@Bind("from") LocalDate from,	@Bind("till")LocalDate till);

	@Mapper(SubscrArticleMapper.class)
	@SqlQuery("select * from subscrArticle where id = :id")
	SubscrArticle getSubscrArticle(@Bind("id") long id);

	@Mapper(SubscrIntervalMapper.class)
	@SqlQuery("select * from subscrInterval where id = :id")
	SubscrInterval getSubscrInterval(@Bind("id") long id);

	@Mapper(SubscrDeliveryMapper.class)
	@SqlQuery("select * from subscrDelivery where id = :id")
	SubscrDelivery getSubscrDelivery(@Bind("id") long delId);

	@Mapper(SubscrIntervalDeliveryMapper.class)
	@SqlQuery("select * from subscrIntervalDelivery where id = :id")
	SubscrIntervalDelivery getSubscrIntervalDelivery(@Bind("id") long delId);

	@Mapper(SubscriberMapper.class)
	@SqlQuery("select * from subscriber where id = :id")
	Subscriber getSubscriber(@Bind("id") long id);

	@Mapper(SubscriberMapper.class)
	@SqlQuery("select * from subscriber order by name1")
	List<Subscriber> getSubscribers();

	@Mapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where id = :id")
	Subscription getSubscription(@Bind("id") long subId);

	@Mapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription ")
	List<Subscription> getSubscriptions();

	@Mapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where complJson like '%memo%'")
	List<Subscription> getSubscriptionsWithMemo();

	@Mapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where productId = :id ")
	List<Subscription> getSubscriptionsForProduct(@Bind("id") long productId);

	@Mapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where subscriberId = :id ")
	List<Subscription> getSubscriptionsForSubscriber(@Bind("id") long id);

	@Mapper(SubscriptionMapper.class)
	@SqlQuery("select * from subscription where payedUntil is null or payedUntil < :till")
	List<Subscription> getSubscriptionsForTimespan(@Bind("from") LocalDate from, @Bind("till")LocalDate till);

	@Mapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct where id = :id")
	SubscrProduct getSubscrProduct(@Bind("id") long id);

	@Mapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct order by name")
	List<SubscrProduct> getSubscrProducts();

	@Mapper(TempInvoiceMapper.class)
	@SqlQuery("select * from tempInvoices ")
	List<PosInvoice> getTempInvoices();

	@Mapper(TempInvoiceMapper.class)
	@SqlQuery("select * from tempInvoices where num = :invNum ")
	PosInvoice getTempInvoice(@Bind("invNum") String invNUm);

	@GetGeneratedKeys
	@SqlUpdate("insert into subscrArticle (complJson, productId, erschTag) " +
		    " values (:complJson, :productId, :erschTag)")
	long insertArticle(@BindBean SubscrArticle art);

	@SqlUpdate("insert into subscrDelivery (complJson, subscriptionId, articleId, subscriberId, deliveryDate, payed, invoiceNumber) " +
		    " values (:complJson, :subscriptionId, :articleId, :subscriberId, :deliveryDate, :payed, :invoiceNumber)")
	void insertDelivery(@BindBean SubscrDelivery d);

	@GetGeneratedKeys
	@SqlUpdate("insert into subscrInterval (complJson, productId, startDate, endDate) " +
		    " values (:complJson, :productId, :startDate, :endDate)")
	long insertInterval(@BindBean SubscrInterval art);

	@SqlUpdate("insert into subscrIntervalDelivery (complJson, subscriptionId, intervalId, subscriberId, payed, invoiceNumber) " +
		    " values (:complJson, :subscriptionId, :intervalId, :subscriberId, :payed, :invoiceNumber)")
	void insertIntervalDelivery(@BindBean SubscrIntervalDelivery d);

	@GetGeneratedKeys
	@SqlUpdate("insert into subscriber (complJson, pointId, customerId, name1, name2) " +
		    " values (:complJson, :pointid, :customerId, :name1, :name2)")
	long insertSubscriber(@BindBean Subscriber p);

	@GetGeneratedKeys
	@SqlUpdate("insert into subscription (complJson, subscriberId, productId, startDate, endDate, payedUntil, pointId) " +
		    " values (:complJson, :subscriberId, :productId, :startDate, :endDate, :payedUntil, :pointid)")
	long insertSubscription(@BindBean Subscription p);

	@GetGeneratedKeys
	@SqlUpdate("insert into subscrProduct (complJson, startDate, endDate, nextDelivery, name) " +
		    " values (:complJson, :startDate, :endDate, :nextDelivery, :name)")
	long insertSubscrProduct(@BindBean SubscrProduct p);
	
	@SqlUpdate("insert into tempInvoices (complJson, num) " +
		    " values (:complJson, :number)")
	void insertTempInvoice(@BindBean PosInvoice ti); // should be moved to PosInvoiceDAO

	@Mapper(SubscriberMapper.class)
	@SqlQuery("select * from subscriber where name1 like :q or name2 like :q or to_char(customerId) like :q order by name1")
	List<Subscriber> querySubscribers(@Bind("q") String query);

	@Mapper(SubscrProductMapper.class)
	@SqlQuery("select * from subscrProduct where name like :q or to_char(id) like :q order by name")
	List<SubscrProduct> querySubscrProducts(@Bind("q") String query);

	@SqlBatch("update subscrDelivery set invoiceNumber = :invNum, payed = true where id = :id")
	void recordDetailsOnInvoice(@Bind("id") List<Long> deliveryIds, @Bind("invNum") String invNumber);

	@SqlBatch("update subscrDelivery set invoiceNumber = null, payed = false where id = :id")
	void resetDetailsOfInvoice(@Bind("id") List<Long> deliveryIds);

	@SqlBatch("update subscrIntervalDelivery set invoiceNumber = :invNum, payed = true where id = :id")
	void recordIntervalDetailsOnInvoice(@Bind("id") List<Long> deliveryIds, @Bind("invNum") String invNumber);

	@SqlBatch("update subscrIntervalDelivery set invoiceNumber = null, payed = false where id = :id")
	void resetIntervalDetailsOfInvoice(@Bind("id") List<Long> deliveryIds);

	@SqlUpdate("update subscrArticle set (complJson, productId, erschTag) " +
		    " = (:complJson, :productId, :erschTag) where id = :id")
	void updateArticle(@BindBean SubscrArticle art);

	@SqlUpdate("update subscrDelivery set (complJson, subscriptionId, articleId, subscriberId, deliveryDate, payed, invoiceNumber) " +
		    " = (:complJson, :subscriptionId, :articleId, :subscriberId, :deliveryDate, :payed, :invoiceNumber) where id = :id")
	void updateDelivery(@BindBean SubscrDelivery art);

	@SqlUpdate("update subscrInterval set (complJson, productId, startDate, endDate) " +
		    " = (:complJson, :productId, :startDate, :endDate) where id = :id")
	void updateInterval(@BindBean SubscrInterval art);

	@SqlUpdate("update subscrIntervalDelivery set (complJson, subscriptionId, intervalId, subscriberId, payed, invoiceNumber) " +
		    " = (:complJson, :subscriptionId, :intervalId, :subscriberId, :payed, :invoiceNumber) where id = :id")
	void updateIntervalDelivery(@BindBean SubscrIntervalDelivery art);

	@SqlUpdate("update subscriber set (complJson, pointId, customerId, name1, name2) " +
		    " = (:complJson, :pointid, :customerId, :name1, :name2) where id = :id")
	void updateSubscriber(@BindBean Subscriber art);

	@SqlUpdate("update subscription set (complJson, subscriberId, productId, startDate, endDate, payedUntil, pointId) " +
		    " = (:complJson, :subscriberId, :productId, :startDate, :endDate, :payedUntil, :pointid) where id = :id")
	void updateSubscription(@BindBean Subscription art);

	@SqlUpdate("update subscrProduct set (complJson, startDate, endDate, nextDelivery, name) " +
		    " = (:complJson, :startDate, :endDate, :nextDelivery, :name) where id = :id")
	void updateSubscrProduct(@BindBean SubscrProduct p);
	
	// sollte in jedes DAO
	@SqlUpdate("insert into userChanges (objectid, login, fieldId, oldValue, newValue, action, modDate) "
			+ " values (:objectId, :login, :fieldId, :oldValue, :newValue, :action, :modDate) ")
	void insert(@Valid @BindBean UserChange u);

	@Mapper(PosIssueSlipMapper.class)
	@SqlQuery("select * from posissueslip where debitor = :debId and payed = 0 order by number asc")
	List<PosIssueSlip> findIssueSlipsToAdd(@Bind("debId") long id);

	@Mapper(PosIssueSlipMapper.class)
	@SqlQuery("select * from posissueslip where id = :num")
	PosIssueSlip getIssueSlip(@Bind("num") long id);

	@SqlUpdate("update posissueslip set (customer, debitor, invDate, complJson, name1, name2, name3,  street, city, payed) " +
	" = (:customerId, :debitorId, :date, :complJson,  :name1, :name2, :name3, :street, :city, :payed )  where id = :id")
	void updateIssueSlip(@Valid @BindBean PosIssueSlip inv);


}
