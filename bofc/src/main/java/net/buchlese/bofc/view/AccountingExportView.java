package net.buchlese.bofc.view;

import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.Tax;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

public class AccountingExportView extends AbstractBofcView {
	private final AccountingExport export;
	@SuppressWarnings("deprecation")
	private final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();

	public AccountingExportView(AccountingExport ae) {
		super("accexport.ftl");
		this.export = ae;
	}

	public AccountingExport getExport() {
		return export;
	}
	
	public String getDescription() {
		return export.getDescription();
	}

	public String getAccount() {
		return String.valueOf(export.getRefAccount());
	}
	
	public long getCashStart() {
		return export.getBalances().get(0).getCashStart();
	}

	public long getCashEnd() {
		return export.getBalances().get(export.getBalances().size()-1).getCashEnd();
	}

	public long getAbsorptionSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getAbsorption).sum();
	}

	public long getTelecashSum() {
		return export.getBalances().stream().mapToLong(x -> x.getPaymentMethodBalance().get(PaymentMethod.TELE)).sum();
	}

	public long getRevenueSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getRevenue).sum();
	}

	public long getCashInSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getCashInSum).sum();
	}

	public long getCashOutSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getCashOutSum).sum();
	}

	public long getInvoicesPayedSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getPayedInvoicesSum).sum();
	}

	public long getInvoicesSum() {
		return export.getInvoices().stream().mapToLong(PosInvoice::getAmount).sum();
	}

	public long getCouponsInSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getCouponTradeIn).sum();
	}

	public long getCouponsOutSum() {
		return export.getBalances().stream().mapToLong(PosCashBalance::getCouponTradeOut).sum();
	}

	public long getTelecashForBalance(PosCashBalance bal) {
		return bal.getPaymentMethodBalance().get(PaymentMethod.TELE);
	}
	
	public List<String> getPayedInvoices() {
		return export.getBalances().stream().flatMap(x -> x.getPayedInvoices().keySet().stream()).collect(Collectors.toList());
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
					return "0,00 €";
				}
				if (args.get(0) instanceof TemplateNumberModel) {
					return String.format("%,.2f €", ((TemplateNumberModel)args.get(0)).getAsNumber().intValue() / 100.0d);
				}
				return "";
			}
		};
    }

}
