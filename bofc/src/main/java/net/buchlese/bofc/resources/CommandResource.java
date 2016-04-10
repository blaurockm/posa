package net.buchlese.bofc.resources;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import net.buchlese.bofc.api.bofc.ArticleGroup;
import net.buchlese.bofc.api.cmd.AbstractBofcCommand;

public class CommandResource {

	private Map<String, AbstractBofcCommand> commands;
	
	public CommandResource() {
		super();
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
	public void sendCommands(@QueryParam("pos") Integer pos) {
		
	}

	@POST
	@Path("/answercmds")
	@Produces(MediaType.APPLICATION_JSON)
	public void recvResponses(@FormParam("jsonAnswer") String jsonString ) {
		JSONRPC2Response respIn = null;
		try {
			respIn = JSONRPC2Response.parse(jsonString);
		} catch (JSONRPC2ParseException e) {
			throw new WebApplicationException(e);
		}
		String id = (String) respIn.getID();
		if (commands.containsKey(id)) {
			commands.get(id).setResult(String.valueOf(respIn.getResult()));
		}
	}
	
	
	
}
