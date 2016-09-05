package net.buchlese.posa.view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Timer;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.core.CommandTimer;
import net.buchlese.posa.core.SendTimer;
import net.buchlese.posa.core.SyncTimer;

public class TechnicsView extends View {

	private final Environment app;
	private final PosAdapterConfiguration cfg;
	
	public TechnicsView( PosAdapterConfiguration cfg, Environment app) {
		super("technics.ftl");
		this.app = app;
		this.cfg = cfg;
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

	public String getMobileViewAvg() {
		Timer g = app.metrics().getTimers().get("net.buchlese.posa.view.MobileView.rendering");
		if (g == null || g.getCount() <= 0) {
			return "--";
		}
		double val = g.getSnapshot().getMean() / 1000000000; // in seconds, not nanos
		return DecimalFormat.getNumberInstance().format(val) + " s";
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
	
	
	public String getDeskViewAvg() {
		Timer g = app.metrics().getTimers().get("net.buchlese.posa.view.AppView.rendering");
		if (g == null || g.getCount() <= 0) {
			return "--";
		}
		double val = g.getSnapshot().getMean() / 1000000000; // in seconds, not nanos;
		return DecimalFormat.getNumberInstance().format(val) + " s";
	}

	public String getSyncTimeMax() {
		return new Duration(SyncTimer.maxDuration).toString();
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

}
