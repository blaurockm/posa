package net.buchlese.posa.core.cmd;

import java.io.IOException;
import java.net.ConnectException;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.core.CloudConnect;
import net.buchlese.posa.core.CommandTimer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.jcraft.jsch.JSchException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

public abstract class AbstractCommand {
	private final static String homeResource = "answercmds";

	private AbstractCommand successor;
	private final Logger logger;
	private final PosAdapterConfiguration config;
	private final String homeUrl;
	
	public AbstractCommand(PosAdapterConfiguration config) {
		super();
		this.config = config;
		this.homeUrl = config.getHomeUrl();
		logger = LoggerFactory.getLogger(CommandTimer.class);
	}

	public void handle(JSONRPC2Request req) {
		if (canHandle(req)) {
			try {
				sendBack(execute(req), req.getID());
			} catch (Throwable t) {
				sendBack("FATAL ERROR " + t.getMessage(), req.getID());
			}
		} else {
			if (successor != null) {
				successor.handle(req);
			} else {
				sendBack("unknownRequestMethod", req.getID());
			}
		}
	}
	
	protected void sendBack(Object result, Object id) {
		synchronized(PosAdapterApplication.homingQueue) {

			try (CloudConnect cloud = new CloudConnect(config, logger)) {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);

				WebResource r = client.resource(homeUrl + homeResource);				
				JSONRPC2Response respIn = new JSONRPC2Response(result, id );

				Form f = new Form();
				f.add("jsonAnswer", respIn.toString());

				r.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE).post();
				
			} catch (ConnectException e) {
				// wir konnten keine Verbindung aufbauen.
				// mark PosCashBalance for retry next hour
				logger.warn("problem while connecting home", e);
			} catch (JSchException e1) {
				// problem connecting to ssh-server
				logger.error("problem connecting ssh-session", e1);
			} catch (IOException e) {
				// problem closing httpClient
				logger.error("problem closing session", e);
			}
		}
	
	}
	
	public abstract boolean canHandle(JSONRPC2Request req);
	public abstract Object execute(JSONRPC2Request req);
	
	public AbstractCommand concat(AbstractCommand succ) {
		this.successor = succ;
		return succ;
	}
}
