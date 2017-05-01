package net.buchlese.posa.core;

import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PosState;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.jdbi.bofc.DynamicStateDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PosStateGatherer {

	@Inject private PosTxDAO txDAO;
	@Inject private KassenVorgangDAO vorgangDao;
	@Inject private DynamicStateDAO stateDao;
	
	private PosState lastState = new PosState();
	
	public void gatherData() {
		// jetzt die Tx neu Synchronisieren
		SynchronizePosTx syncTx = new SynchronizePosTx(txDAO, vorgangDao);
		List<KassenVorgang> vorgs = vorgangDao.fetchAllBetween(DateTime.now().withTimeAtStartOfDay(), DateTime.now());
		List<PosTx> posTxs = syncTx.convertVorgangToTx(vorgs);
		
		// jetzt die Tickets neu synchronisieren
//		SynchronizePosTicket syncTicket = new SynchronizePosTicket(ticketDAO, belegDao);
//		List<KassenBeleg> belgs = belegDao.fetchAllBetween(DateTime.now().withTimeAtStartOfDay(), DateTime.now());
//		List<PosTicket> posTickets= syncTicket.convertBelegToTicket(belgs);
		
		lastState = new PosState();
		lastState.setTimest(Instant.now());
		lastState.setStateDate(LocalDate.now());
		lastState.setRevenue(posTxs.stream().filter(x -> x.getTotal() != null).mapToLong(PosTx::getTotal).sum());
		lastState.setProfit(posTxs.stream().filter(x -> x.getTotal() != null && x.getArticleGroup() != null && x.getArticleGroup().getMarge() != null).mapToLong(x -> Long.valueOf((long)(x.getTotal()*x.getArticleGroup().getMarge()))   ).sum());
		lastState.setSyncStates(stateDao.getSyncStates());
		
		PosAdapterApplication.homingQueue.offer(lastState); // sync back home
	}

	public PosState getState() {
		return lastState;
	}

}
