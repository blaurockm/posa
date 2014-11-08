package net.buchlese.posa.view;

import io.dropwizard.views.View;
import net.buchlese.posa.PosAdapterConfiguration;

public class MobileView extends View {

	private final PosAdapterConfiguration cfg;
	
	public MobileView(PosAdapterConfiguration config) {
		super("mobile.ftl");
		this.cfg = config;
	}

	public String getPosName() {
		return cfg.getName();
	}
}
