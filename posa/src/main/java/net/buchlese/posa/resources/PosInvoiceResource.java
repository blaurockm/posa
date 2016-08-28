package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.pos.KleinteilKopf;
import net.buchlese.posa.core.SynchronizePosInvoice;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.jdbi.pos.KleinteilDAO;

import com.google.inject.Inject;

@Path("/invoice")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class PosInvoiceResource {

	private final PosInvoiceDAO dao;
	private final KleinteilDAO rechDao;

	@Inject
	public PosInvoiceResource(PosInvoiceDAO dao, KleinteilDAO rd) {
		super();
		this.dao = dao;
		this.rechDao = rd;
	}
	
	@GET
	@Path("/{nr}")
	public List<PosInvoice> fetch(@PathParam("nr") String nr)  {
		return dao.fetchInvoice(nr);
	}

	@GET
	@Path("/sendbof/{nr}")
	public PosInvoice sendAgainInvoice(@PathParam("nr") String nr)  {
		List<PosInvoice> cb = dao.fetchInvoice(nr);
		if (cb.isEmpty() == false) {
			PosAdapterApplication.homingQueue.offer(cb.get(0));
			return cb.get(0);
		}
		return null;
	}

	@GET
	@Path("/resync/{nr}")
	public List<PosInvoice> resyncInvoice(@PathParam("nr") String nr)  {
		List<KleinteilKopf> kk = rechDao.fetch(nr);
		SynchronizePosInvoice spi = new SynchronizePosInvoice(dao,  rechDao, 28);
		return spi.createNewInvoices(kk);
	}

}
