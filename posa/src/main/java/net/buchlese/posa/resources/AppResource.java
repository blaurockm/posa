package net.buchlese.posa.resources;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.view.AppView;
import net.buchlese.posa.view.MobileView;
import net.buchlese.posa.view.TechnicsView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final PosAdapterConfiguration cfg;
	private final Environment env;
	private final PosCashBalanceDAO dao;
	private final PosInvoiceDAO daoInv;

	@Inject
	public AppResource(PosAdapterConfiguration config, Environment env, PosCashBalanceDAO dao, PosInvoiceDAO daoInv) {
		this.cfg = config;
		this.env = env;
		this.dao = dao;
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
	public View getDesktop() {
		return new AppView(cfg, dao, daoInv);
	}
	
	@GET
	@Path("/technics")
	public View getTechnics() {
		return new TechnicsView(cfg, env);
	}

}
