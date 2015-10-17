package net.buchlese.posa.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.buchlese.posa.api.bofc.PosState;
import net.buchlese.posa.api.bofc.ServerState;
import net.buchlese.posa.core.PosStateGatherer;
import net.buchlese.posa.core.ServerStateGatherer;

import com.google.inject.Inject;

@Path("/state")
@Produces(MediaType.APPLICATION_JSON)
public class StateResource {

	@Inject private PosStateGatherer psg;
	@Inject private ServerStateGatherer ssg;

	public StateResource() {
		super();
	}
	
	@GET
	@Path("/pos")
	public PosState fetchP() {
		psg.gatherData();
		return psg.getState();
	}
	
	@GET
	@Path("/server")
	public ServerState fetchS() {
		ssg.gatherData();
		return ssg.getState();
	}

}
