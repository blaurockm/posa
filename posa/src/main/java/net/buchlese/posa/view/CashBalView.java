package net.buchlese.posa.view;

import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import io.dropwizard.views.View;

import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.api.bofc.PaymentMethod;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.Tax;

import org.joda.time.DateTime;

public class CashBalView extends View {

	private final PosCashBalance cashBal;
	private final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
	
	public CashBalView(PosCashBalance cashBal) {
		super("cashbal.ftl");
		this.cashBal = cashBal;
	}

	public String getPosName() {
		return PosAdapterApplication.getPosName();
	}
	
	public TemplateHashModel getBalance() throws TemplateModelException {
    	wrapper.setSimpleMapWrapper(true);
		return (TemplateHashModel) wrapper.wrap(cashBal);
	}

    public TemplateHashModel getPaymentMethod() throws TemplateModelException {
    	TemplateHashModel enumModels = wrapper.getEnumModels();
    	return (TemplateHashModel) enumModels.get(PaymentMethod.class.getName());  
    }

    public TemplateHashModel getTax() throws TemplateModelException {
    	TemplateHashModel enumModels = wrapper.getEnumModels();
    	return (TemplateHashModel) enumModels.get(Tax.class.getName());  
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
				if (args.get(0) == null) {
					return "existiert nicht";
				}
				return ((DateTime)((BeanModel)args.get(0)).getWrappedObject()).toLocalDate();
			}
		};
    }

}
