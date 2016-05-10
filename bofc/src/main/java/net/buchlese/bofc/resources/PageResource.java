package net.buchlese.bofc.resources;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.joda.time.LocalDate;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.api.bofc.Mapping;
import net.buchlese.bofc.jdbi.bofc.MappingDAO;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.pages.CommandsView;
import net.buchlese.bofc.view.pages.ExportView;
import net.buchlese.bofc.view.pages.ExportsView;
import net.buchlese.bofc.view.pages.MappingView;
import net.buchlese.bofc.view.pages.RechnungenView;
import net.buchlese.bofc.view.pages.StateView;
import net.buchlese.bofc.view.pages.TechnicsView;
import net.buchlese.bofc.view.subscr.SubscrDashboardView;

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
	private MappingDAO mapDao;

	@Inject
	public PageResource(BackOfcConfiguration config, Environment app,
			PosInvoiceDAO daoInv, PosCashBalanceDAO dalBal, 
			CommandResource cres, MappingDAO mapdao) {
		this.cfg = config;
		this.app = app;
		this.daoInv = daoInv;
		this.daoBal = dalBal;
		this.cmdRes = cres;
		this.mapDao = mapdao;
	}

	@GET
	@Path("/commands")
	public View getCommands() {
		return new CommandsView(cfg, cmdRes);
	}

	@GET
	@Path("/mappings")
	public View getMappings( @QueryParam("point") Integer pi) {
		if (pi != null) {
			List<Mapping> res = mapDao.fetch(pi.intValue());
			return new MappingView(res);
		}
		return new MappingView(mapDao.fetchAllEmpty());
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
	@Path("/exports")
	public View getExports() {
		return new ExportsView();
	}

	@GET
	@Path("/rechnungen")
	public View getRechnungen() {
		return new RechnungenView(cfg, daoInv);
	}


}
