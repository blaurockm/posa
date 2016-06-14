package net.buchlese.bofc.resources;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.buchlese.bofc.api.bofc.ServerState;

import org.apache.commons.io.FileUtils;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@Path("/serverState")
public class ServerStateResource {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ServerStateResource.class);

	private final static Map<Integer, LinkedList<ServerState>> states = new ConcurrentHashMap<>();
	private final static int MAXSIZE = 100;
	private final DBI bofcDb;
	
	@Inject
	public ServerStateResource(@Named("bofcdb")DBI posDBI) {
		this.bofcDb = posDBI;
	}
	
	
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
		return res;
	}

	@GET
	@Path("/createBackup")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createBackup()  {
		byte[] docStream;
		try {
			File tmpFile = File.createTempFile("bofcDbBackup", "zip");
			Handle h = bofcDb.open();
			h.execute("SCRIPT DROP TO '" + tmpFile.getAbsolutePath() + "' COMPRESSION ZIP");
			h.close();
			docStream = FileUtils.readFileToByteArray(tmpFile);
		} catch (IOException e) {
			throw new WebApplicationException(e);
		} 

		String filename = "bofcDbBackup_" + LocalDate.now().toString() + ".zip";
		return Response
		            .ok(docStream, MediaType.APPLICATION_OCTET_STREAM)
		            .header("content-disposition","attachment; filename = " + filename)
		            .build();
	}
	
}
