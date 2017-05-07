package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosArticle;
import net.buchlese.posa.api.bofc.Tax;
import net.buchlese.posa.api.pos.Artikel;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;
import net.buchlese.posa.jdbi.pos.ArtikelDAO;

import com.google.common.base.Optional;

public class SynchronizePosArticle extends AbstractSynchronizer {

	private final PosArticleDAO artDAO;

	private final ArtikelDAO artikelDAO;
	private final Integer limit;

	public SynchronizePosArticle(PosArticleDAO artDao, ArtikelDAO artikelDao, Integer limit) {
		this.artDAO = artDao;
		this.artikelDAO = artikelDao;
		this.limit = limit;
	}
	
	/**
	 * erzeuge Invoices für die neu angelegten Rechnungen
	 * 
	 * Wird vom Sync-Timer aufgerufen.
	 * 
	 * @param syncStart
	 */
	public BigDecimal fetchNewAndChangedArticles(BigDecimal rowver) {
		BigDecimal res = rowver;
		Optional<Integer> maxId = Optional.fromNullable(artDAO.getLastErfasst());

		List<Artikel> rechs = artikelDAO.fetchAllArtikelAfter(maxId.or(0), limit);
		createNewArticles(rechs);
		if (rechs.isEmpty() == false) {
			res = rechs.get(rechs.size()-1).getZeitmarke();
			if (rowver == null || rowver.intValue() == 0) {
				rowver = res;
			}
		}
		
		if (rechs.size() != 1000) {
			// wenn es genau 1000 sind, dann hat vermutlich das limit in dem SQL-Statement zugeschlagen, dann
			// gibt es beim nächsten durchlauf noch mehr. Änderungen sind erst sinnvoll, wenn alle übertragen wurden
			List<Artikel> rechsChg = artikelDAO.fetchAllChangedArtikelAfter(rowver);
			if (rechsChg.isEmpty() == false) {
				BigDecimal newRes = rechsChg.get(rechsChg.size()-1).getZeitmarke();
				if (newRes.compareTo(res) > 0) {
					res = newRes;
				}
			}
			updateArticles(rechsChg);
		}
		return res;
	}

	public List<PosArticle> createNewArticles(List<Artikel> artikel) {
		List<PosArticle> invs = new ArrayList<>();
		for (Artikel art : artikel) {
			if (art.getArtikelnummer() != null &&  art.getmWSt() != null) {
				invs.add(createArticle(art));
			}
		}
		artDAO.insertAll(invs.iterator());
		PosAdapterApplication.homingQueue.addAll(invs); // sync the new ones back home
		return invs;
	}

	public List<PosArticle> updateArticles(List<Artikel> rechnungen) {
		List<PosArticle> invs = new ArrayList<>();
		for (Artikel art : rechnungen) {
			if (art.getArtikelnummer() != null &&  art.getmWSt() != null) {
				List<PosArticle> artcl = artDAO.fetchArticle(art.getArtikelident());
				if (artcl.isEmpty() == false) {
					PosArticle i = updateArticle(artcl.get(0), art);
					invs.add(i);
					artDAO.updateArticle(i);
				}
			}
		}
		PosAdapterApplication.homingQueue.addAll(invs); // sync the new ones back home
		return invs;
	}
	
	
	
	public PosArticle createArticle(Artikel rech) {
		PosArticle inv = new PosArticle();
		inv.setArtikelIdent(rech.getArtikelident()); // der darf sich net verändern..
		return updateArticle(inv, rech);
	}
	
	
	public PosArticle updateArticle(PosArticle part, Artikel artikel) {
		updDate(part::setLastPurchaseDate, part.getLastPurchaseDate(), artikel.getLetztesEinkaufsdatum());
		updDate(part::setLastSellingDate, part.getLastSellingDate(), artikel.getLetztesVerkaufsdatum());
		
		updStr(part::setEan, part.getEan(), artikel.getEan());
		updStr(part::setIsbn, part.getIsbn(), artikel.getIsbn());
		updStr(part::setMatchcode, part.getMatchcode(), artikel.getMatchcode());
		updStr(part::setAuthor, part.getAuthor(), artikel.getAutor());
		updStr(part::setBezeichnung, part.getBezeichnung(), artikel.getBezeichnung(), 255);
		updStr(part::setPublisher, part.getPublisher(), artikel.getVerlag());
		updStr(part::setArtikelnummer, part.getArtikelnummer(), artikel.getArtikelnummer());

		updInt(part::setAvailableStock, part.getAvailableStock(), artikel.getBestand());
		
		updMoney(part::setPurchasePrice, part.getPurchasePrice(), artikel.geteK());
		updMoney(part::setSellingPrice, part.getSellingPrice(), artikel.getvK());
		updEnum(part::setTax, part.getTax(), Tax.mappingFrom(artikel.getmWSt()));
		if (artikel.getWarGrIndex() != null) {
			updStr(part::setWargrindex, part.getWargrindex(), String.valueOf(artikel.getWarGrIndex()));
		} else {
			updStr(part::setWargrindex, part.getWargrindex(), null);
		}

		return part;
	}


}
