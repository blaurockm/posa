package net.buchlese.bofc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.buchlese.bofc.api.bofc.AccrualDay;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;

public class DayView extends AbstractBofcView {
	private final AccrualDay week;
	
	public DayView(AccrualDay aw) {
		super("dayview.ftl");
		this.week = aw;
	}

	public AccrualDay getDay() {
		return week;
	}

	public long getWeekAbsorptionSulz() {
		return week.getBalances().getOrDefault(2,Collections.emptyList()).stream().map(PosCashBalance::getAbsorption).reduce(0l, Long::sum);
	}

	public long getWeekAbsorptionDornhan() {
		return week.getBalances().getOrDefault(1,Collections.emptyList()).stream().map(PosCashBalance::getAbsorption).reduce(0l, Long::sum);
	}

	public long getWeekTelecash() {
		long dornhan = week.getBalances().getOrDefault(1,Collections.emptyList()).stream().map( x -> x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l) ).reduce(0l, Long::sum);
		long sulz = week.getBalances().getOrDefault(2,Collections.emptyList()).stream().map( x -> 0 + x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l)).reduce(0l, Long::sum);
		return sulz + dornhan;
	}

	public Map<String, Long> getArticleGroupBalance() {
		Map<String, Long> res = new HashMap<String, Long>();
		week.getBalances().getOrDefault(1,Collections.emptyList()).stream().filter(x -> x.getArticleGroupBalance() != null).forEach( x -> x.getArticleGroupBalance().forEach( (k,v) -> res.merge(k, v, Long::sum)));
		week.getBalances().getOrDefault(2,Collections.emptyList()).stream().filter(x -> x.getArticleGroupBalance() != null).forEach( x -> x.getArticleGroupBalance().forEach( (k,v) -> res.merge(k, v, Long::sum)));
		return res;
	}
	
	

}
