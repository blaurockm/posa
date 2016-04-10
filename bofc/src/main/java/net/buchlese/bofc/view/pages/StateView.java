package net.buchlese.bofc.view.pages;

import io.dropwizard.views.View;
import net.buchlese.bofc.BackOfcConfiguration;

public class StateView extends View {

	private final BackOfcConfiguration cfg;
	
	public StateView(BackOfcConfiguration config) {
		super("state.ftl");
		this.cfg = config;
	}

	public String getPosName() {
		cfg.getMetricsFactory();
		return "Backend";
	}
}
