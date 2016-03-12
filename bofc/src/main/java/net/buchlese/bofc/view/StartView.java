package net.buchlese.bofc.view;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import io.dropwizard.views.View;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class StartView extends View {

	private final BackOfcConfiguration cfg;
	private final PosInvoiceDAO daoInv;

	private static LocalDate[] dates = new LocalDate[10];
	
	public StartView(BackOfcConfiguration config, PosInvoiceDAO daoInv) {
		super("start.ftl", Charset.forName("UTF-8"));
		this.cfg = config;
		this.daoInv = daoInv;
	}

	public String getPosName() {
		cfg.getMetricsFactory();
		return "Backend";
	}
	
	public String getDornhanFrom() {
		return getFromDate(1);
	}
	public String getSulzFrom() {
		return getFromDate(2);
	}
	public String getSchrambergFrom() {
		return getFromDate(3);
	}

	public String getDornhanTill() {
		return "";
	}
	public String getSulzTill() {
		return "";
	}
	public String getSchrambergTill() {
		return "";
	}

	public String getFromDate(int kasse) {
		if (dates[kasse] != null) {
			return dates[kasse].toString("yyyy-MM-dd");
		}
		return "";
	}
	
	public static void setFromDate(Integer kasse, LocalDate ti) {
		if (ti != null) {
			dates[kasse] = ti.plusDays(1);
		}
	}

	public List<PosInvoice> getInvoices() {
		List<PosInvoice> res = daoInv.fetchAllAfter(LocalDate.now().minusDays(10).toDateTimeAtStartOfDay().toDate());
		Collections.reverse(res);
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
