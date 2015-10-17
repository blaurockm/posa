package net.buchlese.posa.resources;

import io.dropwizard.views.View;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.core.CashBalance;
import net.buchlese.posa.core.Validator;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.view.CashBalView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.google.inject.Inject;

@Path("/cashbalance")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class PosCashBalanceResource {

	private final PosCashBalanceDAO dao;
	private final PosTicketDAO ticketDao;

	@Inject
	public PosCashBalanceResource(PosCashBalanceDAO dao,PosTicketDAO ticketdao) {
		super();
		this.dao = dao;
		this.ticketDao = ticketdao;
	}

	@GET
	@Path("/{date}")
	public PosCashBalance fetchForDate(@PathParam("date") String date)  {
		return fetchBalanceForDate(date);
	}
	
	@GET
	@Path("/list")
	public List<PosCashBalance> fetchForList()  {
		return dao.fetchAllAfter(LocalDate.now().minusDays(8).toString("yyyyMMdd"));
	}

	@Produces({"text/html"})
	@GET
	@Path("/view/{date}")
	public View fetchViewForDate(@PathParam("date") String abschlussId)  {
		PosCashBalance cb = dao.fetchForDate(abschlussId);
		if (cb == null) {
			// es gibt den zu resyncenden abschluss noch gar nicht. einen leeren anlegen
			CashBalance balComp = new CashBalance(ticketDao);
			cb = balComp.createBalance(null, null);
			cb.setAbschlussId(abschlussId);
		}
		return new CashBalView(cb);
	}

	@Produces({"text/html"})
	@GET
	@Path("/resync/{date}")
	public View resyncBalanceForDate(@PathParam("date") String abschlussId)  {
		PosCashBalance cb = dao.fetchForDate(abschlussId);
		if (cb == null) {
			// es gibt den zu resyncenden abschluss noch gar nicht. einen leeren anlegen, der rest macht das resync
			CashBalance balComp = new CashBalance(ticketDao);
			cb = balComp.createBalance(null, null);
			cb.setAbschlussId(abschlussId);
		}
		PosAdapterApplication.resyncQueue.offer(cb);
		return new CashBalView(cb);
	}

	@Produces({"text/html"})
	@GET
	@Path("/sendbof/{date}")
	public View sendAgainBalanceForDate(@PathParam("date") String date)  {
		PosCashBalance cb = dao.fetchForDate(date);
		PosAdapterApplication.homingQueue.offer(cb);
		return new CashBalView(cb);
	}

	
	@Produces(MediaType.TEXT_PLAIN)
	@GET
	@Path("/extended/{date}")
	public StreamingOutput fetchDetailsForDate(@PathParam("date") String date)  {
	    return new StreamingOutput() {
	        public void write(OutputStream output) throws IOException, WebApplicationException {
	            try {
	                Validator generator = new Validator(dao.fetchForDate(date), ticketDao);
	                generator.validateDetails(output);
	            } catch (Exception e) {
	                throw new WebApplicationException(e);
	            }
                output.flush();
	        }
	    };	
	}

	private PosCashBalance fetchBalanceForDate(String datekey) {
		CashBalance balComp = new CashBalance(ticketDao);
		DateTime till = new DateTime();
		DateTime from = till.hourOfDay().setCopy(0); // stunde 0
		PosCashBalance bal = null;
		switch (datekey) {
		case "today" :
			// wir berechnen den von heute...
			bal = balComp.computeBalanceFast(from, till);
			break;
		case "thisweek" :
			// wir berechnen den von dieser woche
			from = till.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 0
			bal = balComp.computeBalanceFast(from, till); // hier ist die geschwindigkeit wichtiger als die Genauigkeit
			break;
		case "lastweek" :
			// wir berechnen den von letzter woche
			till = new DateTime().minusWeeks(1).hourOfDay().setCopy(23).dayOfWeek().setCopy(7); // letzter tag und letzte stunde
			from = till.hourOfDay().setCopy(0).dayOfWeek().setCopy(1); // stunde 0 und wochentag 1
			bal = balComp.computeBalanceFast(from, till); // hier ist die geschwindigkeit wichtiger als die Genauigkeit
			break;
		case "thismonth" :
			// wir berechnen den von diesem monat
			from = till.hourOfDay().setCopy(0).dayOfMonth().setCopy(1); // stunde 0 und monats-ersten
			bal = balComp.computeBalanceFast(from, till); // hier ist die geschwindigkeit wichtiger als die Genauigkeit
			break;
		default :
			bal = dao.fetchForDate(datekey);
			if (bal == null) {
				// es gibt den zu resyncenden abschluss noch gar nicht. einen leeren anlegen
				bal = balComp.createBalance(null, null);
				bal.setAbschlussId(datekey);
			}
		}
		return bal;
	}

}
