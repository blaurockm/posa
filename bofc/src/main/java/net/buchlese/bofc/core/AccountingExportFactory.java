package net.buchlese.bofc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.view.StartView;

import org.joda.time.LocalDate;

import com.google.common.base.Optional;

public class AccountingExportFactory {

	private static int key = 100;
	private static Map<Integer, AccountingExport> cache = new HashMap<>();
	
	/**
	 * Erzeugt einen FiBu-Export
	 * 
	 * @param kasse
	 * @param from
	 * @param till
	 * @return
	 */
	public static synchronized AccountingExport createExport( int kasse, LocalDate from, Optional<LocalDate> till, PosCashBalanceDAO dao) {
		AccountingExport res = new AccountingExport();
		res.setExecDate(LocalDate.now());
		res.setFrom(from);
		res.setBalances(new ArrayList<PosCashBalance>());
		res.setPointId(kasse);
		res.setRefAccount(AccountingExportFile.getKassenkonto(kasse));
		res.setDescription("FiBuexport für Kasse " + kasse);
		String fromId = from.toString(PosCashBalanceDAO.IDFORMAT);
		Optional<String> tillId = till.transform( d -> d.toString(PosCashBalanceDAO.IDFORMAT));
		LocalDate ti = till.orNull();
		for (PosCashBalance bal :  dao.fetch(kasse, fromId, tillId)) {
			if (ti == null || bal.getLastCovered().toLocalDate().isAfter(ti)) {
				ti = bal.getLastCovered().toLocalDate();
			}
			res.getBalances().add(bal);
		}
		if (ti == null) {
			res.setTill(from.minusDays(1));
		} else {
			res.setTill(ti);
		}
		StartView.setFromDate(kasse, ti);
		res.setKey(key);
		cache.put(key++, res);
		return res;
	}
	
	public static AccountingExport getExport(int key) {
		return cache.get(key);
	}
	
	
}
