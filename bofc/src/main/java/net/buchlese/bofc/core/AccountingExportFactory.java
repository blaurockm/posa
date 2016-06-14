package net.buchlese.bofc.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.pages.ExportView;

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
		res.setInvoices(new ArrayList<PosInvoice>());
		res.setPointId(kasse);
		switch (kasse) {
		case 1 : res.setRefAccount(1600);
		res.setDescription("FiBuexport für Kasse Dornhan (" + kasse + ")");
		break;
		case 2 : res.setRefAccount(1610); 
		res.setDescription("FiBuexport für Kasse Sulz(" + kasse + ")");
		break;
		case 3 : res.setRefAccount(1620); 
		res.setDescription("FiBuexport für Kasse Schramberg (" + kasse + ")");
		break;
		default : res.setRefAccount(0);
		}
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
		ExportView.setFromDate(kasse, ti);
		res.setKey(key);
		cache.put(key++, res);
		return res;
	}

	/**
	 * Erzeugt einen FiBu-Export
	 * 
	 * @param kasse
	 * @param from
	 * @param till
	 * @return
	 */
	public static synchronized AccountingExport createExport( int kasse, LocalDate from, Optional<LocalDate> till, PosInvoiceDAO dao) {
		AccountingExport res = new AccountingExport();
		res.setExecDate(LocalDate.now());
		res.setFrom(from);
		res.setInvoices(new ArrayList<PosInvoice>());
		res.setBalances(new ArrayList<PosCashBalance>());
		res.setPointId(kasse);
		switch (kasse) {
		case 1 : res.setRefAccount(1600);
		res.setDescription("FiBuexport für Kasse Dornhan (" + kasse + ")");
		break;
		case 2 : res.setRefAccount(1610); 
		res.setDescription("FiBuexport für Kasse Sulz(" + kasse + ")");
		break;
		case 3 : res.setRefAccount(1620); 
		res.setDescription("FiBuexport für Kasse Schramberg (" + kasse + ")");
		break;
		default : res.setRefAccount(0);
		}
		Optional<java.util.Date> tillD = till.transform( d -> d.toDate());
		LocalDate ti = till.orNull();
		for (PosInvoice inv :  dao.fetch(kasse,from.toDate(),  tillD)) {
			if (ti == null || inv.getDate().isAfter(ti)) {
				ti = inv.getDate();
			}
			res.getInvoices().add(inv);
		}
		if (ti == null) {
			res.setTill(from.minusDays(1));
		} else {
			res.setTill(ti);
		}
		ExportView.setFromDate(kasse, ti);
		res.setKey(key);
		cache.put(key++, res);
		return res;
	}

	/**
	 * Erzeugt einen FiBu-Export
	 * 
	 * @param kasse
	 * @param from
	 * @param till
	 * @return
	 */
	public static synchronized AccountingExport createExport( int kasse, LocalDate from, Optional<LocalDate> till, PosCashBalanceDAO dao, PosInvoiceDAO daoInv) {
		AccountingExport res = new AccountingExport();
		res.setExecDate(LocalDate.now());
		res.setFrom(from);
		res.setBalances(new ArrayList<PosCashBalance>());
		res.setInvoices(new ArrayList<PosInvoice>());
		res.setPointId(kasse);
		switch (kasse) {
		case 1 : res.setRefAccount(1600);
		res.setDescription("FiBuexport für Kasse Dornhan (" + kasse + ")");
		break;
		case 2 : res.setRefAccount(1610); 
		res.setDescription("FiBuexport für Kasse Sulz(" + kasse + ")");
		break;
		case 3 : res.setRefAccount(1620); 
		res.setDescription("FiBuexport für Kasse Schramberg (" + kasse + ")");
		break;
		default : res.setRefAccount(0);
		}
		String fromId = from.toString(PosCashBalanceDAO.IDFORMAT);
		Optional<String> tillId = till.transform( d -> d.toString(PosCashBalanceDAO.IDFORMAT));
		Optional<java.util.Date> tillD = till.transform( d -> d.toDate());
		LocalDate ti = till.orNull();
		for (PosCashBalance bal :  dao.fetch(kasse, fromId, tillId)) {
			if (ti == null || bal.getLastCovered().toLocalDate().isAfter(ti)) {
				ti = bal.getLastCovered().toLocalDate();
			}
			res.getBalances().add(bal);
		}
		for (PosInvoice inv :  daoInv.fetch(kasse,from.toDate(),  tillD)) {
			res.getInvoices().add(inv);
		}
		if (ti == null) {
			res.setTill(from.minusDays(1));
		} else {
			res.setTill(ti);
		}
		ExportView.setFromDate(kasse, ti);
		res.setKey(key);
		cache.put(key++, res);
		return res;
	}

	public static AccountingExport getExport(int key) {
		return cache.get(key);
	}

	public static Collection<AccountingExport> getExports() {
		return cache.values();
	}
	
	
}
