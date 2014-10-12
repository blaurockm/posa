package net.buchlese.posa.view;

import io.dropwizard.views.View;
import net.buchlese.posa.PosAdapterConfiguration;

public class AppView extends View {

	private final PosAdapterConfiguration cfg;
	
	public AppView(PosAdapterConfiguration config) {
		super("app.ftl");
		this.cfg = config;
	}

	public String getPosName() {
		return cfg.getName();
	}
}
