package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.bofc.api.bofc.Mapping;
import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.SubscrTestDataDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.SubscrDashboardView;
import net.buchlese.bofc.view.pages.MappingView;
import net.buchlese.bofc.view.subscr.SubscrDeliveryView;
import net.buchlese.bofc.view.subscr.SubscrDispoView;
import net.buchlese.bofc.view.subscr.SubscrProductDetailView;
import net.buchlese.bofc.view.subscr.SubscriptionDetailView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/subscr")
public class SubscrResource {

	private static SubscrDAO dao = new SubscrTestDataDAO();
	
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
	public View showDispo(@PathParam("prod") String product, @QueryParam("date") Optional<String> dateP, @QueryParam("artid") Optional<String> artIdP) {
		long productId = Long.parseLong(product);
		LocalDate from = new DateParam(dateP.orNull()).getDate();
		SubscrArticle art = null;
		if (artIdP.isPresent()) {
			art = dao.getSubscrArticle(Long.parseLong(artIdP.get()));
		}
		return new SubscrDispoView(dao, dao.getSubscrProduct(productId ), dao.getSubscriptionsForProduct(productId), art, from);
	}

	@GET
	@Path("/delivery/{deliv}")
	@Produces({"text/html"})
	public View showDelivery(@PathParam("deliv") String delivery) {
		long delId = Long.parseLong(delivery);
		return new SubscrDeliveryView(dao, dao.getSubscrDelivery(delId));
	}

	@GET
	@Path("/deliveraddresslist")
	@Produces({"text/plain"})
	public Response showDeliveryAdresses(@QueryParam("date") Optional<String> dateP) {
		final List<SubscrDelivery> deliveries = dao.getDeliveries(new DateParam(dateP.orNull()).getDate());
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(os, "iso-8859-1"));
				for (SubscrDelivery del : deliveries) {
					Subscription sub = dao.getSubscription(del.getSubcriptionId());
					if (sub.getDeliveryAddress() != null) {
						writeAddress(writer, sub.getDeliveryAddress(), sub.getDeliveryInfo1(), sub.getDeliveryInfo2());
					} else {
						Subscriber s = dao.getSubscriber(del.getSubscriberId());
						writeAddress(writer, s.getInvoiceAddress(), sub.getDeliveryInfo1(), sub.getDeliveryInfo2());
					}
					writer.write("\n\n");
				}
				writer.flush();
			}
			private void writeAddress(Writer w, Address a, String add1, String add2) throws IOException {
				w.write(a.getName1() + "\n");
				if (a.getName2() != null) {
					w.write(a.getName2() + "\n");
				}
				if (a.getName3() != null) {
					w.write(a.getName3() + "\n");
				}
				if (add1 != null) {
					w.write(add1 + "\n");
				}
				if (add2 != null) {
					w.write(add2 + "\n");
				}
				w.write(a.getStreet() + "\n");
				w.write(a.getPostalcode() + " " + a.getCity() + "\n");
			}
		};
//		return Response.ok(stream).header("Content-Disposition","attachment; filename=adressenliste.txt").build();
		return Response.ok(stream).build();
	}

	@GET
	@Path("/deliverycreate/{sub}/{art}/{date}")
	@Produces({"application/json"})
	public SubscrDelivery createDelivery(@PathParam("sub") String subIdP,@PathParam("art") String artIdP,@PathParam("date") String dateP ) {
		long subId = Long.parseLong(subIdP);
		long artId = Long.parseLong(artIdP);
		LocalDate d = new DateParam(dateP).getDate();
		return dao.createDelivery( dao.getSubscription(subId), dao.getSubscrArticle(artId), d);
	}

	@GET
	@Path("/product/{prod}")
	@Produces({"text/html"})
	public View showProduct(@PathParam("prod") String product) {
		long productId = Long.parseLong(product);
		return new SubscrProductDetailView(dao, dao.getSubscrProduct(productId ), dao.getSubscriptionsForProduct(productId));
	}

	@GET
	@Path("/subscription/{sub}")
	@Produces({"text/html"})
	public View showSubscription(@PathParam("sub") String subdIdP) {
		long subId = Long.parseLong(subdIdP);
		return new SubscriptionDetailView(dao, dao.getSubscription(subId));
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
	
}
