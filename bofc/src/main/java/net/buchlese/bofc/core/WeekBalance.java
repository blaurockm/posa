package net.buchlese.bofc.core;

import java.util.List;

import net.buchlese.bofc.api.bofc.AccrualWeek;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;

import org.joda.time.DateTime;

public class WeekBalance {

	private PosCashBalanceDAO balanceDao;
	
	public WeekBalance(PosCashBalanceDAO balanceDao) {
		this.balanceDao = balanceDao;
	}

	public AccrualWeek computeBalance(DateTime from, DateTime till) {
		AccrualWeek aw = new AccrualWeek();
		
		aw.setWeek(from.getWeekOfWeekyear());
		aw.setYear(from.getYear());
		aw.setFirstDay(from);
		aw.setLastDay(till);
		
		List<PosCashBalance> bals = balanceDao.fetchAllAfter(from.toString("yyyyMMdd"), till.toString("yyyyMMdd"));

		BalanceComputer.computeBalance(aw, bals);
		
		return aw;
	}
}
