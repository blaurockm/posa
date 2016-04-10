package net.buchlese.bofc.view.pages;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class ExportView extends AbstractBofcView {

	private final BackOfcConfiguration cfg;

	private static LocalDate[] dates = new LocalDate[10];
	
	public ExportView(BackOfcConfiguration config) {
		super("export.ftl");
		this.cfg = config;
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
	
}
