package net.buchlese.posa.resources;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.view.AppView;
import net.buchlese.posa.view.BalanceView;

import com.google.common.base.Optional;

@Path("/app{view:.*}")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final PosAdapterConfiguration cfg;
	private final PosCashBalanceDAO dao;
	
	public AppResource(PosAdapterConfiguration config, PosCashBalanceDAO dao) {
		this.cfg = config;
		this.dao = dao;
	}

	@GET
	public View getView(@PathParam("view") String view, @QueryParam("date") Optional<String> date) {
		if ("/cashbalance".equals(view)) {
			return new BalanceView(dao.fetchForDate(date.get()));
		}
		return new AppView(cfg);
	}
	
}
