package net.buchlese.bofc.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.buchlese.bofc.api.bofc.ArticleGroup;
import net.buchlese.bofc.api.bofc.Command;
import net.buchlese.bofc.api.cmd.AbstractBofcCommand;
import net.buchlese.bofc.api.cmd.PayOffCoupon;

import com.google.common.base.Optional;
import com.google.inject.Inject;

@Path("/")
public class CommandResource {

	private static Map<String, AbstractBofcCommand> commands = new ConcurrentHashMap<>();
	
	private static List<AbstractBofcCommand> toBeSend = new CopyOnWriteArrayList<>();
	
	@Inject
	public CommandResource() {
		super();
	}

	@GET
	@Path("/cmdstodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AbstractBofcCommand> fetchCmdToDo()  {
		return new ArrayList<AbstractBofcCommand>(toBeSend);
	}

	@GET
	@Path("/createpayoffcoupon")
	@Produces(MediaType.APPLICATION_JSON)
	public PayOffCoupon createPayoffcoupon(@QueryParam("pos") Integer pos, @QueryParam("key") String gutsch)  {
		PayOffCoupon c2 = new PayOffCoupon();
		c2.setPointId(pos);
		c2.setParams(new Object[] {gutsch});
		toBeSend.add(c2);
		return c2;
	}

	@GET
	@Path("/cmdsdone")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AbstractBofcCommand> fetchCmdDone()  {
		return new ArrayList<AbstractBofcCommand>(commands.values());
	}

	@GET
	@Path("/articlegroup")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<ArticleGroup> fetchAll(@QueryParam("key") Optional<String> key)  {
		if (key.isPresent()) {
			return Arrays.asList(ArticleGroup.getArticleGroups().get(key));
		}
		return ArticleGroup.getArticleGroups().values();
	}


	@GET
	@Path("/getcmds")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AbstractBofcCommand> sendCommands(@QueryParam("pos") Integer pos) {
		final int pointId = pos.intValue();
		List<AbstractBofcCommand> doIt = toBeSend.stream().filter(x -> x.getPointId() == pointId).collect(Collectors.toList());
		toBeSend.removeAll(doIt);
		doIt.forEach(x -> commands.put(String.valueOf(x.getId()), x));
		return doIt;
	}

	@POST
	@Path("/answercmds")
	@Consumes(MediaType.APPLICATION_JSON)
	public void recvResponses(Command respIn ) {
		String id = (String) respIn.getId();
		if (commands.containsKey(id)) {
			commands.get(id).setResult(respIn.getResult());
		}
	}
	
	
	
}
