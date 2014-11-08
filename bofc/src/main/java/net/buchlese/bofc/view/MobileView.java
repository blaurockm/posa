package net.buchlese.bofc.view;

import io.dropwizard.views.View;
import net.buchlese.bofc.BackOfcConfiguration;

public class MobileView extends View {

	private final BackOfcConfiguration cfg;
	
	public MobileView(BackOfcConfiguration config) {
		super("mobile.ftl");
		this.cfg = config;
	}

	public String getPosName() {
		return "Backend";
	}
}
