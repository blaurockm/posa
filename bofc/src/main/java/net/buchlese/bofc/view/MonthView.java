package net.buchlese.bofc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.buchlese.bofc.api.bofc.AccrualMonth;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;

public class MonthView extends AbstractBofcView {
	private final AccrualMonth month;
	
	public MonthView(AccrualMonth aw) {
		super("monthview.ftl");
		this.month = aw;
	}

	public AccrualMonth getMonth() {
		return month;
	}

	public long getMonthAbsorptionSulz() {
		return month.getBalances().getOrDefault(2,Collections.emptyList()).stream().map(PosCashBalance::getAbsorption).reduce(0l, Long::sum);
	}

	public long getMonthAbsorptionDornhan() {
		return month.getBalances().getOrDefault(1,Collections.emptyList()).stream().map(PosCashBalance::getAbsorption).reduce(0l, Long::sum);
	}

	public long getMonthTelecash() {
		long dornhan = month.getBalances().getOrDefault(1,Collections.emptyList()).stream().map( x -> x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l) ).reduce(0l, Long::sum);
		long sulz = month.getBalances().getOrDefault(2,Collections.emptyList()).stream().map( x -> 0 + x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l)).reduce(0l, Long::sum);
		return sulz + dornhan;
	}

	public Map<String, Long> getArticleGroupBalance() {
		Map<String, Long> res = new HashMap<String, Long>();
		month.getBalances().getOrDefault(1,Collections.emptyList()).stream().filter(x -> x.getArticleGroupBalance() != null).forEach( x -> x.getArticleGroupBalance().forEach( (k,v) -> res.merge(k, v, Long::sum)));
		month.getBalances().getOrDefault(2,Collections.emptyList()).stream().filter(x -> x.getArticleGroupBalance() != null).forEach( x -> x.getArticleGroupBalance().forEach( (k,v) -> res.merge(k, v, Long::sum)));
		return res;
	}

}
