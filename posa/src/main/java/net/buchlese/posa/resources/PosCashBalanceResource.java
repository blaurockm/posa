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

	@GET
	@Path("/all")
	public List<PosCashBalance> fetchAll(@QueryParam("date") Optional<String> date)  {
		return dao.fetchAllAfter(date.or(new DateTime().minusMonths(1).toString("yyyyMMdd")));
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
	public PosCashBalance fetchForDate(@PathParam("date") String date, @QueryParam("recreate") Optional<Boolean> recreate)  {
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
		if (recreate.isPresent() && recreate.get()) {
			CashBalance balCOmp = new CashBalance(txDao, ticketDao);
			PosCashBalance newBal = balCOmp.computeBalance(bal.getFirstCovered(), bal.getLastCovered());
			newBal.setAbschlussId(bal.getAbschlussId());
			newBal.setAbsorption(bal.getAbsorption());
			newBal.setOrigAbschluss(bal.getOrigAbschluss());
			return newBal;
		}
		return bal;
	}


}
