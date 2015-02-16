package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.bofc.Tax;
import net.buchlese.posa.api.bofc.TxType;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.core.SyncTimer.BulkLoadDetails;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class SynchronizePosTx extends AbstractSynchronizer {

	private final PosTxDAO txDAO;
	private final KassenVorgangDAO vorgangsDao;

	public SynchronizePosTx(PosTxDAO txDAO, KassenVorgangDAO vorgangsDao) {
		this.txDAO = txDAO;
		this.vorgangsDao = vorgangsDao;
	}

	/**
	 * synchronisiere neu angelegte Tx
	 * @param syncStart
	 * @return
	 */
	public List<PosTx> fetchNewTx(LocalDate syncStart) {
		Optional<DateTime> maxDatum = Optional.fromNullable(txDAO.getMaxDatum());

		List<KassenVorgang> vorgs = vorgangsDao.fetchAllAfter(maxDatum.or(syncStart.toDateTimeAtStartOfDay()));

		// Neue Kassenvorgänge holen und speichern
		// wir geben zurück was wir geholt haben
		return createNewTx(vorgs);
	}

	/**
	 * erzeuge neue Tx in dem geg. Zeitraum
	 * 
	 * @param bulkLoad
	 */
	public void doBulkLoad(BulkLoadDetails bulkLoad) {
		Logger log = LoggerFactory.getLogger("TxBulkLoad");
		log.info("doing " + bulkLoad);
		List<KassenVorgang> vorgs = vorgangsDao.fetchAllBetween(bulkLoad.getFrom().toDateTimeAtStartOfDay(), bulkLoad.getTill().toDateTimeAtStartOfDay().plusDays(1));
		createNewTx(vorgs);
		log.info("done with " + bulkLoad);
	}
	
	/**
	 * lege Tx für die geg. KassenVorgänge an
	 * 
	 * @param vorgs
	 * @return
	 */
	public List<PosTx> createNewTx(List<KassenVorgang> vorgs) {
		Optional<Integer> maxId = Optional.fromNullable(txDAO.getMaxId());

		long id = maxId.or(0);
		List<PosTx> res = new ArrayList<>();
		for (KassenVorgang vorg : vorgs) {
			if (vorg.getGesamt() != null && vorg.getMWSt() != null) {
				PosTx tx = createNewTx(vorg, ++id);
				res.add(tx);
			}
		}
		txDAO.insertAll(res.iterator());
		return res;
	}
	
	
	public void updateLast10Tx() throws Exception {
		// alte Kassenvorgänge nochmals prüfen
    	List<KassenVorgang> lastVorg = vorgangsDao.fetchLast();
		for (KassenVorgang orig: lastVorg) {
			PosTx checker = txDAO.fetch(orig.getBelegNr(), orig.getLfdNummer());
			if (checker != null && updateTx(orig, checker)) {
				// es hat sich was geändert;
				txDAO.update(checker);
			}
		}
	}


	public PosTx createNewTx(KassenVorgang vorg, long nextId) {
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


	public boolean updateTx(KassenVorgang vorg, PosTx tx) {
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

		boolean stillnototalset = true;
		TxType type = TxType.mappingFrom(vorg.getVorgangsArt());

		if (group != null && "COUPON".equals(group.getType()) && 
				type.equals(TxType.TRADEIN) == false &&  type.equals(TxType.TRADEOUT) == false) {
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
				List<KassenVorgang> allVorgs = vorgangsDao.fetchForBeleg(vorg.getBelegNr(), vorg.getKassenNr());
				BigDecimal sumWarenBetrag = allVorgs.stream().filter(v -> v.getLfdNummer() != vorg.getLfdNummer()).map(KassenVorgang::getGesamt).reduce(BigDecimal.ZERO, BigDecimal::add); // (a,b) -> a.add(b)
				BigDecimal amountPayed = vorgangsDao.fetchZahlbetrag(vorg.getBelegNr());
				if (amountPayed == null) {
					tx.setToBeCheckedAgain(true); // hier stimmt was nicht, später nochmal angucken
				} else {
					// der Gutschein ist teil eines belges, es wurde was damit bezahlt
					BigDecimal gutschBetrag = vorg.getGesamt().abs();
					if (sumWarenBetrag.compareTo(gutschBetrag) <0  &&  amountPayed.movePointRight(2).setScale(0,RoundingMode.HALF_UP).longValue() == 0l) {
						OptionalInt maxGutschIdx = allVorgs.stream().filter(v -> (v.getIsbn() != null && v.getIsbn().startsWith("Gutschein"))).mapToInt(KassenVorgang::getLfdNummer).max(); // die IndexNummer des letzten Gutscheins im Beleg
						// es wurde nichts ausbezahlt, wir haben nur einen Teil des Gutscheins eingelöst.
						if (maxGutschIdx.isPresent() && vorg.getLfdNummer() == maxGutschIdx.getAsInt()) {
							// und wir sind der letzte Gutschein des Belegs, unser Betrag wird modifiziert
							// alle anderen bleiben gleich..
							changed |= updMoney(tx::setPurchasePrice, tx.getPurchasePrice(), gutschBetrag.negate());
							changed |= updMoney(tx::setSellingPrice, tx.getSellingPrice(), sumWarenBetrag);  // der Betrag der damit gezahlt wurde
							changed |= updMoney(tx::setTotal, tx.getTotal(), sumWarenBetrag.negate());
							stillnototalset = false;
						}
					}
				}
			} 
		}
		changed |= updEnum(tx::setType, tx.getType(), type);
		
		if (stillnototalset) {
			changed |= updMoney(tx::setSellingPrice, tx.getSellingPrice(), vorg.getVK());
			changed |= updMoney(tx::setTotal, tx.getTotal(), vorg.getGesamt());
		}
		return changed;
	}




}
