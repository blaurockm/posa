package net.buchlese.bofc.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.ReportDeliveryNote;
import net.buchlese.bofc.api.bofc.ReportDeliveryProtocol;
import net.buchlese.bofc.api.bofc.UserChange;
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
import net.buchlese.bofc.core.NumberGenerator;
import net.buchlese.bofc.core.PDFInvoice;
import net.buchlese.bofc.core.PDFReport;
import net.buchlese.bofc.core.SubscriptionInvoiceCreator;
import net.buchlese.bofc.core.reports.ReportDeliveryNoteCreator;
import net.buchlese.bofc.core.reports.ReportDeliveryProtocolCreator;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaPosInvoiceDAO;
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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/subscr")
public class SubscrResource {

	private static class DateParam {
		private LocalDate date;
		public DateParam(String dateStr) {
			if (dateStr != null) { 
				date = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(dateStr);
			} else {
				date = LocalDate.now();
			}
		}
		public LocalDate getDate() {
			return date;
		}
	}
	private final SubscrDAO dao;
	
	private final PosInvoiceDAO invDao;
	private final JpaPosInvoiceDAO jpaDao;

	private final JpaSubscriberDAO jpaSubscriberDao;
	private final JpaSubscriptionDAO jpaSubscriptionDao;
	private final JpaSubscrProductDAO jpaSubscrProductDao;
	private final JpaSubscrArticleDAO jpaSubscrArticleDao;
	private final JpaSubscrDeliveryDAO jpaSubscrDeliveryDao;
	private final JpaSubscrIntervalDAO jpaSubscrIntervalDao;
	private final JpaSubscrIntervalDeliveryDAO jpaSubscrIntervalDeliveryDao;
	private final SessionFactory sessFact;

	private final NumberGenerator numGen;
	
	private void recordUserChange(SubscrDAO dao, String login, long object, String fieldId, String oldValue, String newValue, String action) {
		UserChange uc = new UserChange();
		uc.setLogin(login);
		uc.setObjectId(object);
		uc.setFieldId(fieldId);
		uc.setOldValue(oldValue);
		uc.setNewValue(newValue);
		uc.setAction(StringUtils.left(action, 1));
		uc.setModDate(DateTime.now());
//		dao.insert(uc);
	}
	
	@Inject
	public SubscrResource(PosInvoiceDAO invd, SubscrDAO sdao, NumberGenerator g,
			JpaSubscriberDAO j1, JpaSubscriptionDAO j2, JpaSubscrProductDAO j3, JpaSubscrArticleDAO j4,
			JpaSubscrDeliveryDAO j5, JpaSubscrIntervalDAO j6, JpaSubscrIntervalDeliveryDAO j7, JpaPosInvoiceDAO j8, SessionFactory sessFact) {
		super();
		this.invDao = invd;
		this.dao = sdao;
		this.numGen =g;
		this.jpaSubscriberDao = j1;
		this.jpaSubscriptionDao = j2;
		this.jpaSubscrProductDao = j3;
		this.jpaSubscrArticleDao = j4;
		this.jpaSubscrDeliveryDao = j5;
		this.jpaSubscrIntervalDao = j6;
		this.jpaSubscrIntervalDeliveryDao = j7;
		this.jpaDao = j8;
		this.sessFact = sessFact;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("transferAll")
	@UnitOfWork
	public void transferAll()  {
		List<Subscriber> subscrs = dao.getSubscribers();
		for(Subscriber sub : subscrs) {
			jpaSubscriberDao.create(sub);
		}
		List<SubscrProduct> products = dao.getSubscrProducts();
		for(SubscrProduct p : products) {
			jpaSubscrProductDao.create(p);
			List<SubscrArticle> arts = dao.getArticlesOfProduct(p.getId());
			for(SubscrArticle a : arts) {
				jpaSubscrArticleDao.create(a);
			}
			List<SubscrInterval> ints = dao.getIntervalsOfProduct(p.getId());
			for(SubscrInterval i : ints) {
				jpaSubscrIntervalDao.create(i);
			}
		}
		List<Subscription> subs = dao.getSubscriptions();
		for(Subscription s : subs) {
			jpaSubscriptionDao.create(s);
			List<SubscrDelivery> arts = dao.getDeliveriesForSubscription(s.getId());
			for(SubscrDelivery a : arts) {
				jpaSubscrDeliveryDao.create(a);
			}
			List<SubscrIntervalDelivery> ints = dao.getIntervalDeliveriesForSubscription(s.getId());
			for(SubscrIntervalDelivery a : ints) {
				jpaSubscrIntervalDeliveryDao.create(a);
			}
		}
	}

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
		dao.insertSubscriber(s);
		jpaSubscriberDao.create(s);
		recordUserChange(dao, "master", s.getCustomerId(), "customer", null, null, "N");
		return new SubscrCustomerView(dao);
	}

	@POST
	@Path("/subscriptionCreate")
	@Consumes("application/x-www-form-urlencoded")
	@UnitOfWork
	public void addSubscription(MultivaluedMap<String, String> par) {
		Subscription s = new Subscription();
		if (par.containsKey("subscriberId") && par.getFirst("subscriberId").isEmpty() == false) {
			s.setSubscriberId(Long.parseLong(par.getFirst("subscriberId")));
		} else {
			throw new WebApplicationException("ohne Kundennummer geht nix");
		}
		if (par.containsKey("productId") && par.getFirst("productId").isEmpty() == false) {
			s.setProductId(Long.parseLong(par.getFirst("productId")));
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
		s.setStartDate(LocalDate.now());
		if (par.containsKey("payedUntil") && par.getFirst("payedUntil").isEmpty() == false) {
			try {
				s.setPayedUntil(org.joda.time.YearMonth.parse(par.getFirst("payedUntil"), DateTimeFormat.forPattern("MM/yy")).toLocalDate(28));
			} catch (Exception e) {
				try {
					s.setPayedUntil(org.joda.time.YearMonth.parse(par.getFirst("payedUntil"), DateTimeFormat.forPattern("MM/yyyy")).toLocalDate(28));
				} catch (Exception e1) {
					s.setPayedUntil(null);
				}
			}
		} else {
			s.setPayedUntil(null);
		}
		if (par.containsKey("deliveryAddress.line1") && par.get("deliveryAddress.line1").isEmpty() == false) {
			Address a = new Address();
			a.setName1(par.getFirst("deliveryAddress.line1"));
			a.setName2(par.getFirst("deliveryAddress.line2"));
			a.setName3(par.getFirst("deliveryAddress.line3"));
			a.setStreet(par.getFirst("deliveryAddress.street"));
			a.setPostalcode(par.getFirst("deliveryAddress.postalcode"));
			a.setCity(par.getFirst("deliveryAddress.city"));
		}
		dao.insertSubscription(s);
		jpaSubscriptionDao.create(s);
		recordUserChange(dao, "master", s.getId(), "subscription", null, null, "N");
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
		if (par.containsKey("period") && par.getFirst("period").isEmpty() == false) {
			try {
				p.setPeriod(Period.months(Integer.parseInt(par.getFirst("period"))));
			} catch (NumberFormatException e) {
				p.setPeriod(Period.months(1));
			}
		} else {
			p.setPeriod(Period.months(1));
		}
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
		long pid = dao.insertSubscrProduct(p);
		jpaSubscrProductDao.create(p);
		recordUserChange(dao, "master", pid, "subscrProduct", null, null, "N");
		SubscrArticle art = p.createNextArticle(LocalDate.now());
		art.setProductId(pid);
		long aid = dao.insertArticle(art);
		jpaSubscrArticleDao.create(art);
		recordUserChange(dao, "master", aid, "subscrArticle", null, null, "N");
		return new SubscrProductDetailView(dao, p, Collections.emptyList());
	}

	@GET
	@Path("/createCollInvoice/{sub}")
	@Produces({"application/json"})
	public PosInvoice createCollInvoice(@PathParam("sub") String subIdP) {
		long subId = Long.parseLong(subIdP);
		PosInvoice inv = SubscriptionInvoiceCreator.createCollectiveSubscription(dao, dao.getSubscriber(subId), numGen);
//		recordUserChange(dao, "master", inv.getId(), "collInvoice " + inv.getNumber(), null, null, "N");
		return inv;
	}

	@Produces({"application/pdf"})
	@GET
	@Path("/pdfcreateCollInvoice/{sub}")
	public Response createCollPdfInvoice(@PathParam("sub") String subIdP)  {
		PosInvoice inv = createCollInvoice(subIdP); 
		return invoiceResponse(inv);
	}

	@GET
	@Path("/deliverycreate/{sub}/{art}/{date}")
	@Produces({"application/json"})
	@UnitOfWork
	public SubscrDelivery createDelivery(@PathParam("sub") String subIdP,@PathParam("art") String artIdP,@PathParam("date") String dateP ) {
		long subId = Long.parseLong(subIdP);
		long artId = Long.parseLong(artIdP);
		LocalDate deliveryDate = new DateParam(dateP).getDate();
		Subscription subscription = dao.getSubscription(subId);
		SubscrArticle article = dao.getSubscrArticle(artId);
		SubscrDelivery d = new SubscrDelivery();
		SubscrProduct p = dao.getSubscrProduct(subscription.getProductId());
		Subscriber subscriber  = dao.getSubscriber(subscription.getSubscriberId());
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
		d.setCreationDate(DateTime.now());
		d.setPayed(p.isPayPerDelivery() == false);
		d.setSlipped(subscriber.isNeedsDeliveryNote() == false);

		dao.insertDelivery(d);
		jpaSubscrDeliveryDao.create(d);
		recordUserChange(dao, "master", d.getId(), "subscrDelivery", null, null, "N");
		p.setLastDelivery(deliveryDate);
		if (p.getPeriod() != null) {
			p.setNextDelivery(deliveryDate.plus(p.getPeriod()));
		}
		dao.updateSubscrProduct(p);
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
		LocalDate deliveryDate = new DateParam(dateP).getDate();
		Subscription subscription = dao.getSubscription(subId);
		SubscrInterval article = dao.getSubscrInterval(artId);
		SubscrIntervalDelivery d = new SubscrIntervalDelivery();
		d.setIntervalName(article.getName());
		d.setDeliveryDate(deliveryDate);
		d.setSubscriptionId(subscription.getId());
		d.setSubscriberId(subscription.getSubscriberId());
		d.setIntervalId(article.getId());
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

		dao.insertIntervalDelivery(d);
		jpaSubscrIntervalDeliveryDao.create(d);
		recordUserChange(dao, "master", d.getId(), "subscrIntervalDelivery", null, null, "N");
		return d;
	}

	@GET
	@Path("/createInvoice/{sub}")
	@Produces({"application/json"})
	public PosInvoice createInvoice(@PathParam("sub") String subIdP) {
		long subId = Long.parseLong(subIdP);
		PosInvoice inv =  SubscriptionInvoiceCreator.createSubscription(dao, dao.getSubscription(subId), numGen);
		return inv;
	}

	@GET
	@Path("/pdfcreateInvoice/{sub}")
	@Produces({"application/pdf"})
	public Response createPdfInvoice(@PathParam("sub") String subIdP)  {
		PosInvoice inv = createInvoice(subIdP); 
		return invoiceResponse(inv);
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
		SubscrArticle art = product.createNextArticle(LocalDate.now());
		dao.insertArticle(art);
		jpaSubscrArticleDao.create(art);
		dao.updateSubscrProduct(product);
		jpaSubscrProductDao.update(product);
		recordUserChange(dao, "master", art.getId(), "subscrArticle", null, null, "N");
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
		SubscrInterval art = product.createNextInterval(LocalDate.now());
		dao.insertInterval(art);
		jpaSubscrIntervalDao.create(art);
		dao.updateSubscrProduct(product);
		jpaSubscrProductDao.update(product);
		recordUserChange(dao, "master", art.getId(), "subscrInterval", null, null, "N");
		return art;
	}

	@GET
	@Path("/deliverydelete/{id}")
	@Produces({"text/html"})
	@UnitOfWork
	public View deleteDelivery(@PathParam("id") String deliIdP) {
		long delId = Long.parseLong(deliIdP);
		SubscrDelivery del = dao.getSubscrDelivery(delId);
		Subscription s = dao.getSubscription(del.getSubscriptionId());
		SubscrProduct p = dao.getSubscrProduct(s.getProductId());
		if (p.getPeriod() != null) {
			p.setLastDelivery(p.getLastDelivery() != null ? p.getLastDelivery().minus(p.getPeriod()) : null);
			p.setNextDelivery(p.getNextDelivery() != null ? p.getNextDelivery().minus(p.getPeriod()) : null);
		} else {
			p.setLastDelivery(null);
		}
		dao.updateSubscrProduct(p);
		jpaSubscrProductDao.update(p);
		dao.deleteDelivery(delId);
		jpaSubscrDeliveryDao.delete(del);
		recordUserChange(dao, "master", delId, "subscrDelivery", null, null, "D");
		return new SubscrDashboardView(dao, LocalDate.now());
	}

	@GET
	@Path("/intervaldeliverydelete/{id}")
	@Produces({"text/html"})
	@UnitOfWork
	public View deleteIntervalDelivery(@PathParam("id") String deliIdP) {
		long delId = Long.parseLong(deliIdP);
		dao.deleteIntervalDelivery(delId);
		SubscrIntervalDelivery del = jpaSubscrIntervalDeliveryDao.findById(delId);
		jpaSubscrIntervalDeliveryDao.delete(del);
		recordUserChange(dao, "master", delId, "subscrIntervalDelivery", null, null, "D");
		return new SubscrDashboardView(dao, LocalDate.now());
	}

	@GET
	@Path("/invoiceRecord/{inv}")
	@Produces({"text/html"})
	@UnitOfWork
	public View fakturiereInvoice(@PathParam("inv") String invNum) {
		PosInvoice inv = dao.getTempInvoice(invNum);
		if (inv == null) {
			throw new WebApplicationException("unable to faktura, no temp invoice with this number " + invNum, 500);
		}
		SubscriptionInvoiceCreator.fakturiereInvoice(dao, invDao, inv);
//		Long k = inv.getId();
		List<PosInvoice> x = jpaDao.findByNumber(inv.getNumber());
		if (x.isEmpty()) {
			jpaDao.create(inv);
		} else {
			inv.setId(x.get(0).getId());
			jpaDao.update(inv);
		}
//		inv.setId(k);
//		recordUserChange(dao, "master", inv.getId(), "invoice", null, null, "F");
		return new InvoicesView(dao, invDao);
	}

	@GET
	@Path("/invoiceView/{inv}")
	@Produces({"application/pdf"})
	public Response viewInvoice(@PathParam("inv") String invNum) {
		PosInvoice inv = dao.getTempInvoice(invNum);
		if (inv == null) {
			List<PosInvoice> invs = invDao.fetch(invNum);
			if (invs.size()>1 || invs.isEmpty()) {
				throw new WebApplicationException("unable to cancel, more than one or no invoice with this number " + invNum, 500);
			}
			inv = invs.get(0);
		}
		return invoiceResponse(inv);
	}

	
	private Response invoiceResponse(PosInvoice inv) {
		final PosInvoice invs = inv;
		StreamingOutput stream = new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					PDFInvoice generator = new PDFInvoice(invs);
					generator.generatePDF(output);
				} catch (Exception e) {
					e.printStackTrace();
					throw new WebApplicationException(e);
				}
				output.flush();
			}
		};	
		return Response.ok(stream).header("Content-Disposition","attachment; filename=Rechnung_" + inv.getNumber() + ".pdf").build();
	}
	
	
	@GET
	@Path("/invoiceCancel/{inv}")
	@Produces({"text/html"})
	@UnitOfWork
	public View cancelInvoice(@PathParam("inv") String invNum) {
		PosInvoice inv = dao.getTempInvoice(invNum);
		if (inv != null) {
			// es ist noch eine temporäre, einfach löschen
			dao.deleteTempInvoice(invNum);
			SubscriptionInvoiceCreator.cancelInvoice(dao, inv);
		} else {
			// es ist eine permanente, auf gecanncelled setzen
			List<PosInvoice> invs = invDao.fetch(invNum);
			if (invs.size()>1 || invs.isEmpty()) {
				throw new WebApplicationException("unable to cancel, more than one or no invoice with this number " + invNum, 500);
			}
			inv = invs.get(0);
			inv.setCancelled(true);
			invDao.updateInvoice(inv);
			SubscriptionInvoiceCreator.cancelInvoice(dao, inv);
			List<PosInvoice> x = jpaDao.findByNumber(invNum);
			if (x.isEmpty() == false ) {
				x.get(0).setCancelled(true);
				jpaDao.update(x.get(0));
			}
		}
		// TODO - stornorechnung erzeugen
//		recordUserChange(dao, "master", inv.getId(), "invoice", null, null, "C");
		return new InvoicesView(dao, invDao);
	}

	@GET
	@Path("/querycustomers")
	@Produces({"application/json"})
	public List<Subscriber> querySubscribers(@QueryParam("q") Optional<String> query) {
		if (query.isPresent() && query.get().isEmpty() == false) {
			return dao.querySubscribers("%" + query.get() + "%");
		}
		return dao.getSubscribers();
	}

	@GET
	@Path("/queryproduct")
	@Produces({"application/json"})
	public List<SubscrProduct> querySubscrProducts(@QueryParam("q") Optional<String> query) {
		if (query.isPresent() && query.get().isEmpty() == false) {
			return dao.querySubscrProducts("%" + query.get() + "%");
		}
		return dao.getSubscrProducts();
	}

	@GET
	@Path("/customerCreateForm")
	@Produces({"text/html"})
	public View showCustomerAddForm() {
		return new CustomerAddView(dao);
	}

	@GET
	@Path("/customers")
	@Produces({"text/html"})
	public View showCustomers() {
		return new SubscrCustomerView(dao);
	}

	@GET
	@Path("/invoices")
	@Produces({"text/html"})
	public View showInvoices() {
		return new InvoicesView(dao, invDao);
	}

	@GET
	@Path("/dashboard")
	@Produces({"text/html"})
	public View showDashboard() {
		return new SubscrDashboardView(dao, LocalDate.now());
	}

	@GET
	@Path("/delivery/{deliv}")
	@Produces({"text/html"})
	public View showDelivery(@PathParam("deliv") String delivery) {
		long delId = Long.parseLong(delivery);
		return new SubscrDeliveryView(dao, dao.getSubscrDelivery(delId));
	}

	@GET
	@Path("/intervaldelivery/{deliv}")
	@Produces({"text/html"})
	public View showIntervalDelivery(@PathParam("deliv") String delivery) {
		long delId = Long.parseLong(delivery);
		return new SubscrIntervalDeliveryView(dao, dao.getSubscrIntervalDelivery(delId));
	}

	@GET
	@Path("/deliveraddresslist")
	@Produces({"application/pdf"})
	public Response showDeliveryAdresses(@QueryParam("date") Optional<String> dateP) {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				try {
					LocalDate date = new DateParam(dateP.orNull()).getDate();
					ReportDeliveryProtocol rep = ReportDeliveryProtocolCreator.create(dao, date);
					PDFReport generator = new PDFReport(rep, "report/deliveryProtocol.xsl");
					generator.generatePDF(os);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
				os.flush();
			}
		};
		return Response.ok(stream).build();
	}

	@GET
	@Path("/deliverynote/{id}")
	@Produces({"application/pdf"})
	public Response showDeliveryNote(@PathParam("id") String deliveryId) {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				try {
					long artId = Long.parseLong(deliveryId);
					SubscrDelivery deli = dao.getSubscrDelivery(artId);
					ReportDeliveryNote rep = ReportDeliveryNoteCreator.create(dao, numGen,  artId);
					List<SubscrDelivery> deliveries = dao.getDeliveriesForSubscriberSlipflag(deli.getSubscriberId(), deli.getDeliveryDate(), false);
					dao.recordDetailsOnSlip(deliveries.stream().map(SubscrDelivery::getId).collect(Collectors.toList()), "neu");
					PDFReport generator = new PDFReport(rep, "report/deliveryNote.xsl");
					generator.generatePDF(os);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
				os.flush();
			}
		};
		return Response.ok(stream).build();
	}

	@GET
	@Path("/dispo/{prod}")
	@Produces({"text/html"})
	public View showArticleDispo(@PathParam("prod") String product, @QueryParam("date") Optional<String> dateP, @QueryParam("artid") Optional<String> artIdP) {
		long productId = Long.parseLong(product);
		LocalDate from = new DateParam(dateP.orNull()).getDate();
		SubscrProduct prod = dao.getSubscrProduct(productId );
		SubscrArticle art = null;
		if (artIdP.isPresent()) {
			art = dao.getSubscrArticle(Long.parseLong(artIdP.get()));
		} else {
			art = dao.getNewestArticleOfProduct(productId);
			if (art == null) {
				art = createNewArticle(prod);
			}
		}
		return new SubscrDispoView(dao, prod, art, from);
	}

	@GET
	@Path("/intervaldispo/{prod}")
	@Produces({"text/html"})
	public View showIntervalDispo(@PathParam("prod") String product, @QueryParam("date") Optional<String> dateP, @QueryParam("artid") Optional<String> artIdP) {
		long productId = Long.parseLong(product);
		LocalDate from = new DateParam(dateP.orNull()).getDate();
		SubscrProduct prod = dao.getSubscrProduct(productId );
		SubscrInterval art = null;
		if (artIdP.isPresent()) {
			art = dao.getSubscrInterval(Long.parseLong(artIdP.get()));
		} else {
			art = dao.getNewestIntervalOfProduct(productId);
			if (art == null) {
				art = createNewInterval(prod);
			}
		}
		return new SubscrIntervalDispoView(dao, prod, art, from);
	}

	@GET
	@Path("/disponav/{prod}/{dir}/{art}")
	@Produces({"text/html"})
	public View dispoArticleNav(@PathParam("prod") String prodIdP, @PathParam("dir") String dir,  @PathParam("art") String artIdP) {
		long artId = Long.parseLong(artIdP);
		long prodId = Long.parseLong(prodIdP);
		LocalDate from = LocalDate.now();
		SubscrProduct prod = dao.getSubscrProduct(prodId );
		List<SubscrArticle> arts = dao.getArticlesOfProduct(prodId);
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
	public View dispoIntervalNav(@PathParam("prod") String prodIdP, @PathParam("dir") String dir,  @PathParam("art") String artIdP) {
		long artId = Long.parseLong(artIdP);
		long prodId = Long.parseLong(prodIdP);
		LocalDate from = LocalDate.now();
		SubscrProduct prod = dao.getSubscrProduct(prodId );
		List<SubscrInterval> arts = dao.getIntervalsOfProduct(prodId);
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
	public View showProduct(@PathParam("prod") String product) {
		long productId = Long.parseLong(product);
		return new SubscrProductDetailView(dao, dao.getSubscrProduct(productId ), dao.getSubscriptionsForProduct(productId));
	}
	
	@GET
	@Path("/productCreateForm")
	@Produces({"text/html"})
	public View showProductAddForm() {
		return new ProductAddView(dao);
	}

	@GET
	@Path("/products")
	@Produces({"text/html"})
	public View showProducts() {
		return new SubscrProductsView(dao, dao.getSubscrProducts());
	}


	@GET
	@Path("/subscriber/{sub}")
	@Produces({"text/html"})
	public View showSubscriber(@PathParam("sub") String subdIdP) {
		long subId = Long.parseLong(subdIdP);
		return new SubscriberDetailView(dao, invDao, dao.getSubscriber(subId), sessFact);
	}

	@GET
	@Path("/subscription/{sub}")
	@Produces({"text/html"})
	public View showSubscription(@PathParam("sub") String subdIdP) {
		long subId = Long.parseLong(subdIdP);
		return new SubscriptionDetailView(dao, dao.getSubscription(subId), sessFact);
	}
	
	@GET
	@Path("/subscriptionCreateForm")
	@Produces({"text/html"})
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
			res = new IssueSlipUpdateHelper(invDao).update(pk, fieldname, value);
		}
		if (res == null) {
			res = new UpdateResult();
			res.success = false;
			res.msg =" not implemented yet";
		} else {
			recordUserChange(dao, "master", Long.parseLong(pk), fieldname, res.oldValue, res.newValue, "U");
		}
		return res;
	}

	
	
}
