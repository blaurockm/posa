package net.buchlese.bofc.view;

import io.dropwizard.views.View;
import net.buchlese.bofc.BackOfcConfiguration;

public class AppView extends View {

	private final BackOfcConfiguration cfg;
	
	public AppView(BackOfcConfiguration config) {
		super("app.ftl");
		this.cfg = config;
	}

	public String getPosName() {
		cfg.getMetricsFactory();
		return "Backend";
	}
}
