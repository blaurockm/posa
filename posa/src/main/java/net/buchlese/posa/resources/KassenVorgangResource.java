package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;

@Path("/vorgang")
@Produces(MediaType.APPLICATION_JSON)
public class KassenVorgangResource {

	private final KassenVorgangDAO dao;

	public KassenVorgangResource(KassenVorgangDAO dao, DBI dbi) {
		super();
		this.dao = dao;
	}
	
	@GET
	public List<KassenVorgang> fetchAll(@QueryParam("date") Optional<DateTime> nr) {
		if (nr.isPresent()) {
			return dao.fetchAllAfter(nr.get());
		}
		// ohne parameter nur die letzten 2 monate
		return dao.fetchAllAfter(new DateTime().minusMonths(2));
	}
	
	
	
}
