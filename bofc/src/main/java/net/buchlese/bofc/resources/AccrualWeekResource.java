package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.api.bofc.AccrualWeek;
import net.buchlese.bofc.core.WeekBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.view.WeekView;

import org.joda.time.DateTime;

@Path("/accrualweek")
public class AccrualWeekResource {
	private PosCashBalanceDAO balanceDao;
	
	
	public AccrualWeekResource(PosCashBalanceDAO balanceDao) {
		super();
		this.balanceDao = balanceDao;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{date}")
	public AccrualWeek fetchAll(@PathParam("date") String date)  {
      	return fetchAccrualWeekForDate(date);
	}

	@Produces({"text/html"})
	@GET
	@Path("/view/{date}")
	public View fetchViewForDate(@PathParam("date") String date)  {
      	AccrualWeek aw = fetchAccrualWeekForDate(date);
		return new WeekView(aw, balanceDao);
	}
	
	private AccrualWeek fetchAccrualWeekForDate(String date) {
		if ("thisweek".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 0
			WeekBalance balComp = new WeekBalance(balanceDao);
			AccrualWeek bal = balComp.computeBalance(startOfToday, today);
			return bal;
		}
		if ("lastweek".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime().minusWeeks(1).hourOfDay().setCopy(23).dayOfWeek().setCopy(7); // letzter tag und letzte stunde
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 1
			WeekBalance balComp = new WeekBalance(balanceDao);
			AccrualWeek bal = balComp.computeBalance(startOfToday, today);
			return bal;
		}
		if ("thismonth".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfMonth().setCopy(1); // stunde 0 und monats-ersten
			WeekBalance balComp = new WeekBalance(balanceDao);
			AccrualWeek bal = balComp.computeBalance(startOfToday, today);
			return bal;
		}
		return null;
	}

}
