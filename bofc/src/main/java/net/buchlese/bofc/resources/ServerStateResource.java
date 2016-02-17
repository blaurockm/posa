package net.buchlese.bofc.resources;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.buchlese.bofc.api.bofc.PosState;
import net.buchlese.bofc.api.bofc.ServerState;

import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.slf4j.LoggerFactory;

@Path("/serverState")
public class ServerStateResource {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServerStateResource.class);

	private final static Map<Integer, LinkedList<ServerState>> states = new ConcurrentHashMap<>();
	private final static int MAXSIZE = 100;
	
	@POST
	@Path("/acceptState")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptState(ServerState state)  {
		try {
			LinkedList<ServerState> list = states.get(state.getPointid());
			if (list == null) {
				list = new LinkedList<ServerState>();
				states.put(state.getPointid(), list);
			}
			if (list.size() > MAXSIZE) {
				list.removeLast();
			}
			list.addFirst(state);
			return Response.ok().build();
		} catch (Throwable t) {
			log.error("problem accepting state " + state, t);
			return Response.serverError().entity(t.getMessage()).build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ServerState> showLast() {
		List<ServerState> res = new ArrayList<ServerState>();
		for (LinkedList<ServerState> x : states.values()) {
			res.add(x.getFirst());
		}
//		ServerState dummy = new ServerState();
//		dummy.setPointid(66);
//		dummy.setTimest(Instant.now());
//		dummy.setThisWeek(new boolean[]{true,true,false,false,true,false,true});
//		res.add(dummy);

		return res;
	}
	
	
}
