package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.api.bofc.AccrualWeek;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.core.WeekBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.view.WeekView;

import org.joda.time.DateTime;

import com.google.inject.Inject;

@Path("/accrualweek")
public class AccrualWeekResource {
	private PosCashBalanceDAO balanceDao;
	
	@Inject
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
	
	/**
	 * für den store2
	 * @param date
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PosCashBalance> fetchBalancesOfWeek(@QueryParam("week") String week)  {
		return fetchAccrualWeekForDate(week).getAllBalances();
	}

	@Produces({"text/html"})
	@GET
	@Path("/view/{date}")
	public View fetchViewForDate(@PathParam("date") String date)  {
      	AccrualWeek aw = fetchAccrualWeekForDate(date);
		return new WeekView(aw);
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
		Integer weekNum = Integer.parseInt(date);
		DateTime week = new DateTime().weekOfWeekyear().setCopy(weekNum);
		DateTime startOfWeek = week.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 1 (Montag)
		DateTime endOfWeek = week.hourOfDay().setCopy(0).dayOfWeek().setCopy(7); // stunde 0 und wochentag 7 (Sonntag)
		WeekBalance balComp = new WeekBalance(balanceDao);
		AccrualWeek bal = balComp.computeBalance(startOfWeek, endOfWeek);
		return bal;
	}

}
