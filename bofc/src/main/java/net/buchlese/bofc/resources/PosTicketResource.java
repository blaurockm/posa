package net.buchlese.bofc.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosTicket;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosTicketDAO;

import org.joda.time.DateTime;

@Path("/ticket")
@Produces(MediaType.APPLICATION_JSON)
public class PosTicketResource {

	private final PosTicketDAO dao;
	private final PosCashBalanceDAO balDao;

	public PosTicketResource(PosTicketDAO dao, PosCashBalanceDAO baldao) {
		super();
		this.dao = dao;
		this.balDao = baldao;
	}
	
	@GET
	public List<PosTicket> fetchAll(@QueryParam("date") String date) {
		if ("today".equals(date)) {
			DateTime refdate = new DateTime();
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("yesterday".equals(date)) {
			DateTime refdate = new DateTime().minusDays(1);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("preyesterday".equals(date)) {
			DateTime refdate = new DateTime().minusDays(2);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("prepreyesterday".equals(date)) {
			DateTime refdate = new DateTime().minusDays(3);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("preprepreyesterday".equals(date)) {
			DateTime refdate = new DateTime().minusDays(4);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("todayLastWeek".equals(date)) {
			DateTime refdate = new DateTime().minusDays(8);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("yesterdayLastWeek".equals(date)) {
			DateTime refdate = new DateTime().minusDays(9);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		PosCashBalance bal = balDao.fetchForDate(date);
		
		if (bal != null) {
			return dao.fetch(bal.getFirstCovered(), bal.getLastCovered());
		}
		return dao.fetch(new DateTime().minusMonths(2), new DateTime());
	}
}