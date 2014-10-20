package net.buchlese.posa.resources;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.core.AccountingExport;
import net.buchlese.posa.core.CashBalance;
import net.buchlese.posa.core.PDFCashBalance;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

@Path("/cashbalance")
@Produces(MediaType.APPLICATION_JSON)
public class PosCashBalanceResource {

	private final PosCashBalanceDAO dao;
	private final PosTicketDAO ticketDao;
	private final PosTxDAO txDao;

	public PosCashBalanceResource(PosCashBalanceDAO dao,PosTicketDAO ticketdao, PosTxDAO txdao) {
		super();
		this.dao = dao;
		this.ticketDao = ticketdao;
		this.txDao = txdao;
	}

	private static String IDFORMAT = "yyyyMMdd";
	
	@GET
	public List<PosCashBalance> fetchAll(@QueryParam("date") Optional<String> date)  {
		String from = date.or(new DateTime().minusMonths(1).toString(IDFORMAT)); // wenn nix da ist, die von vor einem Monat
		Optional<String> till = Optional.absent();
		if (date.isPresent()) {
			if (date.get().equals("thisweek")) {
				from = new DateTime().dayOfWeek().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().dayOfWeek().withMaximumValue().toString(IDFORMAT));
			}
			if (date.get().equals("lastweek")) {
				from = new DateTime().minusWeeks(1).dayOfWeek().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().minusWeeks(1).dayOfWeek().withMaximumValue().toString(IDFORMAT));
			}
			if (date.get().equals("thismonth")) {
				from = new DateTime().dayOfMonth().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().dayOfMonth().withMaximumValue().toString(IDFORMAT));
			}
			if (date.get().equals("lastmonth")) {
				from = new DateTime().minusMonths(1).dayOfMonth().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().minusMonths(1).dayOfMonth().withMaximumValue().toString(IDFORMAT));
			}
			if (date.get().equals("thisquarter")) {
				int quarter = (new DateTime().getMonthOfYear() / 3);
				from = new DateTime().monthOfYear().setCopy(quarter*3).dayOfMonth().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().monthOfYear().setCopy(quarter*3+2).dayOfMonth().withMaximumValue().toString(IDFORMAT));
			}
			if (date.get().equals("lastquarter")) {
				int quarter = (new DateTime().getMonthOfYear() / 3);
				if (quarter >= 1) {
					quarter--;
					from = new DateTime().monthOfYear().setCopy(quarter*3).dayOfMonth().withMinimumValue().toString(IDFORMAT);
					till = Optional.of(new DateTime().monthOfYear().setCopy(quarter*3+2).dayOfMonth().withMaximumValue().toString(IDFORMAT));
				} else {
					int year = new DateTime().getYear();
					from = new DateTime(year-1,10,1,0,0).toString(IDFORMAT);
					till = Optional.of(new DateTime(year-1,12,31,23,0).toString(IDFORMAT));
				}
			}
			if (date.get().equals("thisyear")) {
				from = new DateTime().dayOfYear().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().toString(IDFORMAT));
			}
			if (date.get().equals("lastyear")) {
				from = new DateTime().minusYears(1).dayOfYear().withMinimumValue().toString(IDFORMAT);
				till = Optional.of(new DateTime().minusYears(1).dayOfYear().withMaximumValue().toString(IDFORMAT));
			}
		}
		return dao.fetchAllAfter(from, till);
	}

	@GET
	@Path("/notexported")
	public List<PosCashBalance> fetchNotExported()  {
		return dao.fetchNotExported();
	}

	@GET
	@Path("/fibuexport")
	@Produces(MediaType.TEXT_PLAIN)
	public Response  fibuExport()  {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(os));
				List<PosCashBalance> bals = dao.fetchNotExported();
				for (PosCashBalance bal : bals) {
					String buchung = AccountingExport.accountingExport(bal);
					System.out.println(buchung);
					writer.write(buchung);
				}
				writer.flush();
			}
		};
		return Response.ok(stream).build();		
	}

	@GET
	@Path("/{date}")
	public PosCashBalance fetchForDate(@PathParam("date") String date)  {
		PosCashBalance bal = fetchBalanceForDate(date);
		return bal;
	}

	@Produces({"application/pdf"})
	@GET
	@Path("/pdf/{date}")
	public Response fetchPdfForDate(@PathParam("date") String date)  {
		byte[] pdf;
		try {
			pdf = PDFCashBalance.create(fetchBalanceForDate(date));
			return Response.ok(pdf, "application/pdf").build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	private PosCashBalance fetchBalanceForDate(String date) {
		if ("today".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0); // stunde 0
			CashBalance balCOmp = new CashBalance(txDao, ticketDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today);
			return bal;
		}
		if ("thisweek".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 0
			CashBalance balCOmp = new CashBalance(txDao, ticketDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today);
			return bal;
		}
		if ("lastweek".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime().minusWeeks(1).hourOfDay().setCopy(23).dayOfWeek().setCopy(7); // letzter tag und letzte stunde
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 1
			CashBalance balCOmp = new CashBalance(txDao, ticketDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today);
			return bal;
		}
		if ("thismonth".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfMonth().setCopy(1); // stunde 0 und monats-ersten
			CashBalance balCOmp = new CashBalance(txDao, ticketDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today);
			return bal;
		}
		PosCashBalance bal = dao.fetchForDate(date);
		return bal;
	}



}
