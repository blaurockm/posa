package net.buchlese.bofc.core;

import java.util.List;

import net.buchlese.bofc.api.bofc.AccrualMonth;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;

import org.joda.time.DateTime;

public class MonthBalance {

	private PosCashBalanceDAO balanceDao;
	
	public MonthBalance(PosCashBalanceDAO balanceDao) {
		this.balanceDao = balanceDao;
	}

	public AccrualMonth computeBalance(DateTime from, DateTime till) {
		AccrualMonth aw = new AccrualMonth();
		
		aw.setMonth(from.getMonthOfYear());
		aw.setYear(from.getYear());
		aw.setFirstDay(from);
		aw.setLastDay(till);
		
		List<PosCashBalance> bals = balanceDao.fetchAllAfter(from.toString("yyyyMMdd"), till.toString("yyyyMMdd"));

		BalanceComputer.computeBalance(aw, bals);
		
		return aw;
	}
}
