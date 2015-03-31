package net.buchlese.bofc.core;

import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.AccrualWeek;
import net.buchlese.bofc.api.bofc.PaymentMethod;
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

		aw.setAbsorption(bals.stream().map(PosCashBalance::getAbsorption).reduce(0l,  Long::sum));
		aw.setTelecash(bals.stream().map( x -> x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l) ).reduce(0l, Long::sum));
		aw.setCash(bals.stream().map( x -> x.getPaymentMethodBalance().getOrDefault(PaymentMethod.CASH,0l) ).reduce(0l, Long::sum));

		aw.setCouponTradeIn(bals.stream().map(PosCashBalance::getCouponTradeIn).reduce(0l, Long::sum));
		aw.setCouponTradeOut(bals.stream().map(PosCashBalance::getCouponTradeOut).reduce(0l, Long::sum));

		aw.setCashInSum(bals.stream().map(PosCashBalance::getCashInSum).reduce(0l, Long::sum));
		aw.setCashOutSum(bals.stream().map(PosCashBalance::getCashOutSum).reduce(0l, Long::sum));

		aw.setPayedInvoicesSum(bals.stream().map(PosCashBalance::getPayedInvoicesSum).reduce(0l, Long::sum));

		aw.setTicketCount(bals.stream().map(PosCashBalance::getTicketCount).reduce(0, Integer::sum));
		
		return aw;
	}
}
