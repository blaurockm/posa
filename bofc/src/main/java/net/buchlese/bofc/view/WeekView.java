package net.buchlese.bofc.view;

import java.util.List;

import org.joda.time.DateTime;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import net.buchlese.bofc.api.bofc.AccrualWeek;
import io.dropwizard.views.View;

public class WeekView extends View {

	private final AccrualWeek week;
	
	public WeekView(AccrualWeek aw) {
		super("weekview.ftl");
		this.week = aw;
	}

	public AccrualWeek getWeek() {
		return week;
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
