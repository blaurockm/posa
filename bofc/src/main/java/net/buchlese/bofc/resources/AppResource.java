package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.view.AppView;
import net.buchlese.bofc.view.MobileView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final BackOfcConfiguration cfg;
	
	public AppResource(BackOfcConfiguration config) {
		this.cfg = config;
	}

	@GET
	public View getApp(@HeaderParam("user-agent") String userAgent ) {
		if (userAgent.contains("Android") ||
			userAgent.contains("iPhone") ||
			userAgent.contains("iPad")) {
			return getMobile();
		}
		return getDesktop();
	}

	@GET
	@Path("/mobile")
	public View getMobile() {
		return new MobileView(cfg);
	}

	@GET
	@Path("/app")
	public View getDesktop() {
		return new AppView(cfg);
	}

}