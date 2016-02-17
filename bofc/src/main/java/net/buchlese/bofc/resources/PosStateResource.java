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

import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.slf4j.LoggerFactory;

@Path("/posState")
public class PosStateResource {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(PosStateResource.class);

	private final static Map<Integer, LinkedList<PosState>> states = new ConcurrentHashMap<>();
	private final static int MAXSIZE = 100;
	
	@POST
	@Path("/acceptState")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptState(PosState state)  {
		try {
			LinkedList<PosState> list = states.get(state.getPointid());
			if (list == null) {
				list = new LinkedList<PosState>();
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
	public List<PosState> showLast() {
		List<PosState> res = new ArrayList<PosState>();
		for (LinkedList<PosState> x : states.values()) {
			res.add(x.getFirst());
		}
		PosState dummy = new PosState();
		dummy.setPointid(66);
		dummy.setRevenue(23442L);
		dummy.setProfit(4323L);
		dummy.setStateDate(LocalDate.now());
		dummy.setTimest(Instant.now());
		res.add(dummy);
		return res;
	}
	
	
}
