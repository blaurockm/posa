package net.buchlese.bofc.resources;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.AppView;
import net.buchlese.bofc.view.MobileView;
import net.buchlese.bofc.view.StartView;
import net.buchlese.bofc.view.StateView;
import net.buchlese.bofc.view.TechnicsView;

import com.google.inject.Inject;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final BackOfcConfiguration cfg;
	private final Environment app;
	private final PosInvoiceDAO daoInv;
	
	@Inject
	public AppResource(BackOfcConfiguration config, Environment app, PosInvoiceDAO daoInv) {
		this.cfg = config;
		this.app = app;
		this.daoInv = daoInv;
	}

	@GET
	public View getApp(@HeaderParam("user-agent") String userAgent ) {
		if (userAgent.contains("Android") ||
			userAgent.contains("iPhone") ||
			userAgent.contains("iPad")) {
			return getMobile();
		}
		return getDesktop();
	}

	@GET
	@Path("/mobile")
	public View getMobile() {
		return new MobileView(cfg);
	}

	@GET
	@Path("/app")
	public View getStatistics() {
		return new AppView(cfg);
	}

	@GET
	@Path("/technics")
	public View getTechnics() {
		return new TechnicsView(cfg, app);
	}

	@GET
	@Path("/start")
	public View getDesktop() {
		return new StartView(cfg, daoInv);
	}

	@GET
	@Path("/state")
	public View getState() {
		return new StateView(cfg);
	}

}
