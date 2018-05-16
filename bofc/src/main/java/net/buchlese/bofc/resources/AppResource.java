package net.buchlese.bofc.resources;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import io.dropwizard.views.View;
import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.view.StartView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResource {

	private final BackOfcConfiguration cfg;
	
	@Inject
	public AppResource(BackOfcConfiguration config) {
		this.cfg = config;
	}

	@GET
	public View getApp(@HeaderParam("user-agent") String userAgent ) {
		return getDesktop();
	}
	@GET
	@Path("/start")
	public View getDesktop() {
		return new StartView(cfg);
	}

}
