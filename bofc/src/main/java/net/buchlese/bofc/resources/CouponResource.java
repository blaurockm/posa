package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.UserChange;
import net.buchlese.bofc.api.coupon.Coupon;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.core.CouponInvoiceCreator;
import net.buchlese.bofc.core.NumberGenerator;
import net.buchlese.bofc.core.PDFInvoice;
import net.buchlese.bofc.jdbi.bofc.CouponDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.resources.helper.CouponUpdateHelper;
import net.buchlese.bofc.resources.helper.UpdateResult;
import net.buchlese.bofc.view.coupon.CouponAddView;
import net.buchlese.bofc.view.coupon.CouponCustomerDetailView;
import net.buchlese.bofc.view.coupon.CouponCustomerView;
import net.buchlese.bofc.view.coupon.CouponDashboardView;
import net.buchlese.bofc.view.coupon.CouponDetailView;
import net.buchlese.bofc.view.coupon.CouponsView;
import net.buchlese.bofc.view.subscr.NavigationView;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/coupon")
public class CouponResource {

	private final CouponDAO dao;

	private final SubscrDAO subscrDao;

	private final PosInvoiceDAO invDao;

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
		dao.insert(uc);
	}
	
	@Inject
	public CouponResource(PosInvoiceDAO invd, CouponDAO dao, SubscrDAO sdao, NumberGenerator g) {
		super();
		this.invDao = invd;
		this.dao = dao;
		this.numGen =g;
		this.subscrDao = sdao;
	}

	@POST
	@Path("/couponCreate")
	@Consumes("application/x-www-form-urlencoded")
	public View addCoupon(MultivaluedMap<String, String> par) {
		Coupon p = new Coupon();
		long pid = dao.insertCoupon(p);
		recordUserChange(subscrDao, "master", pid, "coupon", null, null, "N");
		return new CouponDetailView(p);
	}

	@GET
	@Path("/createInvoice/{sub}")
	@Produces({"application/json"})
	public PosInvoice createInvoice(@PathParam("sub") String subcriberIdP) {
		long subId = Long.parseLong(subcriberIdP);
		PosInvoice inv =  CouponInvoiceCreator.createInvoice(dao, dao.fetchAllCouponsForCustomer(subId, false), numGen);
		recordUserChange(subscrDao, "master", inv.getId(), "invoice " + inv.getNumber(), null, null, "N");
		return inv;
	}

	@GET
	@Path("/pdfcreateInvoice/{sub}")
	@Produces({"application/pdf"})
	public Response createPdfInvoice(@PathParam("sub") String subIdP)  {
		PosInvoice inv = createInvoice(subIdP); 
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
	@Path("/querycustomers")
	@Produces({"application/json"})
	public List<Subscriber> querySubscribers(@QueryParam("q") Optional<String> query) {
		if (query.isPresent() && query.get().isEmpty() == false) {
			return subscrDao.querySubscribers("%" + query.get() + "%");
		}
		return subscrDao.getSubscribers();
	}

	@GET
	@Path("/customers")
	@Produces({"text/html"})
	public View showCustomers() {
		return new CouponCustomerView(subscrDao);
	}

	@GET
	@Path("/dashboard")
	@Produces({"text/html"})
	public View showDashboard() {
		return new CouponDashboardView(dao, subscrDao, LocalDate.now());
	}
	
	@GET
	@Path("/navigation")
	public View showNavigation() {
		return new NavigationView();
	}

	@GET
	@Path("/coupon/{prod}")
	@Produces({"text/html"})
	public View showCoupon(@PathParam("prod") String coupIdStr) {
		long productId = Long.parseLong(coupIdStr);
		return new CouponDetailView(dao.fetchCoupon(productId ));
	}
	
	@GET
	@Path("/couponCreateForm")
	@Produces({"text/html"})
	public View showCouponAddForm() {
		return new CouponAddView(dao);
	}

	@GET
	@Path("/coupons")
	@Produces({"text/html"})
	public View showCoupons() {
		return new CouponsView(dao, dao.fetchCoupons());
	}


	@GET
	@Path("/customer/{sub}")
	@Produces({"text/html"})
	public View showCustomer(@PathParam("sub") String subdIdP) {
		long subId = Long.parseLong(subdIdP);
		return new CouponCustomerDetailView(dao, invDao, subscrDao.getSubscriber(subId));
	}
	
	@POST
	@Path("/update")
	@Produces({"application/json"})
	public UpdateResult updateMaping( @FormParam("pk") String pk, @FormParam("name") String fieldname, @FormParam("value") String value) {
		UpdateResult res = null;
		if (fieldname.startsWith("coupon")) {
			res = new CouponUpdateHelper(dao).update(pk, fieldname, value);
		}
		if (res == null) {
			res = new UpdateResult();
			res.success = false;
			res.msg =" not implemented yet";
		} else {
			recordUserChange(subscrDao, "master", Long.parseLong(pk), fieldname, res.oldValue, res.newValue, "U");
		}
		return res;
	}

	
	
}
