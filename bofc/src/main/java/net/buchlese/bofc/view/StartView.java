package net.buchlese.bofc.view;

import net.buchlese.bofc.BackOfcConfiguration;

public class StartView extends AbstractBofcView {

	private final BackOfcConfiguration cfg;

	
	public StartView(BackOfcConfiguration config) {
		super("start.ftl");
		this.cfg = config;
	}

	public String getPosName() {
		cfg.getMetricsFactory();
		return "Backend";
	}
	
}
