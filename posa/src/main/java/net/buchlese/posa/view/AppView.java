package net.buchlese.posa.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.LocalDate;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import io.dropwizard.views.View;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.bofc.PosIssueSlip;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

public class AppView extends View {

	private final PosAdapterConfiguration cfg;
	private final PosCashBalanceDAO dao;
	private final PosInvoiceDAO daoInv;

	private static Map<String, PosCashBalance> balances;
	private static Instant cacheInstant = null;

	public AppView(PosAdapterConfiguration config, PosCashBalanceDAO dao, PosInvoiceDAO daoInv) {
		super("app.ftl");
		this.cfg = config;
		this.dao = dao;
		this.daoInv = daoInv;
}

	public String getPosName() {
		return cfg.getName();
	}

	public String getPointId() {
		return String.valueOf(cfg.getPointOfSaleId());
	}

	public List<LocalDate> getFortnight() {
		List<LocalDate> res = new ArrayList<LocalDate>();
		LocalDate now = LocalDate.now();
		for (int i = 0; i < cfg.getDaysBack(); i++) {
			res.add(now.minusDays(i));
		}
		return res;
	}

	public List<PosInvoice> getInvoices() {
		List<PosInvoice> res = daoInv.fetchAllAfter(LocalDate.now().minusDays(cfg.getDaysBack()).toDateTimeAtStartOfDay().toDate());
		Collections.reverse(res);
		return res;
	}

	public List<PosIssueSlip> getIssueSlips() {
		List<PosIssueSlip> res = daoInv.fetchAllIssueSlipsAfter(LocalDate.now().minusDays(cfg.getDaysBack()).toDateTimeAtStartOfDay().toDate());
		Collections.reverse(res);
		return res;
	}

	public Map<String, PosCashBalance> getBalances() {
		fetchBalances();
		return balances;
	}
	
	public boolean isWorkday(String x) {
		return LocalDate.parse(x).getDayOfWeek() != DateTimeConstants.SUNDAY;
	}
	
	private synchronized void fetchBalances() {
		if (cacheInstant == null || cacheInstant.isBefore(Instant.now().minus(Duration.standardMinutes(30)))) {
			// cache nis noch nicht da oder zu alt..
			LocalDate now = LocalDate.now().minusWeeks(2);
			List<PosCashBalance> bals = dao.fetchAllAfter(now.toString(PosCashBalanceDAO.IDFORMAT));
			balances = new HashMap<String, PosCashBalance>();
			for (PosCashBalance bal : bals) {
				balances.put(bal.getLastCovered().toLocalDate().toString(), bal);
			}
			cacheInstant = Instant.now();
		}
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
