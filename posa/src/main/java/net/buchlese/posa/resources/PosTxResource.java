package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;

import org.joda.time.DateTime;

import com.google.inject.Inject;

@Path("/tx")
@Produces(MediaType.APPLICATION_JSON)
public class PosTxResource {

	private final PosTxDAO dao;
	private final PosCashBalanceDAO balDao;

	@Inject
	public PosTxResource(PosTxDAO dao, PosCashBalanceDAO baldao) {
		super();
		this.dao = dao;
		this.balDao = baldao;
	}
	
	@GET
	public List<PosTx> fetchAll(@QueryParam("date") String date) {
		if ("Heute".equals(date)) {
			DateTime refdate = new DateTime();
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("Gestern".equals(date)) {
			DateTime refdate = new DateTime().minusDays(1);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("Vorgestern".equals(date)) {
			DateTime refdate = new DateTime().minusDays(2);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("Vor 3 Tagen".equals(date)) {
			DateTime refdate = new DateTime().minusDays(3);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("Vor 4 Tagen".equals(date)) {
			DateTime refdate = new DateTime().minusDays(4);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("gleicherWoTag letze Woche".equals(date)) {
			DateTime refdate = new DateTime().minusDays(8);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		if ("gestrigerWoTag letze Woche".equals(date)) {
			DateTime refdate = new DateTime().minusDays(9);
			DateTime from = refdate.hourOfDay().withMinimumValue();
			DateTime till = refdate.hourOfDay().withMaximumValue();
			return dao.fetch(from, till);
		}
		PosCashBalance bal = balDao.fetchForDate(date);
		if (bal != null) {
			return dao.fetch(bal.getFirstCovered(), bal.getLastCovered());
		}
		// kein abschluss gefunden.. wir nehmen die von heute..
		return dao.fetch(new DateTime().minusMonths(2), new DateTime());
	}
}
