package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

import com.google.inject.Inject;

@Path("/invoice")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class PosInvoiceResource {

	private final PosInvoiceDAO dao;

	@Inject
	public PosInvoiceResource(PosInvoiceDAO dao) {
		super();
		this.dao = dao;
	}
	
	@GET
	@Path("/{nr}")
	public List<PosInvoice> fetch(@PathParam("nr") String nr)  {
		return dao.fetch(nr);
	}



}
