package net.buchlese.bofc.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;

import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/invoice")
public class InvoiceResource {

	private final PosInvoiceDAO dao;

	@Inject
	public InvoiceResource(PosInvoiceDAO dao) {
		super();
		this.dao = dao;
	}

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(InvoiceResource.class);

	@POST
	@Path("/acceptInvoice")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptBalance(PosInvoice invoice)  {
		try {
			dao.insert(invoice);
			return Response.ok().build();
		} catch (Throwable t) {
			log.error("problem creating cashBalance" + invoice, t);
			return Response.serverError().entity(t.getMessage()).build();
		}
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{nr}")
	public List<PosInvoice> fetch(@PathParam("nr") String num)  {
		return dao.fetch(num);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PosInvoice> fetchAll(@QueryParam("nr") Optional<String> num)  {
		if (num.isPresent()) {
			return dao.fetch(num.get());
		}
		return dao.fetchAllAfter(new DateTime().minusMonths(1).toDate());
	}


//	@GET
//	@Path("/viewfibuexport")
//	@Produces("text/html")
//	public View create(@QueryParam("key") Integer key) {
//		AccountingExport ae = AccountingExportFactory.getExport(key);
//		if (ae != null) {
//			return new AccountingExportView(ae);
//		}
//		throw new WebApplicationException("key not found");
//	}
//
//	@POST
//	@Path("/fibuexport")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces("text/html")
//	public View create( @FormParam("from")String fromStr, @FormParam("till")String tillStr, @FormParam("kasse") List<Integer> kassen) {
//		LocalDate from = new DateParam(fromStr).getDate();
//		Optional<LocalDate> till = Optional.fromNullable(tillStr).transform(d -> new DateParam(d).getDate());
//		for (Integer kasse : kassen) {
//			return new AccountingExportView(AccountingExportFactory.createExport(kasse, from, till, dao));
//		}
//		throw new WebApplicationException("kasse not found");
//	}
//
//	@GET
//	@Path("/fibuexportfile")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces("text/csv; charset='iso-8859-1'")
//	public Response createFile( @QueryParam("key") Integer key) {
//		StreamingOutput stream = new StreamingOutput() {
//			@Override
//			public void write(OutputStream os) throws IOException,  WebApplicationException {
//				Writer writer = new BufferedWriter(new OutputStreamWriter(os, "iso-8859-1"));
//				AccountingExport ae = AccountingExportFactory.getExport(key);
//				try {
//					AccountingExportFile.createFile(ae, writer);
//				} catch (Exception e) {
//					log.error("problem creating file for AccountingExport " + ae, e);
//					writer.write("\n\n\nproblem creating cashBalance " + e.toString() + "\n\n\n\n");
//				}
//				writer.flush();
//			}
//		};
//		return Response.ok(stream).header("Content-Disposition","attachment; filename=wochenliste.csv").build();
//	}
//
//
//	private static class DateParam {
//		private LocalDate date;
//		public DateParam(String dateStr) {
//			date = DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(dateStr);
//		}
//		public LocalDate getDate() {
//			return date;
//		}
//	}
}
