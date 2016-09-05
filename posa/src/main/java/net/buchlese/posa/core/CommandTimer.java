package net.buchlese.posa.core;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.TimerTask;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.Command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Inject;
import com.jcraft.jsch.JSchException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class CommandTimer extends TimerTask {

	private final String homeResource;

	private final Logger logger;
	private final PosAdapterConfiguration config;
	private final URL homeUrl;

	public static long lastRun;

	@Inject
	public CommandTimer(PosAdapterConfiguration config) throws MalformedURLException {
		this.config = config;
		this.homeUrl = new URL(config.getCommandHomeUrl());
		logger = LoggerFactory.getLogger(CommandTimer.class);
		this.homeResource = config.getCommandGetResource();
	}

	@Override
	public void run() {
		if (homeUrl == null || homeUrl.equals("homeless") ) {
			// do nothing;
			return;
		}

		lastRun = System.currentTimeMillis();
		synchronized(PosAdapterApplication.homingQueue) {

			try (CloudConnect cloud = new CloudConnect(homeUrl, config, logger)) {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);

				WebResource r = client.resource(new URL(homeUrl, homeResource).toURI());
				r = r.queryParam("pointid", String.valueOf(config.getPointOfSaleId()));
				
				List<Command> commands = r.accept(MediaType.APPLICATION_JSON_TYPE)
						.get(new GenericType<List<Command>>() {});

				commands.forEach(x -> PosAdapterApplication.commandQueue.add(x));
				
			} catch (ConnectException | URISyntaxException e) {
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

}