package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosTicket;
import net.buchlese.bofc.api.bofc.PosTx;
import net.buchlese.bofc.core.AccountingExportFactory;
import net.buchlese.bofc.core.AccountingExportFile;
import net.buchlese.bofc.core.CashBalance;
import net.buchlese.bofc.core.PDFCashBalance;
import net.buchlese.bofc.core.Validator;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosTicketDAO;
import net.buchlese.bofc.jdbi.bofc.PosTxDAO;
import net.buchlese.bofc.view.AccountingExportView;
import net.buchlese.bofc.view.CashBalView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/cashbalance")
public class PosCashBalanceResource {

	private final PosCashBalanceDAO dao;
	private final PosTicketDAO ticketDao;
	private final PosTxDAO txDao;

	@Inject
	public PosCashBalanceResource(PosCashBalanceDAO dao,PosTicketDAO ticketdao, PosTxDAO txdao) {
		super();
		this.dao = dao;
		this.ticketDao = ticketdao;
		this.txDao = txdao;
	}

	private static String IDFORMAT = "yyyyMMdd";
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(PosCashBalanceResource.class);

	@POST
	@Path("/acceptBalance")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptBalance(PosCashBalance cashBal)  {
		try {
			Long id = dao.getIdOfExistingBalance(cashBal.getAbschlussId(), cashBal.getPointid());
			List<PosTx> txs = Collections.emptyList();
			List<PosTicket> tickets = cashBal.getTickets() == null ? Collections.emptyList() : cashBal.getTickets();
			txs = tickets.stream().filter(p -> p.getTxs() != null).flatMap(t -> t.getTxs().stream()).collect(Collectors.toList());
			if (id != null) {
				cashBal.setId(id); // damit auch der richtige Abschluss aktualisiert wird
				dao.update(cashBal);
				ticketDao.deleteAll(cashBal.getFirstCovered(), cashBal.getLastCovered(), cashBal.getPointid());
				txDao.deleteAll(cashBal.getFirstCovered(), cashBal.getLastCovered(), cashBal.getPointid());
				ticketDao.insertAll(tickets.iterator());
				txDao.insertAll(txs.iterator());
			} else {
				dao.insert(cashBal);
				ticketDao.insertAll(tickets.iterator());
				txDao.insertAll(txs.iterator());
			}
			return Response.ok().build();
		} catch (Throwable t) {
			log.error("problem creating cashBalance" + cashBal, t);
			return Response.serverError().entity(t.getMessage()).build();
		}
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PosCashBalance> fetchAll(@QueryParam("date") Optional<String> date)  {
		if (date.isPresent()) {
			return fetchBalancesForDate(date.get());
		}
		return dao.fetchAllAfter(new DateTime().minusMonths(1).toString(IDFORMAT), new DateTime().toString(IDFORMAT));
	}

	@GET
	@Path("/complete/{date}")
	public PosCashBalance fetchCompleteForDate(@PathParam("date") String date)  {
		PosCashBalance bal = fetchBalanceForId(date);
		new CashBalance(ticketDao, txDao).amendTickets(bal);
		return bal;
	}

	@Produces({"text/html"})
	@GET
	@Path("/view/{date}")
	public View fetchViewForDate(@PathParam("date") String abschlussId)  {
		PosCashBalance cb = fetchBalanceForId(abschlussId);
		return new CashBalView(cb);
	}

	@GET
	@Path("/viewfibuexport")
	@Produces("text/html")
	public View create(@QueryParam("key") Integer key) {
		AccountingExport ae = AccountingExportFactory.getExport(key);
		if (ae != null) {
			return new AccountingExportView(ae);
		}
		throw new WebApplicationException("key not found");
	}

	@POST
	@Path("/fibuexport")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/html")
	public View create( @FormParam("from")String fromStr, @FormParam("till")String tillStr, @FormParam("kasse") List<Integer> kassen) {
		LocalDate from = new DateParam(fromStr).getDate();
		Optional<LocalDate> till = Optional.fromNullable(tillStr).transform(d -> new DateParam(d).getDate());
		for (Integer kasse : kassen) {
			return new AccountingExportView(AccountingExportFactory.createExport(kasse, from, till, dao));
		}
		throw new WebApplicationException("kasse not found");
	}

	@GET
	@Path("/fibuexportfile")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/csv; charset='iso-8859-1'")
	public Response createFile( @QueryParam("key") Integer key) {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(os, "iso-8859-1"));
				AccountingExport ae = AccountingExportFactory.getExport(key);
				try {
					AccountingExportFile.createFile(ae, writer);
				} catch (Exception e) {
					log.error("problem creating file for AccountingExport " + ae, e);
					writer.write("\n\n\nproblem creating cashBalance " + e.toString() + "\n\n\n\n");
				}
				writer.flush();
			}
		};
		return Response.ok(stream).header("Content-Disposition","attachment; filename=wochenliste.csv").build();
	}

	@Produces({"application/pdf"})
	@GET
	@Path("/pdf/{date}")
	public StreamingOutput fetchPdfForDate(@PathParam("date") String date)  {
		return new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					PDFCashBalance generator = new PDFCashBalance(fetchBalanceForId(date));
					generator.generatePDF(output);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
				output.flush();
			}
		};	
	}

	@Produces(MediaType.TEXT_PLAIN)
	@GET
	@Path("/extended/{date}")
	public StreamingOutput fetchDetailsForDate(@PathParam("date") String date)  {
		return new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				try {
					Validator generator = new Validator(fetchBalanceForId(date), ticketDao, txDao);
					generator.validateDetails(output);
				} catch (Exception e) {
					throw new WebApplicationException(e);
				}
				output.flush();
			}
		};	
	}

	private PosCashBalance fetchBalanceForId(String date) {
		if ("today".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0); // stunde 0
			CashBalance balCOmp = new CashBalance(ticketDao, txDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today,1);
			return bal;
		}
		if ("thisweek".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 0
			CashBalance balCOmp = new CashBalance(ticketDao, txDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today,1);
			return bal;
		}
		if ("lastweek".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime().minusWeeks(1).hourOfDay().setCopy(23).dayOfWeek().setCopy(7); // letzter tag und letzte stunde
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 1
			CashBalance balCOmp = new CashBalance(ticketDao, txDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today,1);
			return bal;
		}
		if ("thismonth".equals(date)) {
			// wir berechnen den von heute...
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0).dayOfMonth().setCopy(1); // stunde 0 und monats-ersten
			CashBalance balCOmp = new CashBalance(ticketDao, txDao);
			PosCashBalance bal = balCOmp.computeBalance(startOfToday, today,1);
			return bal;
		}
		PosCashBalance bal = dao.fetchForId(date);
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
		if (from == null) {
			from = new DateTime().minusWeeks(1).dayOfWeek().withMinimumValue().toString(IDFORMAT);
			till = new DateTime().toString(IDFORMAT);
		}
		return dao.fetchAllAfter(from, till);
	}

	private static class DateParam {
		private LocalDate date;
		public DateParam(String dateStr) {
			date = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(dateStr);
		}
		public LocalDate getDate() {
			return date;
		}
	}
}
