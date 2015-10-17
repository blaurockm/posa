package net.buchlese.posa.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.pos.KassenAbschluss;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/abschluss")
@Produces(MediaType.APPLICATION_JSON)
public class KassenAbschlussResource {

	private final KassenAbschlussDAO dao;

	@Inject
	public KassenAbschlussResource(KassenAbschlussDAO dao) {
		super();
		this.dao = dao;
	}
	
	@GET
	public List<KassenAbschluss> fetch(@QueryParam("date") Optional<String> date) {
		if (date.isPresent()) {
			return Arrays.asList(dao.fetchForDate(date.get()));
		}
		// nur die der letzten 2 Monate
		return dao.fetchAllAfter(new DateTime().minusMonths(2).toString("yyyyMMdd"));
	}
	
	
	
}
