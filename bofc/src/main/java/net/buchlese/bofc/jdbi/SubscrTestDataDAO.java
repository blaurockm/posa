package net.buchlese.bofc.jdbi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.subscr.Address;
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

import com.google.common.base.Optional;

public class SubscrTestDataDAO implements SubscrDAO {

	private List<Subscriber> subscribers;
	private List<SubscrProduct> products;
	private List<Subscription> subscriptions;
	private List<SubscrArticle> articles;
	private List<SubscrDelivery> deliveries;
	
	private static long idcounter = 10;
	
	public SubscrTestDataDAO() {
		createProducts();
		createSubscribers();
		createSubscriptions();
		createArticles();
		createDeliveries();
		
		createDeliveriesForToday();
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
		return deliveries.stream().filter(x -> x.getSubcriptionId() == subid).max(Comparator.naturalOrder()).orElse(null);
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
		return deliveries.stream().filter(x -> x.getSubcriptionId() == subid && x.isPayed() == false).collect(Collectors.toList());
	}

	public List<SubscrDelivery> getDeliveriesForSubscriptionRecorded(long subid) {
		return deliveries.stream().filter(x -> x.getSubcriptionId() == subid && x.isPayed() ).collect(Collectors.toList());
	}

	public List<SubscrDelivery> getDeliveriesForSubscription(long subid) {
		return deliveries.stream().filter(x -> x.getSubcriptionId() == subid).collect(Collectors.toList());
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
	public void recordDetailsOnvInvoice(List<SubscrDelivery> deliveries, String invNumber) {
		deliveries.stream().forEach(x -> x.setPayed(true));
	}
	

	@Override
	public void insertSubscrProduct(SubscrProduct p) {
		p.setId(idcounter++);
		products.add(p);
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
	public void insertArticle(SubscrArticle p) {
		p.setId(idcounter++);
		articles.add(p);
	}
	
	@Override
	public SubscrDelivery createDelivery(Subscription subscription,	SubscrArticle article, LocalDate deliveryDate) {
		SubscrDelivery d = new SubscrDelivery();
		d.setId(idcounter++); 
		d.setArticleName(article.getName());
		d.setDeliveryDate(deliveryDate);
		d.setSubcriptionId(subscription.getId());
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
		d.setCreationDate(DateTime.now());
		deliveries.add(d);
		return d;
	}

	@Override
	public List<SubscrProduct> querySubscrProducts(Optional<String> query) {
		return products.stream().filter(x -> query.isPresent() == false || x.getName().contains(query.get())).collect(Collectors.toList());
	}

	@Override
	public List<Subscriber> querySubscribers(Optional<String> query) {
		return subscribers.stream().filter(x -> query.isPresent() == false || x.getName().contains(query.get()) || String.valueOf(x.getCustomerId()).contains(query.get())).collect(Collectors.toList());
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
		createSubscription(1, 3123, 1, new LocalDate(2004,6,10), null, "Stadtarchiv", "Frau Hempel", ShipType.DELIVERY, 0L );
		createSubscription(2, 3123, 3, new LocalDate(2005,7,13), null, "Kämmerer",    "Fritz Blaurock", ShipType.DELIVERY, 0L );
		createSubscription(3, 3123, 3, new LocalDate(2006,3,20), null, "Hauptamt",    null, ShipType.DELIVERY, 0L );
		createSubscription(4, 3123, 1, new LocalDate(2004,2,22), null, "Ordnungsamt", null, ShipType.DELIVERY, 0L );
		createSubscription(5, 3123, 2, new LocalDate(2005,8,17), null, "Bauamt",      "Baby Hermann", ShipType.DELIVERY, 0L );

		createSubscription(1, 4123, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.PICKUP, 0L );
		createSubscription(4, 4123, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.PICKUP, 0L );

		
		createSubscription(2, 3523, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.DELIVERY, 0L );
		createSubscription(3, 3523, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.DELIVERY, 0L );
		createSubscription(4, 3523, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.DELIVERY, 0L );

		createSubscription(2, 3173, 1, new LocalDate(2005,8,17), null, null,      null, ShipType.MAIL, 0L );
		createSubscription(3, 3173, 2, new LocalDate(2005,8,17), null, null,      null, ShipType.MAIL, 0L );

		createSubscription(6, 5163, 1, new LocalDate(2013,1,20), null, null,      null, ShipType.PUBLISHER, 500L );

		createSubscription(7, 112, 1, new LocalDate(2013,1,20), null, null,      null, ShipType.PUBLISHER, 550L ); 

		createSubscription(8, 3128, 1, new LocalDate(2013,1,20), null, null,      null, ShipType.PUBLISHER, 1000L );

		Address a = createAddress("Renz zuhause",null, null, "Holzweg 12", "78727", "Oberndorf");
		createSubscription(9, 22123, 1, new LocalDate(2013,1,20), a, null,      null, ShipType.MAIL, 1500L );
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
		a.setId(idcounter++);
		a.setProductId(productId);
		a.setHalfPercentage(halfPercentage);
		a.setErschTag(erschTag);
		a.setIssueNo(issue);
		a.updateBrutto(brutto);
		articles.add(a);
		return a;
	}
	
	private SubscrArticle createNextArticle(SubscrArticle olda, SubscrProduct p) {
		SubscrArticle a = p.createNextArticle(olda.getErschTag().plus(p.getPeriod()));
		a.setId(idcounter++);
		a.updateBrutto((long) (olda.getBrutto() *  (1d + Math.random() * 0.05d)));
		articles.add(a);
		return a;
	}
	
	
	private void createSubscription(int productId, int subscriberId, int quantity, LocalDate startDate, Address deliveryAddress, String deliveryInfo1, String deliveryInfo2, ShipType shipType, long shipCost) {
		Subscription s = new Subscription();
		s.setId(idcounter++);
		s.setPointid(3);
		s.setProductId(productId);
		s.setSubscriberId(subscriberId);
		s.setQuantity(quantity);
		s.setStartDate(startDate);
		s.setDeliveryAddress(deliveryAddress);
		s.setDeliveryInfo1(deliveryInfo1);
		s.setDeliveryInfo2(deliveryInfo2);
		s.setShipmentType(shipType);
		s.setShipmentCost(shipCost);
		subscriptions.add(s);
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
		subscribers.add(s);
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
		p.setPayedInAdvance(payAdv);
		p.setQuantity(quantity);
		p.setStartDate(startDate);
		p.setLastDelivery(lastDelivery);
		p.setPeriod(period);
		p.setNamePattern(namePattern);
		p.setCount(count);
		p.setHalfPercentage(halfPercentage);
		products.add(p);
	}



}
