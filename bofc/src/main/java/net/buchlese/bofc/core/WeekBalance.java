package net.buchlese.bofc.core;

import java.util.List;
import java.util.stream.Collectors;

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
		
		aw.setBalances(bals.stream().collect(Collectors.groupingBy(PosCashBalance::getPointid)));

		aw.setProfit(bals.stream().map(PosCashBalance::getProfit).reduce(0l, Long::sum));
		aw.setRevenue(bals.stream().map(PosCashBalance::getRevenue).reduce(0l, Long::sum));
		
		aw.setTicketCount(bals.stream().map(PosCashBalance::getTicketCount).reduce(0, Integer::sum));
		
		return aw;
	}
}
