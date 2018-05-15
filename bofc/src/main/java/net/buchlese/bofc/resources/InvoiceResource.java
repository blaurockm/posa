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

import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import io.dropwizard.hibernate.UnitOfWork;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jpa.JpaPosInvoiceDAO;
import net.buchlese.bofc.jpa.JpaPosIssueSlipDAO;

@Path("/invoice")
public class InvoiceResource {

	private final PosInvoiceDAO dao;
	private final JpaPosInvoiceDAO jpaDao;
	private final JpaPosIssueSlipDAO jpaSlipDao;

	@Inject
	public InvoiceResource(PosInvoiceDAO dao, JpaPosInvoiceDAO jid, JpaPosIssueSlipDAO isd ) {
		super();
		this.dao = dao;
		this.jpaDao = jid;
		this.jpaSlipDao = isd;
	}

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(InvoiceResource.class);

	@POST
	@Path("/acceptInvoice")
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response acceptInvoice(PosInvoice invoice)  {
		try {
			// mapping der Debitor-Nummer
			Integer debNo = dao.mapDebitor(invoice.getPointid(), invoice.getCustomerId());
			if (debNo != null) {
				invoice.setDebitorId(debNo.intValue());
			}
			List<PosInvoice> old = dao.fetch(invoice.getNumber());
			if (old.isEmpty()) {
				long newId = dao.insert(invoice);
				invoice.setId(newId);
				jpaDao.create(invoice);
			} else {
				invoice.setId(old.get(0).getId());
				dao.updateInvoice(invoice);
				jpaDao.update(invoice);
			}
			return Response.ok().build();
		} catch (Throwable t) {
			log.error("problem creating invoice" + invoice, t);
			return Response.serverError().entity(t.getMessage()).build();
		}
	}

	@POST
	@Path("/acceptIssueSlip")
	@Consumes(MediaType.APPLICATION_JSON)
	@UnitOfWork
	public Response acceptIssueSlip(PosIssueSlip invoice)  {
		try {
			// mapping der Debitor-Nummer
			Integer debNo = dao.mapDebitor(invoice.getPointid(), invoice.getCustomerId());
			if (debNo != null) {
				invoice.setDebitorId(debNo.intValue());
			}
			List<PosInvoice> old = dao.fetch(invoice.getNumber());
			if (old.isEmpty()) {
				long newid = dao.insertIssueSlip(invoice);
				invoice.setId(newid);
				jpaSlipDao.create(invoice);
			} else {
				invoice.setId(old.get(0).getId());
				dao.updateIssueSlip(invoice);
				jpaSlipDao.update(invoice);
			}
			return Response.ok().build();
		} catch (Throwable t) {
			log.error("problem creating issueSlip" + invoice, t);
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
	@Path("transferinv/{nr}")
	@UnitOfWork
	public PosInvoice transferInv(@PathParam("nr") String num)  {
		List<PosInvoice> invs = dao.fetch(num);
		if (invs.isEmpty()) {
			return null;
		}
		PosInvoice inv = invs.get(0);
		List<PosInvoice> x = jpaDao.findByNumber(num);
		if (x.isEmpty()) {
			jpaDao.create(inv);
		} else {
			inv.setId(x.get(0).getId());
			jpaDao.update(inv);
		}
		return inv;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("transferslip/{nr}")
	@UnitOfWork
	public PosIssueSlip transferSlip(@PathParam("nr") String num)  {
		PosIssueSlip inv = dao.fetchSlipById(num);
		jpaSlipDao.create(inv);
		return inv;
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	@Path("/xml/{nr}")
	public List<PosInvoice> fetchXml(@PathParam("nr") String num)  {
//		JAXBContext jc = JAXBContext.newInstance(new Class[] {PosCashBalance.class});
//		Marshaller m = jc.createMarshaller();
//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
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

}
