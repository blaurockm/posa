package net.buchlese.posa.core;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.ArticleGroup;

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

public class ConfigSyncTimer extends TimerTask {

	private final static String homeResource = "articlegroup";

	private final Logger logger;
	private final PosAdapterConfiguration config;
	private final URL homeUrl;

	public static long lastRun;

	@Inject
	public ConfigSyncTimer(PosAdapterConfiguration config) throws MalformedURLException {
		this.config = config;
		this.homeUrl = new URL(config.getSyncHomeUrl());
		logger = LoggerFactory.getLogger(ConfigSyncTimer.class);
	}

	@Override
	public void run() {
		if (homeUrl == null || homeUrl.equals("homeless") ) {
			// do nothing;
			return;
		}

		lastRun = System.currentTimeMillis();
		synchronized(PosAdapterApplication.homingQueue) {

			try (CloudConnect cloud = new CloudConnect(homeUrl,config, logger)) {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);

				WebResource r = client.resource(new URL(homeUrl,homeResource).toURI());

				List<ArticleGroup> articleGroups = r.accept(MediaType.APPLICATION_JSON_TYPE)
						.get(new GenericType<List<ArticleGroup>>() {});

				Map<String, ArticleGroup> newConf = new HashMap<String, ArticleGroup>();

				articleGroups.stream().forEach(x -> newConf.put(x.getKey(), x));

				ArticleGroup.injectMappings(newConf);

			} catch (ConnectException | URISyntaxException e) {
				// wir konnten keine Verbindung aufbauen.
				logger.warn("problem while connecting home", e);
			} catch (JSchException e1) {
				// problem connecting to ssh-server
				logger.error("problem connecting ssh-session", e1);
			} catch (IOException e) {
				// problem closing httpClient
				logger.error("problem closing session", e);
			} catch (Throwable e) {
				// irgendwas anderes
				logger.error("komisches problem ", e);
				EmailNotification.sendAdminMail("komisches Problem CommandTimer", e);
			}
		}
	}

}