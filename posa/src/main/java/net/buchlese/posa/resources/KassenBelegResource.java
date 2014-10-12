package net.buchlese.posa.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

@Path("/beleg")
@Produces(MediaType.APPLICATION_JSON)
public class KassenBelegResource {

	private final KassenBelegDAO dao;

	public KassenBelegResource(KassenBelegDAO dao) {
		super();
		this.dao = dao;
	}
	
	@GET
	public List<KassenBeleg> fetch(@QueryParam("nr") Optional<Long> nr) {
		if (nr.isPresent()) {
			return Arrays.asList(dao.fetch(nr.get()));
		}
		// ohne parameter nur die letzten 2 Monate
		return dao.fetchAllAfter(new DateTime().minusMonths(2));
	}
	
	
	
}
