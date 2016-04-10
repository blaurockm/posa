package net.buchlese.bofc.resources;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import net.buchlese.bofc.api.bofc.ArticleGroup;
import net.buchlese.bofc.api.cmd.AbstractBofcCommand;
import net.buchlese.bofc.api.cmd.PayOffInvoice;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

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
		toBeSend.add(new PayOffInvoice());
		return new ArrayList<AbstractBofcCommand>(toBeSend);
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
	public Response sendCommands(@QueryParam("pos") Integer pos) {
		final int pointId = pos.intValue();
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException,  WebApplicationException {
				AbstractBofcCommand doIt = null;
				for (AbstractBofcCommand cmd : toBeSend) {
					if (cmd.getPointId() == pointId) {
						doIt = cmd;
						break;
					}
				}
				// Create a new JSON-RPC 2.0 request
				if (doIt != null) {
					Writer writer = new BufferedWriter(new OutputStreamWriter(os, "iso-8859-1"));
					JSONRPC2Request reqOut = new JSONRPC2Request(doIt.getAction(), doIt.getParams() != null ? Arrays.asList(doIt.getParams()) : null, doIt.getId());
	
					// Serialise the request to a JSON-encoded string
					writer.write(reqOut.toString());
					writer.flush();
					toBeSend.remove(doIt);
					commands.put(doIt.getId(), doIt);
				}
			}
		};
		return Response.ok(stream).build();
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
