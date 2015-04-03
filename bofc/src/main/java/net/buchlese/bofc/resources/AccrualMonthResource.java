package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.api.bofc.AccrualMonth;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.core.MonthBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.view.MonthView;

import org.joda.time.DateTime;

@Path("/accrualmonth")
public class AccrualMonthResource {
	private PosCashBalanceDAO balanceDao;

	public AccrualMonthResource(PosCashBalanceDAO balanceDao) {
		super();
		this.balanceDao = balanceDao;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{date}")
	public AccrualMonth fetchAll(@PathParam("date") String date)  {
      	return fetchAccrualMonthForDate(date);
	}
	
	/**
	 * für den store2
	 * @param date
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PosCashBalance> fetchBalancesOfWeek(@QueryParam("week") String week)  {
		return fetchAccrualMonthForDate(week).getAllBalances();
	}

	@Produces({"text/html"})
	@GET
	@Path("/view/{date}")
	public View fetchViewForDate(@PathParam("date") String date)  {
      	AccrualMonth aw = fetchAccrualMonthForDate(date);
		return new MonthView(aw);
	}

	private AccrualMonth fetchAccrualMonthForDate(String date) {
		if ("thismonth".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfMonth().withMinimumValue(); // stunde 0 und wochentag 0
			MonthBalance balComp = new MonthBalance(balanceDao);
			AccrualMonth bal = balComp.computeBalance(startOfToday, today);
			return bal;
		}
		if ("lastmonth".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime().minusMonths(1).hourOfDay().setCopy(23).dayOfMonth().withMaximumValue(); // letzter tag und letzte stunde
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfMonth().withMinimumValue(); // stunde 0 und wochentag 1
			MonthBalance balComp = new MonthBalance(balanceDao);
			AccrualMonth bal = balComp.computeBalance(startOfToday, today);
			return bal;
		}
		Integer monthNum = Integer.parseInt(date);
		DateTime month = new DateTime().monthOfYear().setCopy(monthNum);
		DateTime startOfMonth = month.hourOfDay().setCopy(0).dayOfMonth().withMinimumValue(); // stunde 0 und wochentag 1 (Montag)
		DateTime endOfMonth = month.hourOfDay().setCopy(0).dayOfMonth().withMaximumValue(); // stunde 0 und wochentag 7 (Sonntag)
		MonthBalance balComp = new MonthBalance(balanceDao);
		AccrualMonth bal = balComp.computeBalance(startOfMonth, endOfMonth);
		return bal;
	}

}
