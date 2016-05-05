package net.buchlese.bofc.jdbi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.subscr.Address;
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

	public List<SubscrProduct> getSubscrProducts() {
		return products;
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<Subscription> getSubscriptionsForProduct(long prodid) {
		return subscriptions.stream().filter(x -> x.getProductId() == prodid).collect(Collectors.toList());
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

	public List<SubscrDelivery> getDeliveriesForSubscription(long subid) {
		return deliveries.stream().filter(x -> x.getSubcriptionId() == subid).collect(Collectors.toList());
	}

	private void createDeliveriesForToday() {
		for (Subscription s : getSubscriptions()) {
			if (Math.random() > 0.5) {
				createDelivery(getNewestArticleOfProduct(s.getProductId()), s, LocalDate.now());
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
			createDelivery(a, s, a.getTimest().toLocalDate().plusDays((int) (2 * Math.random() + 0.5d)));
		}
		
	}

	private void createDelivery(SubscrArticle article, Subscription subscription, LocalDate deliveryDate) {
		SubscrDelivery d = new SubscrDelivery();
		d.setArticle(article);
		d.setDeliveryDate(deliveryDate);
		d.setSubcriptionId(subscription.getId());
		d.setSubscriberId(subscription.getSubscriberId());
		d.setArticleId(article.getId());
		d.setQuantity(subscription.getQuantity());
		d.setTotal(subscription.getQuantity() * article.getBrutto());
		d.setCreationDate(DateTime.now());
		deliveries.add(d);
	}

	private void createArticles() {
		articles = new ArrayList<>();
		SubscrArticle a = null;
		// Der Archivar, halbjärhlich
		a = createArticle(1, "Archivar {number}", 2004, 20456,1d, new DateTime(2004,6,10,10,55));
		createRepetitions(a, 12);
		// Steuergesetze, monatlich
		a = createArticle(2, "Steuergesetze {number}, 55 S.", 156, 5456,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 12 * 8);
		// Bürgerliche Gesetze, monatlich
		a = createArticle(3, "BGB-Ergänzung {number}, 12 S.", 956, 2126,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 12 * 12);
		// Verwaltungsvorschriften BW, monatlich
		a = createArticle(4, "Verwaltungsvorschiften {number}", 756, 9827,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 8 * 12);
		// Baugesetzbuch, 3monatlich
		a = createArticle(5, "Baugesetzbuch {number}", 2004, 19827,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 4 * 8);
		// Brandschutz, jährlich
		a = createArticle(6, "Der Feuerwehrmann {date:yyyy}", 2004, 19827,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 8);
		// Altenpflege,2 monatlich
		a = createArticle(7, "Altenpflege für Anfänger {date:yyyy/MM}", 2004, 19827,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 6 * 8);
		// Atelier, 3monatlich
		a = createArticle(8, "Das Atelier {date:yyyy/MM}", 2004, 19827,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 4 * 8);
		// Autowelt, 14 tägig
		a = createArticle(9, "Auto, Motor & Sport {date:yy/MM}", 2004, 19827,1d, new DateTime(2008,6,10,10,55));
		createRepetitions(a, 26 * 12);
	}

	private void createRepetitions(SubscrArticle a, int c) {
		while (c > 0) {
			a = createNextArticle(a);
			c--;
		}
	}

	private void createSubscriptions() {
		subscriptions = new ArrayList<Subscription>();
		createSubscription(1, 3123, 1, new LocalDate(2004,6,10), null, "Stadtarchiv", "Frau Hempel", false, true );
		createSubscription(2, 3123, 3, new LocalDate(2005,7,13), null, "Kämmerer",    "Fritz Blaurock", false, true );
		createSubscription(3, 3123, 3, new LocalDate(2006,3,20), null, "Hauptamt",    null, false, true );
		createSubscription(4, 3123, 1, new LocalDate(2004,2,22), null, "Ordnungsamt", null, false, true );
		createSubscription(5, 3123, 2, new LocalDate(2005,8,17), null, "Bauamt",      "Baby Hermann", false, true );

		createSubscription(1, 4123, 1, new LocalDate(2005,8,17), null, null,      null, false, true );
		createSubscription(4, 4123, 1, new LocalDate(2005,8,17), null, null,      null, false, true );

		
		createSubscription(2, 3523, 1, new LocalDate(2005,8,17), null, null,      null, false, true );
		createSubscription(3, 3523, 1, new LocalDate(2005,8,17), null, null,      null, false, true );
		createSubscription(4, 3523, 1, new LocalDate(2005,8,17), null, null,      null, false, true );

		createSubscription(2, 3173, 1, new LocalDate(2005,8,17), null, null,      null, true, true );
		createSubscription(3, 3173, 2, new LocalDate(2005,8,17), null, null,      null, true, true );

		createSubscription(6, 5163, 1, new LocalDate(2013,1,20), null, null,      null, true, true );

		createSubscription(7, 112, 1, new LocalDate(2013,1,20), null, null,      null, false, false ); // wird abgeholt

		createSubscription(8, 3128, 1, new LocalDate(2013,1,20), null, null,      null, false, false ); // wird abgeholt

		Address a = createAddress("Renz zuhause",null, null, "Holzweg 12", "78727", "Oberndorf");
		createSubscription(9, 22123, 1, new LocalDate(2013,1,20), a, null,      null, false, true );
	}


	private void createSubscribers() {
		subscribers = new ArrayList<Subscriber>();
		createSubscriber(3123,10108, "Stadt Schramberg",null, null,"Rathaus",       "78713", "Schramberg");
		createSubscriber(4123,10109, "Grundschule"     ,null, null,"Schulstr 21",   "78713", "Schramberg");
		createSubscriber(3523,10110, "Steuerkanzlei B" ,null, null,"Bahnhofstr 21", "78713", "Schramberg");
		createSubscriber(3173,10111, "Steuerbüro Beck" ,null, null,"Sulzerweg 323", "78713", "Schramberg");
		createSubscriber(3128,10112, "Ai Weiwei"       ,null, null,"Bauernhof",     "78713", "Schramberg");
		createSubscriber(5163,10113, "Feuerwehr"       ,null, null,"Feuerwehrhaus", "78713", "Schramberg");
		createSubscriber(112,10115,  "Pflegeheim"      ,null, null,"Schillerhöhe",  "78713", "Schramberg");
		createSubscriber(22123,10118,"RA Renz"         ,null, null,"Gericht",       "78713", "Schramberg");
	}

	private void createProducts() {
		products = new ArrayList<>();
		createProduct(1,"Arch"  ,"Der Archivar"          ,"Cornelsen"   ,2,new LocalDate(1995,7,15),new LocalDate(2016,1,5),  new Period(0,6,0,0,0,0,0,0));
		createProduct(2,"StG"   ,"Steuergesetze"         ,"C.H. Beck"   ,5,new LocalDate(1996,7,17),new LocalDate(2016,5,3),  new Period(0,1,0,0,0,0,0,0));
		createProduct(3,"BGB"   ,"Bürgerliche Gesetze"   ,"C.H. Beck"   ,6,new LocalDate(1997,8,25),new LocalDate(2016,4,25), new Period(0,1,0,0,0,0,0,0));
		createProduct(4,"VwBW"  ,"Verwaltungsvorschr BW" ,"C.H. Beck"   ,3,new LocalDate(1998,8,17),new LocalDate(2016,4,21), new Period(0,1,0,0,0,0,0,0));
		createProduct(5,"BauG"  ,"Baugesetzbuch"         ,"Boorberg"    ,2,new LocalDate(1999,9,25),new LocalDate(2016,5,25), new Period(0,3,0,0,0,0,0,0));
		createProduct(6,"BS"    ,"Brandschutz"           ,"Boorberg"    ,1,new LocalDate(1991,9,18),new LocalDate(2016,1,2),  new Period(1,0,0,0,0,0,0,0));
		createProduct(7,"AltP"  ,"Altenpflege"           ,"Hanser"      ,1,new LocalDate(1992,2,29),new LocalDate(2016,3,10), new Period(0,2,0,0,0,0,0,0));
		createProduct(8,"At"    ,"Atelier"               ,"Kunstwerk"   ,1,new LocalDate(1993,2,10),new LocalDate(2016,4,4),  new Period(0,3,0,0,0,0,0,0));
		createProduct(9,"AW"    ,"Autowelt"              ,"Motorwelt"   ,1,new LocalDate(2013,1,20),new LocalDate(2016,5,2),  new Period(0,0,14,0,0,0,0,0));
	}

	private SubscrArticle createArticle(int productId, String name, int count, long brutto, double halfPercentage, DateTime timest) {
		SubscrArticle a = new SubscrArticle();
		a.setProductId(productId);
		a.setNamePattern(name);
		a.setCount(count);
		a.setBrutto(brutto);
		a.setHalfPercentage(halfPercentage);
		a.setTimest(timest);
		a.initializeName();
		a.initializeBrutto();
		articles.add(a);
		return a;
	}
	
	private SubscrArticle createNextArticle(SubscrArticle olda) {
		SubscrArticle a = olda.clone();
		a.setBrutto((long) (olda.getBrutto() *  (1d + Math.random() * 0.05d)));
		a.initializeBrutto();
		Period p = getSubscrProduct(a.getProductId()).getPeriod();
		a.setCount(olda.getCount()+1);
		a.setTimest(olda.getTimest().plus(p));
		a.initializeName();
		articles.add(a);
		return a;
	}
	
	
	private void createSubscription(int productId, int subscriberId, int quantity, LocalDate startDate, Address deliveryAddress, String deliveryInfo1, String deliveryInfo2, boolean needsDeliveryNote, boolean needsInvoice) {
		Subscription s = new Subscription();
		s.setId(idcounter++);
		s.setProductId(productId);
		s.setSubscriberId(subscriberId);
		s.setQuantity(quantity);
		s.setStartDate(startDate);
		s.setDeliveryAddress(deliveryAddress);
		s.setDeliveryInfo1(deliveryInfo1);
		s.setDeliveryInfo2(deliveryInfo2);
		s.setNeedsDeliveryNote(needsDeliveryNote);
		s.setNeedsInvoice(needsInvoice);
		subscriptions.add(s);
	}
	
	private void createSubscriber(int customerId, int debitorId, String name1, String name2, String name3, String strasse, String plz, String ort) {
		Subscriber s = new Subscriber();
		s.setId(customerId);
		s.setCustomerId(customerId);
		s.setDebitorId(debitorId);
		s.setName(name1);
		s.setInvoiceAddress(createAddress(name1, name2, name3, strasse, plz, ort));
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
	
	
	private void createProduct(int id, String abbrev, String name, String publisher, int quantity, LocalDate startDate, LocalDate lastDelivery, Period period) {
		SubscrProduct p = new SubscrProduct();
		p.setId(id);
		p.setAbbrev(abbrev);
		p.setName(name);
		p.setPublisher(publisher);
		p.setQuantity(quantity);
		p.setStartDate(startDate);
		p.setLastDelivery(lastDelivery);
		p.setPeriod(period);
		products.add(p);
	}




	
	
	
}
