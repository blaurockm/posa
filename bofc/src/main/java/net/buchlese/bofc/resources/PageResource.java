package net.buchlese.bofc.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.pages.NavigationView;
import net.buchlese.bofc.view.pages.StateView;
import net.buchlese.bofc.view.pages.TechnicsView;

@Path("/pages")
@Produces(MediaType.TEXT_HTML)
@SuppressWarnings("unused")
public class PageResource {

	private final BackOfcConfiguration cfg;
	private final Environment app;
	private final PosInvoiceDAO daoInv;

	@Inject
	public PageResource(BackOfcConfiguration config, Environment app,
			PosInvoiceDAO daoInv) {
		this.cfg = config;
		this.app = app;
		this.daoInv = daoInv;
	}

	@GET
	@Path("/technics")
	public View getTechnics() {
		return new TechnicsView(cfg, app);
	}

	@GET
	@Path("/dashboard")
	public View getStartView() {
		return new StateView(cfg);
	}

	@GET
	@Path("/navigation")
	public View getMappings() {
		return new NavigationView();
	}

}
