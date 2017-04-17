package net.buchlese.posa.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosArticle;
import net.buchlese.posa.api.pos.Artikel;
import net.buchlese.posa.core.SynchronizePosArticle;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;
import net.buchlese.posa.jdbi.pos.ArtikelDAO;

import com.google.inject.Inject;

@Path("/article")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class PosArticleResource {

	private final PosArticleDAO dao;
	private final ArtikelDAO artDAO;

	@Inject
	public PosArticleResource(PosArticleDAO dao, ArtikelDAO rd) {
		super();
		this.dao = dao;
		this.artDAO = rd;
	}
	
	@GET
	@Path("/{nr}")
	public List<PosArticle> fetch(@PathParam("nr") Integer nr)  {
		return dao.fetchArticle(nr);
	}

	@GET
	public List<PosArticle> fetchAll()  {
		return dao.fetchArticles();
	}

	@GET
	@Path("/sendbof/{nr}")
	public PosArticle sendAgainInvoice(@PathParam("nr") Integer nr)  {
		List<PosArticle> cb = dao.fetchArticle(nr);
		if (cb.isEmpty() == false) {
			PosAdapterApplication.homingQueue.offer(cb.get(0));
			return cb.get(0);
		}
		return null;
	}

	@GET
	@Path("/resync/{nr}")
	public List<PosArticle> resyncInvoice(@PathParam("nr") Long nr)  {
		List<Artikel> kk = artDAO.fetchByArtikelident(nr);
		SynchronizePosArticle spi = new SynchronizePosArticle(dao,  artDAO, 28);
		return spi.createNewArticles(kk);
	}

}
