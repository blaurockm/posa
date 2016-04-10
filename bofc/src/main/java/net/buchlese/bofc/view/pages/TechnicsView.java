package net.buchlese.bofc.view.pages;

import io.dropwizard.setup.Environment;

import java.text.DecimalFormat;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.view.AbstractBofcView;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Timer;

public class TechnicsView extends AbstractBofcView {

	private final Environment app;
	private final BackOfcConfiguration cfg;
	
	public TechnicsView( BackOfcConfiguration cfg, Environment app) {
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

	public String getWeekViewAvg() {
		Timer g = app.metrics().getTimers().get("net.buchlese.bofc.view.WeekView.rendering");
		if (g == null || g.getCount() <= 0) {
			return "--";
		}
		double val = g.getSnapshot().getMean() / 1000000000; // in seconds, not nanos
		return DecimalFormat.getNumberInstance().format(val) + " s";
	}

	public String getMonthViewAvg() {
		Timer g = app.metrics().getTimers().get("net.buchlese.bofc.view.MonthView.rendering");
		if (g == null || g.getCount() <= 0) {
			return "--";
		}
		double val = g.getSnapshot().getMean() / 1000000000; // in seconds, not nanos;
		return DecimalFormat.getNumberInstance().format(val) + " s";
	}

	public String getDbconfig() {
		return cfg.getBackOfficeDB().getUrl();
	}

}
