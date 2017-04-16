package net.buchlese.posa.view;

import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.core.CommandTimer;
import net.buchlese.posa.core.SendTimer;
import net.buchlese.posa.core.SyncTimer;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.codahale.metrics.Gauge;

public class IndexView extends View {

	private final PosAdapterConfiguration cfg;
	@SuppressWarnings("unused")
	private final PosCashBalanceDAO dao;
	@SuppressWarnings("unused")
	private final PosInvoiceDAO daoInv;
	private final Environment app;


	public IndexView(PosAdapterConfiguration config, PosCashBalanceDAO dao, PosInvoiceDAO daoInv, Environment app) {
		super("index.ftl");
		this.app = app;
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

	public String getJvmVersion() {
		return System.getProperty("java.version") + " " + System.getProperty("java.vendor") +  " on " + System.getProperty("os.arch");
	}
	
	public String getJvmMemory() {
		Gauge<?> used = app.metrics().getGauges().get("jvm.memory.total.used");
		double usedval = ((Number)used.getValue()).doubleValue() / 1024 / 1024; // in Megabytes please
		Gauge<?> comit = app.metrics().getGauges().get("jvm.memory.total.committed");
		double comitval = ((Number)comit.getValue()).doubleValue() / 1024 / 1024; // in Megabytes please
		Gauge<?> max = app.metrics().getGauges().get("jvm.memory.total.max");
		double maxval = ((Number)max.getValue()).doubleValue() / 1024 / 1024; // in Megabytes please
		return DecimalFormat.getNumberInstance().format(usedval) + " MB / " + DecimalFormat.getNumberInstance().format(comitval) + " MB / " +  DecimalFormat.getNumberInstance().format(maxval) + " MB";
	}

	public List<String> getHomingQueue() {
		return PosAdapterApplication.homingQueue.stream().map(Object::toString).collect(Collectors.toList());
	}

	public List<String> getResyncQueue() {
		return PosAdapterApplication.resyncQueue.stream().map(Object::toString).collect(Collectors.toList());
	}

	public List<String> getCommandQueue() {
		return PosAdapterApplication.commandQueue.stream().map(Object::toString).collect(Collectors.toList());
	}

	public List<String> getProblems() {
		return PosAdapterApplication.problemMessages.stream().map(Object::toString).collect(Collectors.toList());
	}

	public String getLastHomingRun() {
		return new DateTime(SendTimer.lastHomingRun).toString();
	}

	public String getLastSyncRun() {
		return new DateTime(SyncTimer.lastRun).toString();
	}

	public String getLastCommandRun() {
		return new DateTime(CommandTimer.lastRun).toString();
	}

	public String getLastSyncRunWithDbConnection() {
		return new DateTime(SyncTimer.lastRunWithDbConnection).toString();
	}
	
	
//	public String getDeskViewAvg() {
//		Timer g = app.metrics().getTimers().get("net.buchlese.posa.view.AppView.rendering");
//		if (g == null || g.getCount() <= 0) {
//			return "--";
//		}
//		double val = g.getSnapshot().getMean() / 1000000000; // in seconds, not nanos;
//		return DecimalFormat.getNumberInstance().format(val) + " s";
//	}

	public String getSyncTimeMax() {
		return new Duration(SyncTimer.maxDuration).toString();
	}

	public PosAdapterConfiguration getConfig() {
		return cfg;
	}
	
	public int getPointid() {
		return cfg.getPointOfSaleId();
	}

	public String getDbconfigPosa() {
		return cfg.getPointOfSaleDB().getUrl();
	}

	public String getDbconfigBofc() {
		return cfg.getBackOfficeDB().getUrl();
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
