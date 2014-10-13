package net.buchlese.posa.resources;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.view.AppView;
import net.buchlese.posa.view.MobileView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final PosAdapterConfiguration cfg;
	
	public AppResource(PosAdapterConfiguration config) {
		this.cfg = config;
	}

	@GET
	public View getApp(@HeaderParam("user-agent") String userAgent ) {
		if (userAgent.matches("Android") ||
			userAgent.matches("iPhone") ||
			userAgent.matches("iPad")) {
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
