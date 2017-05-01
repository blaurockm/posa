package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosArticleStockChange;
import net.buchlese.posa.api.pos.ArtikelBestandBuchung;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;
import net.buchlese.posa.jdbi.pos.ArtikelDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

public class SynchronizePosArticleStockChange extends AbstractSynchronizer {

	private final PosArticleDAO artDAO;

	private final ArtikelDAO artikelDAO;
	private final Integer limit;

	public SynchronizePosArticleStockChange(PosArticleDAO artDao, ArtikelDAO artikelDao, Integer limit) {
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
	public BigDecimal fetchNewAndChangedStockChanges(BigDecimal rowver) {
		BigDecimal res = rowver;
		Optional<DateTime> maxId = Optional.fromNullable(artDAO.getLastStockChange());

		List<ArtikelBestandBuchung> rechs = artikelDAO.fetchBestandsbuchungenAfter(maxId.or(DateTime.now().minusWeeks(40)), limit);
		createNewStockChanges(rechs);
		if (rechs.isEmpty() == false) {
			res = rechs.get(rechs.size()-1).getZeitmarke();
			if (rowver == null || rowver.intValue() == 0) {
				rowver = res;
			}
		}
		
		if (rechs.size() != 1000) {
			// wenn es genau 1000 sind, dann hat vermutlich das limit in dem SQL-Statement zugeschlagen, dann
			// gibt es beim nächsten durchlauf noch mehr. Änderungen sind erst sinnvoll, wenn alle übertragen wurden
			List<ArtikelBestandBuchung> rechsChg = artikelDAO.fetchBestandsbuchungenAfter(rowver);
			if (rechsChg.isEmpty() == false) {
				BigDecimal newRes = rechsChg.get(rechsChg.size()-1).getZeitmarke();
				if (newRes.compareTo(res) > 0) {
					res = newRes;
				}
			}
			updateStockChanges(rechsChg);
		}
		return res;
	}

	public List<PosArticleStockChange> createNewStockChanges(List<ArtikelBestandBuchung> artikel) {
		List<PosArticleStockChange> invs = new ArrayList<>();
		for (ArtikelBestandBuchung art : artikel) {
			if (art.getMenge() != 0) {
				invs.add(createStockChange(art));
			}
		}
		artDAO.insertStockChangeAll(invs.iterator());
		PosAdapterApplication.homingQueue.addAll(invs); // sync the new ones back home
		return invs;
	}

	public List<PosArticleStockChange> updateStockChanges(List<ArtikelBestandBuchung> rechnungen) {
		List<PosArticleStockChange> invs = new ArrayList<>();
		for (ArtikelBestandBuchung art : rechnungen) {
			if (art.getMenge() != 0) {
				List<PosArticleStockChange> artcl = artDAO.fetchStockChange(art.getArtikelident(), art.getDatum());
				if (artcl.isEmpty() == false) {
					PosArticleStockChange i = updateStockChange(artcl.get(0), art);
					invs.add(i);
					artDAO.updateStockChange(i);
				}
			}
		}
		PosAdapterApplication.homingQueue.addAll(invs); // sync the new ones back home
		return invs;
	}
	
	
	
	public PosArticleStockChange createStockChange(ArtikelBestandBuchung rech) {
		PosArticleStockChange inv = new PosArticleStockChange();
		inv.setArtikelIdent(rech.getArtikelident()); // der darf sich net verändern..
		inv.setChangeDate(rech.getDatum());
		return updateStockChange(inv, rech);
	}
	
	
	public PosArticleStockChange updateStockChange(PosArticleStockChange part, ArtikelBestandBuchung artikel) {
		updInt(part::setStockChange, part.getStockChange(), artikel.getMenge());
		updInt(part::setStockAfter, part.getStockAfter(), artikel.getBestandNeu());
		updInt(part::setStockBefore, part.getStockBefore(), artikel.getBestandAlt());
		
		updStr(part::setComment, part.getComment(), artikel.getBemerkung());

		return part;
	}


}
