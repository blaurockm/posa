package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.buchlese.bofc.api.bofc.AccrualDay;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.core.DayBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.view.DayView;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

@Path("/day")
public class AccrualDayResource {
	private PosCashBalanceDAO balanceDao;
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(AccrualDayResource.class);

	@Inject
	public AccrualDayResource(PosCashBalanceDAO balanceDao) {
		super();
		this.balanceDao = balanceDao;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{date}")
	public AccrualDay fetchAll(@PathParam("date") String date)  {
      	return fetchAccruaDayForDate(date);
	}

	@Produces({"text/html"})
	@GET
	@Path("/view/{date}")
	public View fetchViewForDate(@PathParam("date") String date)  {
		AccrualDay aw = fetchAccruaDayForDate(date);
		return new DayView(aw);
	}
	
	@POST
	@Path("/accept")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptBalance(PosCashBalance cashBal)  {
		try {
			getDayBalance().accept(cashBal);
			return Response.ok().build();
		} catch (Throwable t) {
			log.error("problem accepting cashBalance for Day" + cashBal, t);
			return Response.serverError().entity(t.getMessage()).build();
		}
	}

	private AccrualDay fetchAccruaDayForDate(String date) {
		if (date == null || date.isEmpty() || "today".equals(date)) {
			return getDayBalance().computeBalance(null);
		}
		return getDayBalance().computeBalance(date);
	}
	
	private DayBalance balComp;
	 private DayBalance getDayBalance() {
		 if (balComp == null) {
			 balComp = new DayBalance(balanceDao);
		 }
		 return balComp;
	 }
}
