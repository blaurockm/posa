package net.buchlese.bofc.resources;

import java.util.Collections;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.AppView;
import net.buchlese.bofc.view.MappingView;
import net.buchlese.bofc.view.pages.CommandsView;
import net.buchlese.bofc.view.pages.ExportView;
import net.buchlese.bofc.view.pages.RechnungenView;
import net.buchlese.bofc.view.pages.StateView;
import net.buchlese.bofc.view.pages.TechnicsView;

import com.google.inject.Inject;

@Path("/pages")
@Produces(MediaType.TEXT_HTML)
@SuppressWarnings("unused")
public class PageResource {

	private final BackOfcConfiguration cfg;
	private final Environment app;
	private final PosInvoiceDAO daoInv;
	private final PosCashBalanceDAO daoBal;
	private final CommandResource cmdRes;
	
	@Inject
	public PageResource(BackOfcConfiguration config, Environment app, PosInvoiceDAO daoInv, PosCashBalanceDAO dalBal, CommandResource cres) {
		this.cfg = config;
		this.app = app;
		this.daoInv = daoInv;
		this.daoBal = dalBal;
		this.cmdRes = cres;
	}

	@GET
	@Path("/commands")
	public View getCommands() {
		return new CommandsView(cfg, cmdRes);
	}

	@GET
	@Path("/mappings")
	public View getMappings() {
		return new MappingView(Collections.emptyList());
	}

	@GET
	@Path("/technics")
	public View getTechnics() {
		return new TechnicsView(cfg, app);
	}

	@GET
	@Path("/index")
	public View getStartView() {
		return new StateView(cfg);
	}

	@GET
	@Path("/export")
	public View getExport() {
		return new ExportView(cfg);
	}

	@GET
	@Path("/rechnungen")
	public View getRechnungen() {
		return new RechnungenView(cfg, daoInv);
	}


}
