package net.buchlese.bofc.jdbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.UserChange;
import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.ShipType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;

public class SubscrTestDataDAO implements SubscrDAO {

	private List<Subscriber> subscribers;
	private List<SubscrProduct> products;
	private List<Subscription> subscriptions;
	private List<SubscrArticle> articles;
	private List<SubscrDelivery> deliveries;
	private List<PosInvoice> tempInvoices;
	
	private List<UserChange> changes;
	
	private static long idcounter = 10;
	
	public SubscrTestDataDAO() {
		createProducts();
		createSubscribers();
		createSubscriptions();
		createArticles();
		createDeliveries();
		
		createDeliveriesForToday();
		
		tempInvoices = new ArrayList<PosInvoice>();
		changes = new ArrayList<UserChange>();
	}

	@Override 
	public void deleteTempInvoice(String invNumber) {
		tempInvoices = tempInvoices.stream().filter(x -> x.getNumber().equals(invNumber) == false).collect(Collectors.toList());
	}

	@Override 
	public List<PosInvoice> getTempInvoices() {
		return tempInvoices;
	}
	
	@Override
	public void insertTempInvoice(PosInvoice ti) {
		tempInvoices.add(ti);
	}

	@Override
	public PosInvoice getTempInvoice(String invNum) {
		return tempInvoices.stream().filter(x -> x.getNumber().equals(invNum)).findFirst().orElse(null);
	}

	@Override
	public void resetDetailsOfInvoice(List<Long> deliveryId) {
		deliveryId.stream().forEach(i -> { SubscrDelivery x= getSubscrDelivery(i); x.setPayed(false); x.setInvoiceNumber(null); } );
	}


	public Subscriber getSubscriber(final long id) {
		return subscribers.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}

	public SubscrProduct getSubscrProduct(final long id) {
		return products.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}

	public Subscription getSubscription(final long id) {
		return subscriptions.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}

	public List<SubscrProduct> getSubscrProducts() {
		return products;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	public List<Subscription> getSubscriptionsForProduct(long prodid) {
		return subscriptions.stream().filter(x -> x.getProductId() == prodid).collect(Collectors.toList());
	}

	public List<Subscription> getSubscriptionsForSubscriber(long subid) {
		return subscriptions.stream().filter(x -> x.getSubscriberId() == subid).collect(Collectors.toList());
	}

	public SubscrArticle getSubscrArticle(final long id) {
		return articles.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}

	public SubscrDelivery getSubscrDelivery(final long id) {
		return deliveries.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
	}

	public SubscrDelivery getLastDeliveryForSubscription(final long subid) {
		return deliveries.stream().filter(x -> x.getSubscriptionId() == subid).max(Comparator.naturalOrder()).orElse(null);
	}

	public List<SubscrArticle> getArticlesOfProduct(final long prodid) {
		return articles.stream().filter(x -> x.getProductId() == prodid).collect(Collectors.toList());
	}

	public SubscrArticle getNewestArticleOfProduct(final long prodid) {
		return articles.stream().filter(x -> x.getProductId() == prodid).max(Comparator.naturalOrder()).orElse(null);
	}

	public List<SubscrDelivery> getDeliveries(LocalDate now) {
		return deliveries.stream().filter(x -> x.getDeliveryDate().equals(now)).collect(Collectors.toList());
	}

	public List<SubscrDelivery> getDeliveriesForSubscriptionUnrecorded(long subid) {
		return deliveries.stream().filter(x -> x.getSubscriptionId() == subid && x.isPayed() == false).collect(Collectors.toList());
	}

	public List<SubscrDelivery> getDeliveriesForSubscriptionRecorded(long subid) {
		return deliveries.stream().filter(x -> x.getSubscriptionId() == subid && x.isPayed() ).collect(Collectors.toList());
	}

	public List<SubscrDelivery> getDeliveriesForSubscription(long subid) {
		return deliveries.stream().filter(x -> x.getSubscriptionId() == subid).collect(Collectors.toList());
	}

	public Collection<String> getInvoiceNumsForSubscription(long subid) {
		List<SubscrDelivery> delivs = getDeliveriesForSubscription(subid);
		Set<String> invNums = new HashSet<String>();
		for (SubscrDelivery d : delivs) {
			if (d.isPayed() && d.getInvoiceNumber() != null) {
				invNums.add(d.getInvoiceNumber());
			}
		}
		return invNums;
	}
	
	@Override
	public void recordDetailsOnInvoice(List<Long> deliveryId, String invNumber) {
		deliveryId.stream().forEach(i -> { SubscrDelivery x= getSubscrDelivery(i); x.setPayed(true); x.setInvoiceNumber(invNumber); } );
	}
	

	@Override
	public long insertSubscrProduct(SubscrProduct p) {
		p.setId(idcounter++);
		products.add(p);
		return p.getId();
	}

	@Override
	public void insertSubscription(Subscription p) {
		p.setId(idcounter++);
		subscriptions.add(p);
	}

	@Override
	public void insertSubscriber(Subscriber p) {
		p.setId(idcounter++);
		subscribers.add(p);
	}
	
	@Override
	public long insertArticle(SubscrArticle p) {
		p.setId(idcounter++);
		articles.add(p);
		return p.getId();
	}

	@Override
	public void insertDelivery(SubscrDelivery d) {
		deliveries.add(d);
	}

	@Override
	public List<SubscrProduct> querySubscrProducts(String query) {
		return products.stream().filter(x -> x.getName().contains(query)).collect(Collectors.toList());
	}

	@Override
	public List<Subscriber> querySubscribers(String query) {
		return subscribers.stream().filter(x -> x.getName().contains(query) || String.valueOf(x.getCustomerId()).contains(query)).collect(Collectors.toList());
	}

	@Override
	public List<SubscrDelivery> getDeliveriesForSubscription(long subid, LocalDate from, LocalDate till) {
		return deliveries.stream().filter(x -> x.getSubscriptionId() == subid && x.getDeliveryDate().compareTo(from) >= 0 && x.getDeliveryDate().compareTo(till) <= 0).collect(Collectors.toList());
	}

	@Override
	public List<SubscrProduct> getProductsForTimespan(LocalDate d, LocalDate till) {
		return products.stream().filter(x -> x.getLastDelivery() == null || x.getLastDelivery().isBefore(d)).collect(Collectors.toList());
	}

	@Override
	public List<Subscription> getSubscriptionsForTimespan(LocalDate d, LocalDate till) {
		return subscriptions.stream().filter(x -> x.getPayedUntil() == null || x.getPayedUntil().isBefore(d)).collect(Collectors.toList());
	}

	@Override
	public void updateSubscrProduct(SubscrProduct p) {
		products = products.stream().filter(x -> x.getId() != p.getId()).collect(Collectors.toList());
		products.add(p);
	}
	
	@Override
	public void updateDelivery(SubscrDelivery art) {
		deliveries = deliveries.stream().filter(x -> x.getId() != art.getId()).collect(Collectors.toList());
		deliveries.add(art);
	}

	@Override
	public void updateArticle(SubscrArticle art) {
		articles = articles.stream().filter(x -> x.getId() != art.getId()).collect(Collectors.toList());
		articles.add(art);
	}

	@Override
	public void updateSubscriber(Subscriber art) {
		subscribers = subscribers.stream().filter(x -> x.getId() != art.getId()).collect(Collectors.toList());
		subscribers.add(art);
	}

	@Override
	public void updateSubscription(Subscription art) {
		subscriptions = subscriptions.stream().filter(x -> x.getId() != art.getId()).collect(Collectors.toList());
		subscriptions.add(art);
	}

	@Override
	public void deleteDelivery(long delId) {
		deliveries = deliveries.stream().filter(x -> x.getId() != delId).collect(Collectors.toList());
	}

	//==============================================================================================
	//==============================================================================================
	//==============================================================================================
	//==============================================================================================
	
	private void createDeliveriesForToday() {
		for (Subscription s : getSubscriptions()) {
			if (Math.random() > 0.5) {
				createDelivery(s, getNewestArticleOfProduct(s.getProductId()), LocalDate.now());
			}
		}
	}

	private void createDeliveries() {
		deliveries = new ArrayList<SubscrDelivery>();
		for (Subscription s : getSubscriptions()) {
			List<SubscrArticle> arts = getArticlesOfProduct(s.getProductId());
			createDeliveries(s, arts.subList((int) (Math.random() * arts.size() * 0.5d), arts.size()));
		}
		
	}

	private void createDeliveries(Subscription s, List<SubscrArticle> artList) {
		for(SubscrArticle a : artList ) {
			createDelivery(s, a, a.getErschTag().plusDays((int) (2 * Math.random() + 0.5d)));
		}
		
	}

	private void createArticles() {
		articles = new ArrayList<>();
		SubscrArticle a = null;
		// Der Archivar, halbjährlich
		a = createArticle(1,  22,20456, 1d, new LocalDate(2004,6,10));
		createRepetitions(a, 12);
		// Steuergesetze, monatlich
		a = createArticle(2,  22,5456, 1d, new LocalDate(2008,1,1));
		createRepetitions(a, 12 * 8);
		// Bürgerliche Gesetze, monatlich
		a = createArticle(3,  22,2126, 1d, new LocalDate(2008,1,1));
		createRepetitions(a, 12 * 12);
		// Verwaltungsvorschriften BW, monatlich
		a = createArticle(4,  22,9827, 1d, new LocalDate(2008,1,1));
		createRepetitions(a, 8 * 12);
		// Baugesetzbuch, 3monatlich
		a = createArticle(5,  22,19827, 1d, new LocalDate(2008,4,1));
		createRepetitions(a, 4 * 8);
		// Brandschutz, jährlich
		a = createArticle(6,  22,19827, 1d, new LocalDate(2008,1,1));
		createRepetitions(a, 8);
		// Altenpflege, 2 monatlich
		a = createArticle(7,  22,19827, 0.98d, new LocalDate(2008,3,5));
		createRepetitions(a, 6 * 8);
		// Atelier, 3monatlich
		a = createArticle(8,  22, 19827, 0.95d, new LocalDate(2008,4,4));
		createRepetitions(a, 4 * 8);
		// Autowelt, 14 tägig
		a = createArticle(9,  22,19827, 0.78d, new LocalDate(2008,8,10));
		createRepetitions(a, 26 * 12);
	}

	private void createRepetitions(SubscrArticle a, int c) {
		SubscrProduct p = getSubscrProduct(a.getProductId());
		while (c > 0) {
			a = createNextArticle(a, p);
			c--;
		}
	}

	private void createSubscriptions() {
		subscriptions = new ArrayList<Subscription>();
		createSubscription(1, 3123, 1, new LocalDate(2004,6,10), null, "Stadtarchiv", "Frau Hempel", ShipType.DELIVERY, 0L, PayIntervalType.MONTHLY, LocalDate.now().plusDays(4) );
		createSubscription(2, 3123, 3, new LocalDate(2005,7,13), null, "Kämmerer",    "Fritz Blaurock", ShipType.DELIVERY, 0L, PayIntervalType.MONTHLY, LocalDate.now().plusDays(4) );
		createSubscription(3, 3123, 3, new LocalDate(2006,3,20), null, "Hauptamt",    null, ShipType.DELIVERY, 0L, PayIntervalType.MONTHLY, LocalDate.now().plusDays(4) );
		createSubscription(4, 3123, 1, new LocalDate(2004,2,22), null, "Ordnungsamt", null, ShipType.DELIVERY, 0L, PayIntervalType.MONTHLY, LocalDate.now().plusDays(4) );
		createSubscription(5, 3123, 2, new LocalDate(2005,8,17), null, "Bauamt",      "Baby Hermann", ShipType.DELIVERY, 0L, PayIntervalType.MONTHLY, LocalDate.now().plusDays(4) );

		createSubscription(1, 4123, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.PICKUP, 0L, PayIntervalType.EACHDELIVERY, null );
		createSubscription(4, 4123, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.PICKUP, 0L, PayIntervalType.EACHDELIVERY, null );

		
		createSubscription(2, 3523, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.DELIVERY, 0L, PayIntervalType.EACHDELIVERY, null );
		createSubscription(3, 3523, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.DELIVERY, 0L, PayIntervalType.EACHDELIVERY, null );
		createSubscription(4, 3523, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.DELIVERY, 0L, PayIntervalType.EACHDELIVERY, null );

		createSubscription(2, 3173, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.MAIL, 0L, PayIntervalType.HALFYEARLY, LocalDate.now().minusMonths(1) );
		createSubscription(3, 3173, 2, new LocalDate(2005,8,17), null, null,      null, ShipType.MAIL, 0L, PayIntervalType.HALFYEARLY, LocalDate.now().minusMonths(3) );

		createSubscription(6, 5163, 1, new LocalDate(2013,1,20), null, null,      null, ShipType.PUBLISHER, 500L, PayIntervalType.YEARLY, LocalDate.now().minusMonths(1) );

		createSubscription(7, 112, 1, new LocalDate(2013,1,20), null, null,      null, ShipType.PUBLISHER, 550L, PayIntervalType.YEARLY, LocalDate.now().plusDays(4) ); 

		createSubscription(8, 3128, 1, new LocalDate(2013,1,20), null, null,      null, ShipType.PUBLISHER, 1000L, PayIntervalType.YEARLY, new LocalDate(2016,9,20) );

		Address a = createAddress("Renz zuhause",null, null, "Holzweg 12", "78727", "Oberndorf");
		createSubscription(9, 22123, 1, new LocalDate(2013,1,20), a, null,      null, ShipType.MAIL, 1500L, PayIntervalType.YEARLY, new LocalDate(2016,2,20) );
	}


	private void createSubscribers() {
		subscribers = new ArrayList<Subscriber>();
		createSubscriber(3123,10108, "Stadt Schramberg",null, null,"Rathaus",       "78713", "Schramberg", true, false, ShipType.DELIVERY);
		createSubscriber(4123,10109, "Grundschule"     ,null, null,"Schulstr 21",   "78713", "Schramberg", true, false, ShipType.DELIVERY);
		createSubscriber(3523,10110, "Steuerkanzlei B" ,null, null,"Bahnhofstr 21", "78713", "Schramberg", false, false, ShipType.PICKUP);
		createSubscriber(3173,10111, "Steuerbüro Beck" ,null, null,"Sulzerweg 323", "78713", "Schramberg", false, false, ShipType.PICKUP);
		createSubscriber(3128,10112, "Ai Weiwei"       ,null, null,"Bauernhof",     "78713", "Schramberg", false, false, ShipType.PUBLISHER);
		createSubscriber(5163,10113, "Feuerwehr"       ,null, null,"Feuerwehrhaus", "78713", "Schramberg", true, false, ShipType.PUBLISHER);
		createSubscriber(112,10115,  "Pflegeheim"      ,null, null,"Schillerhöhe",  "78713", "Schramberg", false, true, ShipType.PUBLISHER);
		createSubscriber(22123,10118,"RA Renz"         ,null, null,"Gericht",       "78713", "Schramberg", false, true, ShipType.MAIL);
	}

	private void createProducts() {
		products = new ArrayList<>();
		createProduct(1,"Arch"  ,"Der Archivar"          ,"Cornelsen"   ,true,new LocalDate(1995,7,15),new LocalDate(2016,1,5),  new Period(0,6,0,0,0,0,0,0), 2, "Archivar {number}", 12, 1d);
		createProduct(2,"StG"   ,"Steuergesetze"         ,"C.H. Beck"   ,false,new LocalDate(1996,7,17),new LocalDate(2016,5,3),  new Period(0,1,0,0,0,0,0,0), 5, "Steuergesetze {number}, 55 S.", 12, 1d);
		createProduct(3,"BGB"   ,"Bürgerliche Gesetze"   ,"C.H. Beck"   ,false,new LocalDate(1997,8,25),new LocalDate(2016,4,25), new Period(0,1,0,0,0,0,0,0), 6, "BGB-Ergänzung {number}, 12 S.", 12, 1d);
		createProduct(4,"VwBW"  ,"Verwaltungsvorschr BW" ,"C.H. Beck"   ,false,new LocalDate(1998,8,17),new LocalDate(2016,4,21), new Period(0,1,0,0,0,0,0,0), 3, "Verwaltungsvorschiften {number}", 12, 1d);
		createProduct(5,"BauG"  ,"Baugesetzbuch"         ,"Boorberg"    ,false,new LocalDate(1999,9,25),new LocalDate(2016,5,25), new Period(0,3,0,0,0,0,0,0), 2, "Baugesetzbuch {number}", 12, 1d);
		createProduct(6,"BS"    ,"Brandschutz"           ,"Boorberg"    ,false,new LocalDate(1991,9,18),new LocalDate(2016,1,2),  new Period(1,0,0,0,0,0,0,0),  1, "Der Feuerwehrmann {date:yyyy}", 12, 1d);
		createProduct(7,"AltP"  ,"Altenpflege"           ,"Hanser"      ,true,new LocalDate(1992,2,29),new LocalDate(2016,3,10), new Period(0,2,0,0,0,0,0,0), 1, "Altenpflege für Anfänger {date:yyyy/MM}", 12, 0.76d);
		createProduct(8,"At"    ,"Atelier"               ,"Kunstwerk"   ,true,new LocalDate(1993,2,10),new LocalDate(2016,4,4),  new Period(0,3,0,0,0,0,0,0), 1, "Das Atelier {date:yyyy/MM}", 12, 0.90d);
		createProduct(9,"AW"    ,"Autowelt"              ,"Motorwelt"   ,true,new LocalDate(2013,1,20),new LocalDate(2016,5,2),  new Period(0,0,14,0,0,0,0,0), 1, "Auto, Motor & Sport {date:yy/MM}", 12, 0.78d);
	}

	private SubscrArticle createArticle(int productId, int issue, long brutto, double halfPercentage, LocalDate erschTag) {
		SubscrArticle a = new SubscrArticle();
		a.setProductId(productId);
		a.setHalfPercentage(halfPercentage);
		a.setErschTag(erschTag);
		a.setIssueNo(issue);
		a.updateBrutto(brutto);
		insertArticle(a);
		return a;
	}
	
	private SubscrArticle createNextArticle(SubscrArticle olda, SubscrProduct p) {
		SubscrArticle a = p.createNextArticle(olda.getErschTag().plus(p.getPeriod()));
		a.updateBrutto((long) (olda.getBrutto() *  (1d + Math.random() * 0.05d)));
		insertArticle(a);
		return a;
	}
	
	
	private void createSubscription(int productId, int subscriberId, int quantity, LocalDate startDate, Address deliveryAddress, String deliveryInfo1, String deliveryInfo2, ShipType shipType, long shipCost, PayIntervalType paymentType, LocalDate payedUntil) {
		Subscription s = new Subscription();
		s.setPointid(3);
		s.setProductId(productId);
		s.setSubscriberId(subscriberId);
		s.setQuantity(quantity);
		s.setStartDate(startDate);
		s.setDeliveryAddress(deliveryAddress);
		s.setDeliveryInfo1(deliveryInfo1);
		s.setDeliveryInfo2(deliveryInfo2);
		s.setShipmentType(shipType);
		s.setPaymentType(paymentType);
		s.setPayedUntil(payedUntil);
		s.setLastInvoiceDate(null);
		insertSubscription(s);
	}
	
	private void createSubscriber(int customerId, int debitorId, String name1, String name2, String name3, String strasse, String plz, String ort, boolean collectiveInvoice, boolean needsDeliveryNote, ShipType shipType) {
		Subscriber s = new Subscriber();
		s.setId(customerId);
		s.setPointid(3);
		s.setCustomerId(customerId);
		s.setDebitorId(debitorId);
		s.setName(name1);
		s.setInvoiceAddress(createAddress(name1, name2, name3, strasse, plz, ort));
		s.setCollectiveInvoice(collectiveInvoice);
		s.setNeedsDeliveryNote(needsDeliveryNote);
		s.setShipmentType(shipType);
		insertSubscriber(s);
	}
	
	
	private Address createAddress(String name1, String name2, String name3, String strasse, String plz, String ort) {
		Address a = new Address();
		a.setName1(name1);
		a.setName2(name2);
		a.setName3(name3);
		a.setStreet(strasse);
		a.setPostalcode(plz);
		a.setCity(ort);
		return a;
	}
	
	
	private void createProduct(int id, String abbrev, String name, String publisher, boolean payAdv, LocalDate startDate, LocalDate lastDelivery, Period period, int quantity, String namePattern, int count, double halfPercentage) {
		SubscrProduct p = new SubscrProduct();
		p.setId(id);
		p.setAbbrev(abbrev);
		p.setName(name);
		p.setPublisher(publisher);
		p.setPayPerDelivery(payAdv);
		p.setQuantity(quantity);
		p.setStartDate(startDate);
		p.setLastDelivery(lastDelivery);
		p.setPeriod(period);
		p.setNamePattern(namePattern);
		p.setCount(count);
		p.setHalfPercentage(halfPercentage);
		insertSubscrProduct(p);
	}

	public SubscrDelivery createDelivery(Subscription subscription,	SubscrArticle article, LocalDate deliveryDate) {
		SubscrDelivery d = new SubscrDelivery();
		d.setArticleName(article.getName());
		d.setDeliveryDate(deliveryDate);
		d.setSubscriptionId(subscription.getId());
		d.setSubscriberId(subscription.getSubscriberId());
		d.setArticleId(article.getId());
		d.setQuantity(subscription.getQuantity());
		d.setTotal(subscription.getQuantity() * article.getBrutto());
		if (article.getHalfPercentage() >0.5) {
			d.setTotalHalf(subscription.getQuantity() * article.getBrutto_half());
			d.setTotalFull(d.getTotal() - d.getTotalHalf());
		} else {
			d.setTotalFull(subscription.getQuantity() * article.getBrutto_full());
			d.setTotalHalf(d.getTotal() - d.getTotalFull());
		}
		if (subscription.getPayedUntil() != null && deliveryDate.isBefore(subscription.getPayedUntil())) {
			d.setPayed(true);
		} else {
			d.setPayed(false);
		}
		d.setCreationDate(DateTime.now());
		insertDelivery(d);
		return d;
	}

	@Override
	public void insert(UserChange u) {
		changes.add(u);
	}

	@Override
	public List<PosInvoice> getSubscrInvoices(int id, LocalDate d) {
		return tempInvoices;
	}

	@Override
	public List<Subscription> getSubscriptionsWithMemo() {
		return subscriptions.stream().filter(s -> s.getMemo() != null && s.getMemo().isEmpty() == false).collect(Collectors.toList());
	}

	@Override
	public List<PosInvoice> getSubscriberInvoices(int debId) {
		return Collections.emptyList();
	}

}
