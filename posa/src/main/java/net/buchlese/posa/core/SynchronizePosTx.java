package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.bofc.Tax;
import net.buchlese.posa.api.bofc.TxType;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.DateTime;

import com.google.common.base.Optional;

public class SynchronizePosTx extends AbstractSynchronizer {

	private final PosTxDAO txDAO;
	private final KassenVorgangDAO vorgangsDao;


	public SynchronizePosTx(PosTxDAO txDAO, KassenVorgangDAO vorgangsDao) {
		this.txDAO = txDAO;
		this.vorgangsDao = vorgangsDao;
	}


	public List<PosTx> fetchNewTx() throws Exception {
		Optional<DateTime> maxDatum = Optional.fromNullable(txDAO.getMaxDatum());
		Optional<Integer> maxId = Optional.fromNullable(txDAO.getMaxId());

		long id = maxId.or(0);

		List<KassenVorgang> vorgs = vorgangsDao.fetchAllAfter(maxDatum.or(new DateTime(2014,1,1,0,0)));

		// Neue Kassenvorgänge holen und speichern
		List<PosTx> res = new ArrayList<>();
		for (KassenVorgang vorg : vorgs) {
			if (vorg.getGesamt() == null || vorg.getMWSt() == null) {
				// ein seltsamer beleg, den merken wir uns für später
				PosTx tx = createNewTx(vorg, ++id);
				tx.setToBeIgnored(true); // vorläufig ignorieren
				tx.setToBeCheckedAgain(true); // später nochmal angucken
				res.add(tx);
			}
			if (vorg.getGesamt() != null && vorg.getMWSt() != null) {
				// es schein ein normaler Beleg zu sein
				PosTx tx = createNewTx(vorg, ++id);
				res.add(tx);
			}
		}
		txDAO.insertAll(res.iterator());

		// wir geben zurück was wir geholt haben
		return res;
	}
	
	public void updateExistingTx(List<PosTx> lastImportedTx) throws Exception {
		// alte Kassenvorgänge nochmals prüfen
		for (PosTx checker : lastImportedTx) {
			KassenVorgang orig = vorgangsDao.fetch(checker.getBelegNr(), checker.getBelegIdx());
			if (updateTx(orig, checker)) {
				// es hat sich was geändert;
				checker.setToBeCheckedAgain(false); // ist diese Entscheidung richtig? Man kann nur einmal korrigieren
				txDAO.update(checker);
			}
		}

		List<PosTx> toBeCheckedAgain = txDAO.fetchRevisitations(new DateTime().minusHours(48));
		for (PosTx checker : toBeCheckedAgain) {
			KassenVorgang orig = vorgangsDao.fetch(checker.getBelegNr(), checker.getBelegIdx());
			if (updateTx(orig, checker)) {
				// es hat sich was geändert;
				checker.setToBeCheckedAgain(false); // ist diese Entscheidung richtig? Man kann nur einmal korrigieren
				txDAO.update(checker);
			}
		}
		
	}


	private PosTx createNewTx(KassenVorgang vorg, long nextId) {
		PosTx tx = new PosTx();
		tx.setId(nextId);
		tx.setBelegNr(vorg.getBelegNr());
		tx.setBelegIdx(vorg.getLfdNummer());
		tx.setTimestamp(vorg.getDatum());
		tx.setToBeIgnored(false);
		tx.setToBeCheckedAgain(false);
		updateTx(vorg, tx);
		return tx;
	}


	private boolean updateTx(KassenVorgang vorg, PosTx tx) {
		boolean changed = false;
		ArticleGroup group = ArticleGroupMapping.mappingFrom(vorg);
		
		changed |= updStr(tx::setIsbn, tx.getIsbn(), vorg.getIsbn());
		changed |= updInt(tx::setCount, tx.getCount(), vorg.getMenge());
		// ean aus einer anderen Tabelle
		//					tx.setEan(vorg.getIsbn());
		// EK und rabatt aus einer anderen Tabelle !!
		// tx.setPur
		// tx.setRebate
		changed |= updStr(tx::setMatchCode, tx.getMatchCode(), vorg.getMatchCode());
		changed |= updStr(tx::setDescription, tx.getDescription(), vorg.getBezeichnung());
		changed |= updStr(tx::setArticleGroupKey, tx.getArticleGroupKey(), group.getKey() );
		changed |= updInt(tx::setArticleId, tx.getArticleId(), new BigDecimal(vorg.getArtikelident()));
		changed |= updStr(tx::setArticleKey, tx.getArticleKey(), vorg.getArtikelNummer());
		changed |= updEnum(tx::setTax, tx.getTax(), Tax.mappingFrom(vorg.getMWSt()));

		boolean nototalset = true;
		TxType type = TxType.mappingFrom(vorg.getVorgangsArt());

		if (group != null && "COUPON".equals(group.getType()) && 
				type.equals(TxType.TRADEIN) == false &&  type.equals(TxType.TRADEIN) == false) {
			// es ist ein Gutschein irgendeiner art.
			// aber ein nicht pos-systemgenerierter Gutschein
			if (vorg.getGesamt().signum() < 0) {
				// wir haben dafür was bezahlt, d.h. TRADIN, eingelöst
				type = TxType.TRADEIN;
			} else {
				// wir haben was dfür bekommen, d.h. wir haben einen Verkauft
				type = TxType.TRADEOUT;
			}
		} else {
			// der normalfall,
			if (type.equals(TxType.TRADEIN)) {
				// wir haben einen Gutschein angenommen
				// buchen wir es in voller Höhe, oder nur einen Teil davon? 
				// wir müssen uns den Beleg dazu angucken
				// gefährlich, hier gehen wir davon aus, dass gutscheine nur in zusammenhang mit anderen Tx angenommen werden.. 
				BigDecimal sumGesamtBeleg = vorgangsDao.fetchBelegSumOhne(vorg.getBelegNr(), vorg.getLfdNummer());
				BigDecimal amountPayed = vorgangsDao.fetchZahlbetrag(vorg.getBelegNr());
				if (sumGesamtBeleg == null || amountPayed == null) {
					tx.setToBeCheckedAgain(true); // hier stimmt was nicht, später nochmal angucken
				} else {
					// der Gutschein ist teil eines belges, es wurde was damit bezahlt
					if (sumGesamtBeleg.compareTo(vorg.getGesamt().abs()) <0  &&  amountPayed.intValue() == 0) {
						// es wurde nichts ausbezahlt, wir haben nur einen Teil des Gutscheins eingelöst.
						changed |= updMoney(tx::setPurchasePrice, tx.getPurchasePrice(), vorg.getGesamt());
						changed |= updMoney(tx::setSellingPrice, tx.getSellingPrice(), sumGesamtBeleg);  // der Betrag der damit gezahlt wurde
						changed |= updMoney(tx::setTotal, tx.getTotal(), sumGesamtBeleg.negate());
						nototalset = false;
					}
				}
			} 
		}
		changed |= updEnum(tx::setType, tx.getType(), type);
		
		if (nototalset) {
			changed |= updMoney(tx::setSellingPrice, tx.getSellingPrice(), vorg.getVK());
			changed |= updMoney(tx::setTotal, tx.getTotal(), vorg.getGesamt());
		}
		return changed;
	}


}
