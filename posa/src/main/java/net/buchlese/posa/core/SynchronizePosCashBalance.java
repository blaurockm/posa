package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.util.List;

import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.pos.KassenAbschluss;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

public class SynchronizePosCashBalance extends AbstractSynchronizer {

	private final PosCashBalanceDAO cashBalanceDAO;
	private final PosTicketDAO ticketDAO;
	private final PosTxDAO txDAO;
	private final ObjectMapper om;

	private final KassenAbschlussDAO abschlussDao;


	public SynchronizePosCashBalance(PosCashBalanceDAO cashBalanceDAO, PosTicketDAO ticketDAO,
			PosTxDAO txDAO, KassenAbschlussDAO abschlussDao) {
		this.cashBalanceDAO = cashBalanceDAO;
		this.ticketDAO = ticketDAO;
		this.txDAO = txDAO;
		this.abschlussDao = abschlussDao;
		this.om = Jackson.newObjectMapper();
	}


	public synchronized  void execute() throws Exception {
		Optional<String> maxId = Optional.fromNullable(cashBalanceDAO.getMaxAbschlussId());

		List<KassenAbschluss> belege = abschlussDao.fetchAllAfter(maxId.or("20140101"));
		
		// convert KassenVorgang to posTx
		for (KassenAbschluss abschluss : belege) {
			if (abschluss.getIst() != null) {
				PosCashBalance bal = createNewBalance(abschluss);
				cashBalanceDAO.insert(bal);
			}
		}
		
		Optional<DateTime> maxCreTi = Optional.fromNullable(cashBalanceDAO.getMaxDatum());
		if (maxCreTi.isPresent()) {
			belege = abschlussDao.fetchAllModified(maxCreTi.get());
			for (KassenAbschluss abschluss : belege) {
				PosCashBalance oldBal = cashBalanceDAO.fetchForDate(abschluss.getAbschlussid());
				if (oldBal == null) {
					PosCashBalance bal = createNewBalance(abschluss);
					cashBalanceDAO.insert(bal);
				} else {
					updateBalance(abschluss, oldBal);
					cashBalanceDAO.update(oldBal);
				}
			}
		}
	}


	private PosCashBalance createNewBalance(KassenAbschluss abschluss) {
		CashBalance balComp = new CashBalance(txDAO, ticketDAO);
		PosCashBalance bal = balComp.computeBalance(abschluss.getVonDatum(), abschluss.getBisDatum());
		updateBalance(abschluss, bal);
		bal.setId(abschluss.getId());
		bal.setAbschlussId(abschluss.getAbschlussid());
		return bal;
	}

	private void updateBalance(KassenAbschluss abschluss, PosCashBalance bal) {
    	updDate(bal::setFirstCovered, bal.getFirstCovered(), abschluss.getVonDatum());
    	updDate(bal::setLastCovered, bal.getLastCovered(), abschluss.getBisDatum());
    	// revenue wird berechnet, das wird nicht aus dem abschluss übernommen
    	// balanceSheet natürlich ebenso
		updMoney(bal::setAbsorption, bal.getAbsorption(), abschluss.getAbschoepfung());
		updMoney(bal::setCashStart, bal.getCashStart(), abschluss.getAnfang());
		updMoney(bal::setCashEnd, bal.getCashEnd(), abschluss.getBar().subtract(abschluss.getAbschoepfung()));
		try {
			bal.setOrigAbschluss(om.writeValueAsString(abschluss));
		} catch (JsonProcessingException e) {
			// das  ist uns egal
		}
	}



	
	
	
}
