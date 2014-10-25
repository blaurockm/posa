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
		if (date.isPresent()) {
			return fetchBalancesForDate(date.get());
		}
		return dao.fetchAllAfter(new DateTime().minusMonths(1).toString(IDFORMAT), Optional.absent());
	}

	@GET
	@Path("/fibuexport/{date}")
	@Produces(MediaType.TEXT_PLAIN)
	public StreamingOutput  fibuExport(@PathParam("date") String date)  {
		return new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(os));
				for (PosCashBalance bal : fetchBalancesForDate(date)) {
					writer.write(AccountingExport.accountingExport(bal));
				}
				writer.flush();
			}
		};
	}

	@GET
	@Path("/{date}")
	public PosCashBalance fetchForDate(@PathParam("date") String date)  {
		return fetchBalanceForDate(date);
	}

	@Produces({"application/pdf"})
	@GET
	@Path("/pdf/{date}")
	public StreamingOutput fetchPdfForDate(@PathParam("date") String date)  {
	    return new StreamingOutput() {
	        public void write(OutputStream output) throws IOException, WebApplicationException {
	            try {
	                PDFCashBalance generator = new PDFCashBalance(fetchBalanceForDate(date));
	                generator.generatePDF(output);
	            } catch (Exception e) {
	                throw new WebApplicationException(e);
	            }
                output.flush();
	        }
	    };	
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

	private List<PosCashBalance> fetchBalancesForDate(String date) {
		String from = null;
		String till = null;
		if (date.equals("thisweek")) {
			from = new DateTime().dayOfWeek().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().dayOfWeek().withMaximumValue().toString(IDFORMAT);
		}
		if (date.equals("lastweek")) {
			from = new DateTime().minusWeeks(1).dayOfWeek().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().minusWeeks(1).dayOfWeek().withMaximumValue().toString(IDFORMAT);
		}
		if (date.equals("thismonth")) {
			from = new DateTime().dayOfMonth().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().dayOfMonth().withMaximumValue().toString(IDFORMAT);
		}
		if (date.equals("lastmonth")) {
			from = new DateTime().minusMonths(1).dayOfMonth().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().minusMonths(1).dayOfMonth().withMaximumValue().toString(IDFORMAT);
		}
		if (date.equals("thisquarter")) {
			int quarter = (new DateTime().getMonthOfYear() / 3);
			from = new DateTime().monthOfYear().setCopy(quarter*3).dayOfMonth().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().monthOfYear().setCopy(quarter*3+2).dayOfMonth().withMaximumValue().toString(IDFORMAT);
		}
		if (date.equals("lastquarter")) {
			int quarter = (new DateTime().getMonthOfYear() / 3);
			if (quarter >= 1) {
				quarter--;
				from = new DateTime().monthOfYear().setCopy(quarter*3).dayOfMonth().withMinimumValue().toString(IDFORMAT);
				till = new DateTime().monthOfYear().setCopy(quarter*3+2).dayOfMonth().withMaximumValue().toString(IDFORMAT);
			} else {
				int year = new DateTime().getYear();
				from = new DateTime(year-1,10,1,0,0).toString(IDFORMAT);
				till = new DateTime(year-1,12,31,23,0).toString(IDFORMAT);
			}
		}
		if (date.equals("thisyear")) {
			from = new DateTime().dayOfYear().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().toString(IDFORMAT);
		}
		if (date.equals("lastyear")) {
			from = new DateTime().minusYears(1).dayOfYear().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().minusYears(1).dayOfYear().withMaximumValue().toString(IDFORMAT);
		}
		if (date.equals("notexported")) {
			return dao.fetchNotExported();
		}
		if (from == null) {
			from = new DateTime().minusWeeks(1).dayOfWeek().withMinimumValue().toString(IDFORMAT);
		}
		return dao.fetchAllAfter(from, Optional.fromNullable(till));
	}

}
