package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.core.AccountingExportFactory;
import net.buchlese.bofc.core.AccountingExportFile;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.AccountingExportView;
import net.buchlese.bofc.view.MobileView;
import net.buchlese.bofc.view.StartView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final BackOfcConfiguration cfg;
	private final PosInvoiceDAO daoInv;
	private final PosCashBalanceDAO daoBal;
	
	@Inject
	public AppResource(BackOfcConfiguration config, PosInvoiceDAO daoInv, PosCashBalanceDAO dalBal) {
		this.cfg = config;
		this.daoInv = daoInv;
		this.daoBal = dalBal;
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
	@Path("/start")
	public View getDesktop() {
		return new StartView(cfg);
	}

	@GET
	@Path("/viewfibuexport")
	@Produces("text/html")
	public View create(@QueryParam("key") Integer key) {
		AccountingExport ae = AccountingExportFactory.getExport(key);
		if (ae != null) {
			return new AccountingExportView(ae);
		}
		throw new WebApplicationException("key not found");
	}

	@POST
	@Path("/fibuexport")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/html")
	public View create( @FormParam("from")String fromStr, @FormParam("till")String tillStr, @FormParam("kasse") List<Integer> kassen) {
		LocalDate from = new DateParam(fromStr).getDate();
		Optional<LocalDate> till = Optional.fromNullable(tillStr).transform(d -> new DateParam(d).getDate());
		for (Integer kasse : kassen) {
			return new AccountingExportView(AccountingExportFactory.createExport(kasse, from, till, daoBal, daoInv));
		}
		throw new WebApplicationException("kasse not found");
	}

	@GET
	@Path("/fibuexportfile")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/csv; charset='iso-8859-1'")
	public Response createFile( @QueryParam("key") Integer key) {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(os, "iso-8859-1"));
				AccountingExport ae = AccountingExportFactory.getExport(key);
				try {
					AccountingExportFile.createFile(ae, writer);
				} catch (Exception e) {
					writer.write("\n\n\nproblem creating cashBalance " + e.toString() + "\n\n\n\n");
				}
				writer.flush();
			}
		};
		return Response.ok(stream).header("Content-Disposition","attachment; filename=wochenliste.csv").build();
	}

	private static class DateParam {
		private LocalDate date;
		public DateParam(String dateStr) {
			date = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(dateStr);
		}
		public LocalDate getDate() {
			return date;
		}
	}

}
