package net.buchlese.bofc.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.ReportDeliveryNote;
import net.buchlese.bofc.api.bofc.ReportDeliveryProtocol;
import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.ShipType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.core.CreationUtils;
import net.buchlese.bofc.core.DateUtils;
import net.buchlese.bofc.core.NumberGenerator;
import net.buchlese.bofc.core.PDFInvoice;
import net.buchlese.bofc.core.PDFReport;
import net.buchlese.bofc.core.SubscriptionInvoiceCreator;
import net.buchlese.bofc.core.reports.ReportDeliveryNoteCreator;
import net.buchlese.bofc.core.reports.ReportDeliveryProtocolCreator;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaPosInvoiceDAO;
import net.buchlese.bofc.jpa.JpaPosIssueSlipDAO;
import net.buchlese.bofc.jpa.JpaSubscrArticleDAO;
import net.buchlese.bofc.jpa.JpaSubscrDeliveryDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDeliveryDAO;
import net.buchlese.bofc.jpa.JpaSubscrProductDAO;
import net.buchlese.bofc.jpa.JpaSubscriberDAO;
import net.buchlese.bofc.jpa.JpaSubscriptionDAO;
import net.buchlese.bofc.resources.helper.IssueSlipUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscrArticleUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscrDeliveryUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscrIntervalDeliveryUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscrIntervalUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscrProductUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscriberUpdateHelper;
import net.buchlese.bofc.resources.helper.SubscriptionUpdateHelper;
import net.buchlese.bofc.resources.helper.UpdateResult;
import net.buchlese.bofc.view.subscr.CustomerAddView;
import net.buchlese.bofc.view.subscr.DeliveryNotesView;
import net.buchlese.bofc.view.subscr.InvoicesView;
import net.buchlese.bofc.view.subscr.NavigationView;
import net.buchlese.bofc.view.subscr.ProductAddView;
import net.buchlese.bofc.view.subscr.SubscrCustomerView;
import net.buchlese.bofc.view.subscr.SubscrDashboardView;
import net.buchlese.bofc.view.subscr.SubscrDeliveryView;
import net.buchlese.bofc.view.subscr.SubscrDispoView;
import net.buchlese.bofc.view.subscr.SubscrIntervalDeliveryView;
import net.buchlese.bofc.view.subscr.SubscrIntervalDispoView;
import net.buchlese.bofc.view.subscr.SubscrProductDetailView;
import net.buchlese.bofc.view.subscr.SubscrProductsView;
import net.buchlese.bofc.view.subscr.SubscriberDetailView;
import net.buchlese.bofc.view.subscr.SubscriptionAddView;
import net.buchlese.bofc.view.subscr.SubscriptionDetailView;

@Path("/subscr")
public class SubscrResource {

	private static class DateParam {
		private java.sql.Date date;
		public DateParam(String dateStr) {
			if (dateStr != null) { 
				try {
					date = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr).getTime());
				} catch (ParseException e) {
					date = DateUtils.now();
				}
			} else {
				date = DateUtils.now();
			}
		}
		public java.sql.Date getDate() {
			return date;
		}
		public java.time.LocalDate getLocalDate() {
			return date.toLocalDate();
		}
	}
	private final SubscrDAO dao;
	
	private final JpaPosInvoiceDAO jpaPosInvoiceDao;
	private final JpaPosIssueSlipDAO jpaPosIssueSlipDao;

	private final JpaSubscriberDAO jpaSubscriberDao;
	private final JpaSubscriptionDAO jpaSubscriptionDao;
	private final JpaSubscrProductDAO jpaSubscrProductDao;
	private final JpaSubscrArticleDAO jpaSubscrArticleDao;
	private final JpaSubscrDeliveryDAO jpaSubscrDeliveryDao;
	private final JpaSubscrIntervalDAO jpaSubscrIntervalDao;
	private final JpaSubscrIntervalDeliveryDAO jpaSubscrIntervalDeliveryDao;

	private final NumberGenerator numGen;
	
	@Inject
	public SubscrResource(SubscrDAO sdao, NumberGenerator g,
			JpaSubscriberDAO j1, JpaSubscriptionDAO j2, JpaSubscrProductDAO j3, JpaSubscrArticleDAO j4,
			JpaSubscrDeliveryDAO j5, JpaSubscrIntervalDAO j6, JpaSubscrIntervalDeliveryDAO j7, JpaPosInvoiceDAO j8, JpaPosIssueSlipDAO j9) {
		super();
		this.dao = sdao;
		this.numGen =g;
		this.jpaSubscriberDao = j1;
		this.jpaSubscriptionDao = j2;
		this.jpaSubscrProductDao = j3;
		this.jpaSubscrArticleDao = j4;
		this.jpaSubscrDeliveryDao = j5;
		this.jpaSubscrIntervalDao = j6;
		this.jpaSubscrIntervalDeliveryDao = j7;
		this.jpaPosInvoiceDao = j8;
		this.jpaPosIssueSlipDao = j9;
	}

//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("transferAll")
//	@UnitOfWork
//	public void transferAll()  {
//		List<Subscriber> subscrs = dao.getSubscribers();
//		for(Subscriber sub : subscrs) {
//			jpaSubscriberDao.create(sub);
//		}
//		List<SubscrProduct> products = dao.getSubscrProducts();
//		for(SubscrProduct p : products) {
//			jpaSubscrProductDao.create(p);
//			List<SubscrArticle> arts = dao.getArticlesOfProduct(p.getId());
//			for(SubscrArticle a : arts) {
//				jpaSubscrArticleDao.create(a);
//			}
//			List<SubscrInterval> ints = dao.getIntervalsOfProduct(p.getId());
//			for(SubscrInterval i : ints) {
//				jpaSubscrIntervalDao.create(i);
//			}
//		}
//		List<Subscription> subs = dao.getSubscriptions();
//		for(Subscription s : subs) {
//			jpaSubscriptionDao.create(s);
//			List<SubscrDelivery> arts = dao.getDeliveriesForSubscription(s.getId());
//			for(SubscrDelivery a : arts) {
//				jpaSubscrDeliveryDao.create(a);
//			}
//			List<SubscrIntervalDelivery> ints = dao.getIntervalDeliveriesForSubscription(s.getId());
//			for(SubscrIntervalDelivery a : ints) {
//				jpaSubscrIntervalDeliveryDao.create(a);
//			}
//		}
//	}

	@POST
	@Path("/customerCreate")
	@Consumes("application/x-www-form-urlencoded")
	@UnitOfWork
	public View addCustomer(MultivaluedMap<String, String> par) {
		Subscriber s = new Subscriber();
		s.setName(par.getFirst("name"));
		s.setPointid(Integer.parseInt(par.getFirst("pointId")));
		if (par.containsKey("customerId") && par.getFirst("customerId").isEmpty() == false) {
			try {
				s.setCustomerId(Integer.parseInt(par.getFirst("customerId")));
			} catch (Exception e) {
			}
		}
		if (s.getCustomerId() == 0) {
			s.setCustomerId(numGen.getNextCustomerNumber(s.getPointid()));
		}
		if (par.containsKey("collectiveInvoice")) {
			s.setCollectiveInvoice(true);
		}
		if (par.containsKey("needsDeliveryNote")) {
			s.setNeedsDeliveryNote(true);
		}
		if (par.containsKey("shipmentType")) {
			s.setShipmentType(ShipType.valueOf(par.getFirst("shipmentType")));
		} else {
			s.setShipmentType(ShipType.DELIVERY);
		}
		Address a = new Address();
		if (par.containsKey("invoiceAddress.line1") && par.get("invoiceAddress.line1").isEmpty() == false) {
			a.setName1(par.getFirst("invoiceAddress.line1"));
			a.setName2(par.getFirst("invoiceAddress.line2"));
			a.setName3(par.getFirst("invoiceAddress.line3"));
			a.setStreet(par.getFirst("invoiceAddress.street"));
			a.setPostalcode(par.getFirst("invoiceAddress.postalcode"));
			a.setCity(par.getFirst("invoiceAddress.city"));
			s.setInvoiceAddress(a);
		}
		jpaSubscriberDao.create(s);
		return new SubscrCustomerView(dao);
	}

	@POST
	@Path("/subscriptionCreate")
	@Consumes("application/x-www-form-urlencoded")
	@UnitOfWork
	public void addSubscription(MultivaluedMap<String, String> par) {
		Subscription s = new Subscription();
		if (par.containsKey("subscriberId") && par.getFirst("subscriberId").isEmpty() == false) {
			Subscriber subscriber = dao.getSubscriber(Long.parseLong(par.getFirst("subscriberId")));
			s.setSubscriber(subscriber);
		} else {
			throw new WebApplicationException("ohne Kundennummer geht nix");
		}
		if (par.containsKey("productId") && par.getFirst("productId").isEmpty() == false) {
			SubscrProduct product = dao.getSubscrProduct(Long.parseLong(par.getFirst("productId")));
			s.setProduct(product);
		} else {
			throw new WebApplicationException("ohne Periodikumnummer geht nix");
		}
		s.setDeliveryInfo1(par.getFirst("deliveryInfo1"));
		s.setDeliveryInfo2(par.getFirst("deliveryInfo2"));
		if (par.containsKey("shipmentType")) {
			s.setShipmentType(ShipType.valueOf(par.getFirst("shipmentType")));
		} else {
			s.setShipmentType(ShipType.DELIVERY);
		}
		if (par.containsKey("paymentType")) {
			s.setPaymentType(PayIntervalType.valueOf(par.getFirst("paymentType")));
		} else {
			s.setPaymentType(PayIntervalType.EACHDELIVERY);
		}
		if (par.containsKey("quantity") && par.getFirst("quantity").isEmpty() == false) {
			try {
				s.setQuantity(Integer.parseInt(par.getFirst("quantity")));
			} catch (NumberFormatException e) {
				s.setQuantity(1);
			}
		} else {
			s.setQuantity(1);
		}
		s.setStartDate(DateUtils.now());
		s.setPayedUntil(null);
		if (par.containsKey("deliveryAddress.line1") && par.get("deliveryAddress.line1").isEmpty() == false) {
			Address a = new Address();
			a.setName1(par.getFirst("deliveryAddress.line1"));
			a.setName2(par.getFirst("deliveryAddress.line2"));
			a.setName3(par.getFirst("deliveryAddress.line3"));
			a.setStreet(par.getFirst("deliveryAddress.street"));
			a.setPostalcode(par.getFirst("deliveryAddress.postalcode"));
			a.setCity(par.getFirst("deliveryAddress.city"));
		}
		jpaSubscriptionDao.create(s);
	}


	@POST
	@Path("/productCreate")
	@Consumes("application/x-www-form-urlencoded")
	@UnitOfWork
	public View addSubscrProduct(MultivaluedMap<String, String> par) {
		SubscrProduct p = new SubscrProduct();
		p.setAbbrev(par.getFirst("abbrev"));
		p.setName(par.getFirst("name"));
		p.setPublisher(par.getFirst("publisher"));
		p.setNamePattern(par.getFirst("namePattern"));
		p.setPayPerDelivery(false);
		if (par.containsKey("quantity") && par.getFirst("quantity").isEmpty() == false) {
			try {
				p.setQuantity(Integer.parseInt(par.getFirst("quantity")));
			} catch (NumberFormatException e) {
				p.setQuantity(1);
			}
		} else {
			p.setQuantity(1);
		}
		if (par.containsKey("count") && par.getFirst("count").isEmpty() == false) {
			try {
				p.setCount(Integer.parseInt(par.getFirst("count")));
			} catch (NumberFormatException e) {
				p.setCount(1);
			}
		} else {
			p.setCount(1);
		}
		if (par.containsKey("halfPercentage") && par.getFirst("halfPercentage").isEmpty() == false) {
			try {
				p.setHalfPercentage(Double.parseDouble(par.getFirst("halfPercentage")));
			} catch (NumberFormatException e) {
				p.setHalfPercentage(1d);
			}
		} else {
			p.setHalfPercentage(1d);
		}
		jpaSubscrProductDao.create(p);
		SubscrArticle art = CreationUtils.createArticle(p);
		jpaSubscrArticleDao.create(art);
		return new SubscrProductDetailView(dao, p);
	}

	@GET
	@Path("/createCollInvoice/{sub}")
	@Produces({"application/json"})
	public PosInvoice createCollInvoice(@PathParam("sub") String subIdP) {
		long subId = Long.parseLong(subIdP);
		PosInvoice inv = SubscriptionInvoiceCreator.createCollectiveSubscription(dao, dao.getSubscriber(subId), numGen);
		return inv;
	}

	@Produces({"application/pdf"})
	@GET
	@Path("/pdfcreateCollInvoice/{sub}")
	@UnitOfWork
	public Response createCollPdfInvoice(@PathParam("sub") String subIdP)  {
		PosInvoice inv = createCollInvoice(subIdP); 
		return invoiceResponse(Arrays.asList(inv));
	}

	@GET
	@Path("/deliverycreate/{sub}/{art}/{date}")
	@Produces({"application/json"})
	@UnitOfWork
	public SubscrDelivery createDelivery(@PathParam("sub") String subIdP,@PathParam("art") String artIdP,@PathParam("date") String dateP ) {
		long subId = Long.parseLong(subIdP);
		long artId = Long.parseLong(artIdP);
		Date deliveryDate = new DateParam(dateP).getDate();
		Subscription subscription = jpaSubscriptionDao.findById(subId);
		SubscrArticle article = dao.getSubscrArticle(artId);
		SubscrDelivery d = new SubscrDelivery();
		SubscrProduct p = subscription.getProduct();
		Subscriber subscriber  = subscription.getSubscriber();
		d.setArticleName(article.getName());
		d.setDeliveryDate(deliveryDate);
		d.setSubscription(subscription);
		d.setSubscriber(subscriber);
		d.setQuantity(subscription.getQuantity());
		d.setTotal(subscription.getQuantity() * article.getBrutto());
		if (article.getHalfPercentage() >0.5) {
			d.setTotalHalf(subscription.getQuantity() * article.getBrutto_half());
			d.setTotalFull(d.getTotal() - d.getTotalHalf());
		} else {
			d.setTotalFull(subscription.getQuantity() * article.getBrutto_full());
			d.setTotalHalf(d.getTotal() - d.getTotalFull());
		}
		d.setCreationDate(DateUtils.nowTime());
		d.setPayed(p.isPayPerDelivery() == false);
		d.setSlipped(subscriber.isNeedsDeliveryNote() == false);

		jpaSubscrDeliveryDao.create(d);
		p.setLastDelivery(deliveryDate);
		jpaSubscrProductDao.update(p);
		return d;
	}

	@GET
	@Path("/intervaldeliverycreate/{sub}/{art}/{date}")
	@Produces({"application/json"})
	@UnitOfWork
	public SubscrIntervalDelivery createIntervalDelivery(@PathParam("sub") String subIdP,@PathParam("art") String artIdP,@PathParam("date") String dateP ) {
		long subId = Long.parseLong(subIdP);
		long artId = Long.parseLong(artIdP);
		Date deliveryDate = new DateParam(dateP).getDate();
		Subscription subscription = jpaSubscriptionDao.findById(subId);
		SubscrInterval article = dao.getSubscrInterval(artId);
		SubscrIntervalDelivery d = new SubscrIntervalDelivery();
		d.setIntervalName(article.getName());
		d.setDeliveryDate(deliveryDate);
		d.setSubscription(subscription);
		d.setSubscriber(subscription.getSubscriber());
		d.setInterval(article);
		d.setQuantity(subscription.getQuantity());
		d.setTotal(subscription.getQuantity() * article.getBrutto());
		if (article.getHalfPercentage() >0.5) {
			d.setTotalHalf(subscription.getQuantity() * article.getBrutto_half());
			d.setTotalFull(d.getTotal() - d.getTotalHalf());
		} else {
			d.setTotalFull(subscription.getQuantity() * article.getBrutto_full());
			d.setTotalHalf(d.getTotal() - d.getTotalFull());
		}
		d.setCreationDate(DateUtils.nowTime());

		jpaSubscrIntervalDeliveryDao.create(d);
		return d;
	}

	@GET
	@Path("/createInvoice/{sub}")
	@Produces({"application/json"})
	@UnitOfWork
	public PosInvoice createInvoice(@PathParam("sub") String subIdP) {
		long subId = Long.parseLong(subIdP);
		PosInvoice inv =  SubscriptionInvoiceCreator.createSubscription(dao, jpaSubscriptionDao.findById(subId), numGen);
		return inv;
	}

	@GET
	@Path("/pdfcreateInvoice/{sub}")
	@Produces({"application/pdf"})
	@UnitOfWork
	public Response createPdfInvoice(@PathParam("sub") String subIdP)  {
		PosInvoice inv = createInvoice(subIdP); 
		return invoiceResponse(Arrays.asList(inv));
	}

	@GET
	@Path("/subscrarticlecreate/{prod}")
	@Produces({"application/json"})
	@UnitOfWork
	public View createSubscrArticle(@PathParam("prod") String prodIdP) {
		long prodId = Long.parseLong(prodIdP);
		SubscrProduct product = dao.getSubscrProduct(prodId);
		SubscrArticle art = createNewArticle(product);
		return new SubscrDispoView(dao, product, art, LocalDate.now());
	}

	private SubscrArticle createNewArticle(SubscrProduct product) {
		SubscrArticle art = CreationUtils.createArticle(product);
		jpaSubscrArticleDao.create(art);
		jpaSubscrProductDao.update(product);
		return art;
	}

	@GET
	@Path("/subscrintervalcreate/{prod}")
	@Produces({"application/json"})
	@UnitOfWork
	public View createSubscrInterval(@PathParam("prod") String prodIdP) {
		long prodId = Long.parseLong(prodIdP);
		SubscrProduct product = dao.getSubscrProduct(prodId);
		SubscrInterval art = createNewInterval(product);
		return new SubscrIntervalDispoView(dao, product, art, LocalDate.now());
	}

	private SubscrInterval createNewInterval(SubscrProduct product) {
		SubscrInterval art = CreationUtils.createInterval(product);
		jpaSubscrIntervalDao.create(art);
		jpaSubscrProductDao.update(product);
		return art;
	}

	@GET
	@Path("/deliverydelete/{id}")
	@Produces({"text/html"})
	@UnitOfWork
	public View deleteDelivery(@PathParam("id") String deliIdP) {
		long delId = Long.parseLong(deliIdP);
		SubscrDelivery del = dao.getSubscrDelivery(delId);
		Subscription s = del.getSubscription();
		SubscrProduct p = s.getProduct();
		p.setLastDelivery(null);
		jpaSubscrProductDao.update(p);
		jpaSubscrDeliveryDao.delete(del);
		return new SubscrDashboardView(dao, LocalDate.now());
	}

	@GET
	@Path("/intervaldeliverydelete/{id}")
	@Produces({"text/html"})
	@UnitOfWork
	public View deleteIntervalDelivery(@PathParam("id") String deliIdP) {
		long delId = Long.parseLong(deliIdP);
		jpaSubscrIntervalDeliveryDao.delete(jpaSubscrIntervalDeliveryDao.findById(delId));
		return new SubscrDashboardView(dao, LocalDate.now());
	}

	@GET
	@Path("/invoiceView/{inv}")
	@Produces({"application/pdf"})
	@UnitOfWork
	public Response viewInvoice(@PathParam("inv") String invNum, @QueryParam("mark") Optional<String> mark) {
		List<PosInvoice> invs = null;
		if (invNum.equals("unprinted")) {
			// wir wollen einen Stapeldruck
			invs = jpaPosInvoiceDao.getSubscrInvoices(3, null, Boolean.FALSE, null, Boolean.FALSE, 50);
		} else {
			invs = jpaPosInvoiceDao.findByNumber(invNum);
		}
		
		Response x = invoiceResponse(invs);
		
		if (mark.isPresent()) {
			jpaPosInvoiceDao.markSubscrInvoicesAsPrinted(3, null, Boolean.FALSE, null, null, 50); // auch die stornierten als gedruckt markieren.
		}
		
		return x;
	}

	
	private Response invoiceResponse(List<PosInvoice> inv) {
		PDFInvoice generator = new PDFInvoice(inv);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			generator.generatePDF(baos);
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e);
		}
		
		StreamingOutput stream = new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				output.write(baos.toByteArray());
				output.flush();
			}
		};	
		String num = "viele";
		if (inv.size() == 1) {
			num = inv.get(0).getNumber();
		}
		return Response.ok(stream).header("Content-Disposition","attachment; filename=Rechnung_" + num + ".pdf").build();
	}
	
	
	@GET
	@Path("/invoiceCancel/{inv}")
	@Produces({"text/html"})
	@UnitOfWork
	public View cancelInvoice(@PathParam("inv") String invNum) {
		List<PosInvoice> invs = jpaPosInvoiceDao.findByNumber(invNum);
		if (invs.size()>1 || invs.isEmpty()) {
			throw new WebApplicationException("unable to cancel, more than one or no invoice with this number " + invNum, 500);
		}
		PosInvoice inv = invs.get(0);
		inv.setCancelled(true);
		jpaPosInvoiceDao.update(inv);
		SubscriptionInvoiceCreator.cancelInvoice(dao, inv);
		return new InvoicesView(jpaPosInvoiceDao.getSubscrInvoices(3, null, null, null, null, 25));
	}

	@GET
	@Path("/querycustomers")
	@Produces({"application/json"})
	@UnitOfWork
	public List<Subscriber> querySubscribers(@QueryParam("q") Optional<String> query) {
		if (query.isPresent() && query.get().isEmpty() == false) {
			return dao.querySubscribers("%" + query.get() + "%");
		}
		return dao.getSubscribers();
	}

	@GET
	@Path("/queryproduct")
	@Produces({"application/json"})
	@UnitOfWork
	public List<SubscrProduct> querySubscrProducts(@QueryParam("q") Optional<String> query) {
		if (query.isPresent() && query.get().isEmpty() == false) {
			return dao.querySubscrProducts("%" + query.get() + "%");
		}
		return jpaSubscrProductDao.findAll();
	}

	@GET
	@Path("/customerCreateForm")
	@Produces({"text/html"})
	@UnitOfWork
	public View showCustomerAddForm() {
		return new CustomerAddView(dao);
	}

	@GET
	@Path("/customers")
	@Produces({"text/html"})
	@UnitOfWork
	public View showCustomers() {
		return new SubscrCustomerView(dao);
	}

	@GET
	@Path("/invoices")
	@Produces({"text/html"})
	@UnitOfWork
	public View showInvoices(@QueryParam("spec") Optional<String> spec) {
		if (spec.isPresent()) {
			Boolean printed = null;
			Boolean payed = null;
			Boolean exported = null;
			Boolean cancelled = null;
			if (spec.get().contains("Pt")) {
				printed = Boolean.TRUE;
			}
			if (spec.get().contains("Pf")) {
				printed = Boolean.FALSE;
			}
			if (spec.get().contains("Yt")) {
				payed = Boolean.TRUE;
			}
			if (spec.get().contains("Yf")) {
				payed = Boolean.FALSE;
			}
			if (spec.get().contains("Et")) {
				exported = Boolean.TRUE;
			}
			if (spec.get().contains("Ef")) {
				exported = Boolean.FALSE;
			}
			if (spec.get().contains("Ct")) {
				cancelled = Boolean.TRUE;
			}
			if (spec.get().contains("Cf")) {
				cancelled = Boolean.FALSE;
			}
			return new InvoicesView(jpaPosInvoiceDao.getSubscrInvoices(3, payed, printed, exported, cancelled, 25));
		}
		return new InvoicesView(jpaPosInvoiceDao.getSubscrInvoices(3, null, null, null, null, 25));
	}

	@GET
	@Path("/deliveryNotes")
	@Produces({"text/html"})
	@UnitOfWork
	public View showDeliveryNotes() {
		return new DeliveryNotesView(jpaSubscrDeliveryDao.getDeliveryNotes(3, 25));
	}
	
	
	@GET
	@Path("/dashboard")
	@Produces({"text/html"})
	@UnitOfWork
	public View showDashboard() {
		return new SubscrDashboardView(dao, LocalDate.now());
	}

	@GET
	@Path("/delivery/{deliv}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showDelivery(@PathParam("deliv") String delivery) {
		long delId = Long.parseLong(delivery);
		return new SubscrDeliveryView(dao, dao.getSubscrDelivery(delId));
	}

	@GET
	@Path("/intervaldelivery/{deliv}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showIntervalDelivery(@PathParam("deliv") String delivery) {
		long delId = Long.parseLong(delivery);
		return new SubscrIntervalDeliveryView(dao, dao.getSubscrIntervalDelivery(delId));
	}

	@GET
	@Path("/deliveraddresslist")
	@Produces({"application/pdf"})
	@UnitOfWork
	public Response showDeliveryAdresses(@QueryParam("date") Optional<String> dateP) {
		Date date = new DateParam(dateP.orNull()).getDate();
		ReportDeliveryProtocol rep = ReportDeliveryProtocolCreator.create(dao, date);
		
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				try {
					PDFReport generator = new PDFReport(rep, "report/deliveryProtocol.xsl");
					generator.generatePDF(os);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
				os.flush();
			}
		};
		return Response.ok(stream).header("Content-Disposition","attachment; filename=Adressprotokoll_" + dateP + ".pdf").build();
	}

	@GET
	@Path("/deliverynote/{id}")
	@Produces({"application/pdf"})
	@UnitOfWork
	public Response showDeliveryNote(@PathParam("id") String deliveryId) {
		final long artId = Long.parseLong(deliveryId);
		final SubscrDelivery deli = dao.getSubscrDelivery(artId);
		final List<SubscrDelivery> deliveries = jpaSubscrDeliveryDao.getDeliveriesForSubscriberSlipflag(deli.getSubscriber(), deli.getDeliveryDate(), false);
		long num = numGen.getNextNumber();
		final ReportDeliveryNote rep = ReportDeliveryNoteCreator.create(num, deliveries);
		if (rep == null) {
			throw new WebApplicationException("keine lieferscheinrelevante Lieferung!");
		}
		jpaSubscrDeliveryDao.recordDetailsOnSlip(deliveries, String.valueOf(rep.delivNum));
		
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				try {
					PDFReport generator = new PDFReport(rep, "report/deliveryNote.xsl");
					generator.generatePDF(os);
				} catch (Exception e) {
					e.printStackTrace(System.err);
					throw new WebApplicationException(e);
				}
				os.flush();
			}
		};
		return Response.ok(stream).header("Content-Disposition","attachment; filename=Lieferschein_" + rep.delivNum + ".pdf").build();
	}

	@GET
	@Path("/getdeliverynote/{num}")
	@Produces({"application/pdf"})
	@UnitOfWork
	public Response getDeliveryNote(@PathParam("num") String deliveryNoteNum) {
		final List<SubscrDelivery> deliveries = jpaSubscrDeliveryDao.getDeliveriesForNote(deliveryNoteNum);
		final ReportDeliveryNote rep = ReportDeliveryNoteCreator.create(Long.parseLong(deliveryNoteNum), deliveries);
	
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				try {
					PDFReport generator = new PDFReport(rep, "report/deliveryNote.xsl");
					generator.generatePDF(os);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
				os.flush();
			}
		};
		return Response.ok(stream).header("Content-Disposition","attachment; filename=Lieferschein_" + rep.delivNum + ".pdf").build();
	}

	@GET
	@Path("/dispo/{prod}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showArticleDispo(@PathParam("prod") String product, @QueryParam("date") Optional<String> dateP, @QueryParam("artid") Optional<String> artIdP) {
		long productId = Long.parseLong(product);
		LocalDate from = new DateParam(dateP.orNull()).getLocalDate();
		SubscrProduct prod = dao.getSubscrProduct(productId );
		SubscrArticle art = null;
		if (artIdP.isPresent()) {
			art = dao.getSubscrArticle(Long.parseLong(artIdP.get()));
		} else {
			art = dao.getNewestArticleOfProduct(prod);
			if (art == null) {
				art = createNewArticle(prod);
			}
		}
		return new SubscrDispoView(dao, prod, art, from);
	}

	@GET
	@Path("/intervaldispo/{prod}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showIntervalDispo(@PathParam("prod") String product, @QueryParam("date") Optional<String> dateP, @QueryParam("artid") Optional<String> artIdP) {
		long productId = Long.parseLong(product);
		LocalDate from = new DateParam(dateP.orNull()).getLocalDate();
		SubscrProduct prod = dao.getSubscrProduct(productId );
		SubscrInterval art = null;
		if (artIdP.isPresent()) {
			art = dao.getSubscrInterval(Long.parseLong(artIdP.get()));
		} else {
			art = dao.getNewestIntervalOfProduct(prod);
			if (art == null) {
				art = createNewInterval(prod);
			}
		}
		return new SubscrIntervalDispoView(dao, prod, art, from);
	}

	@GET
	@Path("/disponav/{prod}/{dir}/{art}")
	@Produces({"text/html"})
	@UnitOfWork
	public View dispoArticleNav(@PathParam("prod") String prodIdP, @PathParam("dir") String dir,  @PathParam("art") String artIdP) {
		long artId = Long.parseLong(artIdP);
		long prodId = Long.parseLong(prodIdP);
		LocalDate from = LocalDate.now();
		SubscrProduct prod = dao.getSubscrProduct(prodId );
		Set<SubscrArticle> arts = prod.getArticles();
		long[] artIds = arts.stream().mapToLong(SubscrArticle::getId).sorted().toArray();
		int idx = Arrays.binarySearch(artIds, artId);
		if (dir.equals("prev") && idx > 0) {
			return new SubscrDispoView(dao, prod, dao.getSubscrArticle(artIds[idx-1]), from);
		}
		if (dir.equals("next") && idx >= 0 && idx < artIds.length-1) {
			return new SubscrDispoView(dao, prod, dao.getSubscrArticle(artIds[idx+1]), from);
		}
		if (dir.equals("next") && idx == artIds.length-1) {
			return new SubscrDispoView(dao, prod, dao.getSubscrArticle(artIds[idx]), from);
		}
		return new SubscrDispoView(dao, prod, dao.getSubscrArticle(artId), from);
	}

	@GET
	@Path("/intervaldisponav/{prod}/{dir}/{art}")
	@Produces({"text/html"})
	@UnitOfWork
	public View dispoIntervalNav(@PathParam("prod") String prodIdP, @PathParam("dir") String dir,  @PathParam("art") String artIdP) {
		long artId = Long.parseLong(artIdP);
		long prodId = Long.parseLong(prodIdP);
		LocalDate from = LocalDate.now();
		SubscrProduct prod = dao.getSubscrProduct(prodId );
		Set<SubscrInterval> arts = prod.getIntervals();
		long[] artIds = arts.stream().mapToLong(SubscrInterval::getId).sorted().toArray();
		int idx = Arrays.binarySearch(artIds, artId);
		if (dir.equals("prev") && idx > 0) {
			return new SubscrIntervalDispoView(dao, prod, dao.getSubscrInterval(artIds[idx-1]), from);
		}
		if (dir.equals("next") && idx >= 0 && idx < artIds.length-1) {
			return new SubscrIntervalDispoView(dao, prod, dao.getSubscrInterval(artIds[idx+1]), from);
		}
		if (dir.equals("next") && idx == artIds.length-1) {
			return new SubscrIntervalDispoView(dao, prod, dao.getSubscrInterval(artIds[idx]), from);
		}
		return new SubscrIntervalDispoView(dao, prod, dao.getSubscrInterval(artId), from);
	}

	
	@GET
	@Path("/navigation")
	public View showNavigation() {
		return new NavigationView();
	}

	@GET
	@Path("/product/{prod}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showProduct(@PathParam("prod") String product) {
		long productId = Long.parseLong(product);
		return new SubscrProductDetailView(dao, dao.getSubscrProduct(productId ));
	}
	
	@GET
	@Path("/productCreateForm")
	@Produces({"text/html"})
	@UnitOfWork
	public View showProductAddForm() {
		return new ProductAddView(dao);
	}

	@GET
	@Path("/products")
	@Produces({"text/html"})
	@UnitOfWork
	public View showProducts() {
		return new SubscrProductsView(dao, jpaSubscrProductDao.findAll());
	}


	@GET
	@Path("/subscriber/{sub}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showSubscriber(@PathParam("sub") String subdIdP) {
		long subId = Long.parseLong(subdIdP);
		return new SubscriberDetailView(dao, dao.getSubscriber(subId));
	}

	@GET
	@Path("/subscription/{sub}")
	@Produces({"text/html"})
	@UnitOfWork
	public View showSubscription(@PathParam("sub") String subdIdP) {
		long subId = Long.parseLong(subdIdP);
		return new SubscriptionDetailView(dao, jpaSubscriptionDao.findById(subId));
	}
	
	@GET
	@Path("/subscriptionCreateForm")
	@Produces({"text/html"})
	@UnitOfWork
	public View showSubscriptionAddForm(@QueryParam("sub") Optional<String> subIdP, @QueryParam("prod") Optional<String> prodIdP) {
		Subscriber s =  subIdP.transform(x -> dao.getSubscriber(Long.parseLong(x))).orNull();
		SubscrProduct  p =  prodIdP.transform(x -> dao.getSubscrProduct(Long.parseLong(x))).orNull();
		return new SubscriptionAddView(dao,s,p);
	}
	
	
	@POST
	@Path("/update")
	@Produces({"application/json"})
	@UnitOfWork
	public UpdateResult updateMaping( @FormParam("pk") String pk, @FormParam("name") String fieldname, @FormParam("value") String value) {
		UpdateResult res = null;
		if (fieldname.startsWith("article")) {
			res = new SubscrArticleUpdateHelper(dao, jpaSubscrArticleDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("intervalDelivery")) {
			res = new SubscrIntervalDeliveryUpdateHelper(dao, jpaSubscrIntervalDeliveryDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("interval.")) {
			res = new SubscrIntervalUpdateHelper(dao, jpaSubscrIntervalDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("subscriber")) {
			res = new SubscriberUpdateHelper(dao, jpaSubscriberDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("subscription")) {
			res = new SubscriptionUpdateHelper(dao, jpaSubscriptionDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("product")) {
			res = new SubscrProductUpdateHelper(dao, jpaSubscrProductDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("delivery")) {
			res = new SubscrDeliveryUpdateHelper(dao, jpaSubscrDeliveryDao).update(pk, fieldname, value);
		}
		if (fieldname.startsWith("issueSlip")) {
			res = new IssueSlipUpdateHelper(jpaPosIssueSlipDao).update(pk, fieldname, value);
		}
		if (res == null) {
			res = new UpdateResult();
			res.success = false;
			res.msg =" not implemented yet";
		}
		return res;
	}

	
	
}
