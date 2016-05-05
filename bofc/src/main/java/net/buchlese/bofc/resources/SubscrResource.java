package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.util.Collections;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.buchlese.bofc.api.bofc.Mapping;
import net.buchlese.bofc.jdbi.SubscrTestDataDAO;
import net.buchlese.bofc.view.SubscrDashboardView;
import net.buchlese.bofc.view.pages.MappingView;
import net.buchlese.bofc.view.subscr.SubscrProductDetailView;

import org.joda.time.LocalDate;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/subscr")
public class SubscrResource {

	private static SubscrTestDataDAO dao = new SubscrTestDataDAO();
	
	@Inject
	public SubscrResource() {
		super();
	}

	@POST
	@Path("/update")
	@Produces({"text/html"})
	public View updateMaping( @FormParam("point") Integer pi, @FormParam("deb") Integer deb, @FormParam("cust") Integer cust) {
		Mapping um = new Mapping();
		um.setPointid(pi);
		um.setCustomerId(cust);
		um.setDebitorId(deb);
		return new MappingView(Collections.emptyList());
	}


	@GET
	@Produces({"text/html"})
	public View showDashboard() {
		return new SubscrDashboardView(dao, dao.getSubscrProducts(), dao.getDeliveries(LocalDate.now()));
	}
	
	@GET
	@Path("/dispo/{prod}")
	@Produces({"text/html"})
	public View showDispo(@PathParam("prod") String product, @QueryParam("date") Optional<String> date) {
		return null;
	}

	@GET
	@Path("/delivery/{deliv}")
	@Produces({"text/html"})
	public View showDelivery(@PathParam("deliv") String delivery) {
		return null;
	}

	@GET
	@Path("/product/{prod}")
	@Produces({"text/html"})
	public View showProduct(@PathParam("prod") String product) {
		long productId = Long.parseLong(product);
		return new SubscrProductDetailView(dao, dao.getSubscrProduct(productId ), dao.getSubscriptionsForProduct(productId));
	}

	// missing Subscription
	// missing Subscriber
	
	@GET
	@Path("/subscrinvoice/{id}")
	@Produces({"text/html"})
	public View showSubscriptionInvoice(@PathParam("id") String invId) {
		return null;
	}
	
	@GET
	@Path("/deliverynote/{id}")
	@Produces({"text/html"})
	public View showDeliveryNote(@PathParam("id") String noteId) {
		return null;
	}
}
