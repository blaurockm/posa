package net.buchlese.bofc.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.joda.time.DateTime;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import net.buchlese.bofc.api.bofc.AccrualWeek;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import io.dropwizard.views.View;

public class WeekView extends View {
	private final AccrualWeek week;
	private final PosCashBalanceDAO balanceDao;
	
	public WeekView(AccrualWeek aw, PosCashBalanceDAO balanceDao) {
		super("weekview.ftl");
		this.week = aw;
		this.balanceDao = balanceDao;
	}

	public AccrualWeek getWeek() {
		return week;
	}

	public long getWeekAbsorptionSulz() {
		return week.getBalances().get(2).stream().map(PosCashBalance::getAbsorption).reduce(0l, Long::sum);
	}

	public long getWeekAbsorptionDornhan() {
		return week.getBalances().get(1).stream().map(PosCashBalance::getAbsorption).reduce(0l, Long::sum);
	}

	public long getWeekTelecash() {
		long dornhan = week.getBalances().get(1).stream().map( x -> x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l) ).reduce(0l, Long::sum);
		long sulz = week.getBalances().get(2).stream().map( x -> 0 + x.getPaymentMethodBalance().getOrDefault(PaymentMethod.TELE,0l)).reduce(0l, Long::sum);
		return sulz + dornhan;
	}

	public Map<String, Long> getArticleGroupBalance() {
		Map<String, Long> res = new HashMap<String, Long>();
		week.getBalances().get(1).stream().forEach( x -> x.getArticleGroupBalance().forEach( (k,v) -> res.merge(k, v, Long::sum)));
		week.getBalances().get(2).stream().forEach( x -> x.getArticleGroupBalance().forEach( (k,v) -> res.merge(k, v, Long::sum)));
		return res;
	}
	
	
	public TemplateMethodModelEx getMoney() {
    	return new TemplateMethodModelEx() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() != 1) {
					throw new TemplateModelException("nur ein int als Argument");
				}
				if (args.get(0) == null) {
					return "0,00 EUR";
				}
				if (args.get(0) instanceof TemplateNumberModel) {
					return String.format("%,.2f EUR", ((TemplateNumberModel)args.get(0)).getAsNumber().intValue() / 100.0d);
				}
				return "";
			}
		};
    }
	
    public TemplateMethodModelEx getLocalDate() {
    	return new TemplateMethodModelEx() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object exec(List args) throws TemplateModelException {
				if (args.size() != 1) {
					throw new TemplateModelException("nur ein DateTime als Argument");
				}
				return ((DateTime)((BeanModel)args.get(0)).getWrappedObject()).toLocalDate();
			}
		};
    }

}
