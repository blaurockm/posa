package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

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
	public List<PosTicket> fetchAll(@QueryParam("date") Optional<String> date) {
		if (date.isPresent()) {
			PosCashBalance bal = balDao.fetchForDate(date.get());
			
			if (bal != null) {
				return dao.fetch(bal.getFirstCovered(), bal.getLastCovered());
			}
			// kein abschluss gefunden.. wir nehmen die von heute..
			DateTime today = new DateTime();
			DateTime startOfToday = today.hourOfDay().setCopy(0); // stunde 0
			return dao.fetch(startOfToday, today);
		}
		return dao.fetchAll();
	}
}
