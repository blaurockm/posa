package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.view.menu.MenuView;
import net.buchlese.bofc.view.menu.NavigationView;

import com.google.inject.Inject;

@Path("/menu")
@Produces(MediaType.TEXT_HTML)
public class MenuResource {


	@Inject
	public MenuResource() {
	}

	@GET
	@Path("/index")
	public View getCommands() {
		return new MenuView();
	}

	@GET
	@Path("/navigation")
	public View getMappings() {
		return new NavigationView();
	}
}
