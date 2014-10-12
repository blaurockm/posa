package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.bofc.PosCashBalance;
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
	@Path("all")
	public List<PosCashBalance> fetchAll(@QueryParam("date") Optional<String> date)  {
		return dao.fetchAll();
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
