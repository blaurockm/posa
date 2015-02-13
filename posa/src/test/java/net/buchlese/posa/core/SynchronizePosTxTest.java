package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Ignore;

public class SynchronizePosTxTest {

	@Ignore
	public void missingCentTest1() throws Exception {
		
		SynchronizePosTx sptx = new SynchronizePosTx(getTxDao(), getTicketDao(), LocalDate.now());
		sptx.fetchNewTx();
	}
	
	private KassenVorgangDAO getTicketDao() {
		return new KassenVorgangDAO() {

			@Override
			public List<KassenVorgang> fetchForBeleg(KassenBeleg beleg) {
				return null;
			}

			@Override
			public List<KassenVorgang> fetchAllAfter(DateTime maxDatum) {
				return null;
			}

			@Override
			public List<KassenVorgang> fetchLast() {
				return null;
			}

			@Override
			public BigDecimal fetchZahlbetrag(int belegNr) {
				return null;
			}

			@Override
			public List<KassenVorgang> fetchForBeleg(int belegNr, int lfdNummer) {
				return null;
			}

			@Override
			public KassenVorgang fetch(long belegNr, int belegIdx) {
				return null;
			}

			@Override
			public List<KassenVorgang> fetchAllBetween(DateTime fromDate, DateTime tillDate) {
				return null;
			}
		};
	}
	private PosTxDAO getTxDao() {
		return new PosTxDAO() {
			
			@Override
			public void update(PosTx checker) {
				
			}
			
			@Override
			public void insertAll(Iterator<PosTx> transactions) {
				
			}
			
			@Override
			public Integer getMaxId() {
				return null;
			}
			
			@Override
			public DateTime getMaxDatum() {
				return null;
			}
			
			@Override
			public List<PosTx> fetchRevisitations(DateTime datum) {
				return null;
			}
			
			@Override
			public PosTx fetch(int belegNr, int lfdNummer) {
				return null;
			}
			
			@Override
			public List<PosTx> fetch(DateTime vonDatum, DateTime bisDatum) {
				return null;
			}

			@Override
			public void deleteTxBetween(DateTime vonDatum, DateTime bisDatum) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
}
