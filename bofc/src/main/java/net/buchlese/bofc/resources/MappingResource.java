package net.buchlese.bofc.resources;

import io.dropwizard.views.View;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.buchlese.bofc.api.bofc.Mapping;
import net.buchlese.bofc.jdbi.bofc.MappingDAO;
import net.buchlese.bofc.view.pages.MappingView;

import com.google.inject.Inject;

@Path("/mapping")
public class MappingResource {

	private MappingDAO dao;

	@Inject
	public MappingResource(MappingDAO dao) {
		super();
		this.dao = dao;
	}

	@POST
	@Path("/update")
	@Produces({"text/html"})
	public View updateMaping( @FormParam("point") Integer pi, @FormParam("deb") Integer deb, @FormParam("cust") Integer cust) {
		Mapping um = new Mapping();
		um.setPointid(pi);
		um.setCustomerId(cust);
		um.setDebitorId(deb);
		dao.insert(um);
		return new MappingView(Collections.emptyList());
	}

	@GET
	@Produces({"text/html"})
	public View showMappings( @QueryParam("point") Integer pi) {
		List<Mapping> res = dao.fetch(pi.intValue());

		return new MappingView(res);
	}
	
	
}
