package net.buchlese.bofc.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.AccrualPeriod;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;

public class BalanceComputer {

	public static void computeBalance(AccrualPeriod aw, List<PosCashBalance> bals) {
		aw.setAllBalances(bals);
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

		aw.setGoodsOut(bals.stream().map(PosCashBalance::getGoodsOut).reduce(0l, Long::sum));

		aw.setPayedInvoicesSum(bals.stream().map(PosCashBalance::getPayedInvoicesSum).reduce(0l, Long::sum));

		aw.setTicketCount(bals.stream().map(PosCashBalance::getTicketCount).reduce(0, Integer::sum));
		
		// validate all
		List<String> probs = new ArrayList<String>();
		for (List<PosCashBalance> pbals : aw.getBalances().values()) {
			pbals.sort( (x,y) -> x.getAbschlussId().compareTo(y.getAbschlussId()) );
			long curr = 0;
			for (PosCashBalance bal : pbals) {
				if (curr > 0 && bal.getCashStart() != curr) {
					probs.add("Kassenfangsbestandproblem (" + bal.getPointid() + ") am " + bal.getFirstCovered().toLocalDate() + " : " + bal.getCashStart() + " statt " + curr);
				}
				curr = bal.getCashEnd();
				if (Validator.validBalance(bal) == false) {
					probs.add("Unstimmigkeit bei Kassenbericht (" + bal.getPointid() + ") vom " + bal.getFirstCovered().toLocalDate());
				}
			}
		}
		aw.setProblems(probs);
	}
}
