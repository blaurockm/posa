package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/vorgang")
@Produces(MediaType.APPLICATION_JSON)
public class KassenVorgangResource {

	private final KassenVorgangDAO dao;

	@Inject
	public KassenVorgangResource(KassenVorgangDAO dao) {
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
	
	@Path("/{nr}/{idx}")
	@GET
	public KassenVorgang fetchAll(@PathParam("nr") long belegnr,@PathParam("idx") int idx) {
		return dao.fetch(belegnr, idx);
	}

	
	
}
