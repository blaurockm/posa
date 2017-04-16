package net.buchlese.posa.resources;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.view.DataView;
import net.buchlese.posa.view.IndexView;
import net.buchlese.posa.view.LogfilesView;

import com.google.inject.Inject;

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
			return getDesktop();
		}
		return getDesktop();
	}

	@GET
	@Path("/index")
	public View getDesktop() {
		return new IndexView(cfg, dao, daoInv, env);
	}
	
	@GET
	@Path("/data")
	public View getData() {
		return new DataView(cfg, dao, daoInv);
	}

	@GET
	@Path("/logfiles")
	public View getLogfiles() {
		return new LogfilesView(cfg, dao, daoInv, env);
	}

}
