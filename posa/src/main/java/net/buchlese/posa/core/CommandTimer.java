package net.buchlese.posa.core;

import java.io.IOException;
import java.net.ConnectException;
import java.util.TimerTask;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.core.cmd.PayOffCouponCommand;
import net.buchlese.posa.core.cmd.PayOffInvoiceCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Inject;
import com.jcraft.jsch.JSchException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class CommandTimer extends TimerTask {

	private final static String homeResource = "getcmds";

	private final Logger logger;
	private final PosAdapterConfiguration config;
	private final String homeUrl;

	public static long lastRun;

	@Inject
	public CommandTimer(PosAdapterConfiguration config) {
		this.config = config;
		this.homeUrl = config.getHomeUrl();
		logger = LoggerFactory.getLogger(CommandTimer.class);
	}

	@Override
	public void run() {
		if (homeUrl == null || homeUrl.isEmpty() || homeUrl.equals("homeless") ) {
			// do nothing;
			return;
		}

		lastRun = System.currentTimeMillis();
		synchronized(PosAdapterApplication.homingQueue) {

			try (CloudConnect cloud = new CloudConnect(config, logger)) {

				ClientConfig clientConfig = new DefaultClientConfig();
				clientConfig.getClasses().add(JacksonJsonProvider.class);
				Client client = Client.create(clientConfig);

				WebResource r = client.resource(homeUrl + homeResource);
				r = r.queryParam("pos", String.valueOf(config.getPointOfSaleId()));
				
				String jsonString = r.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
				JSONRPC2Request reqIn = null;

				try {
					reqIn = JSONRPC2Request.parse(jsonString);

				} catch (JSONRPC2ParseException e) {
					logger.error("problem parsing command", e);
				}

				// das command ausführen
				if (reqIn.getMethod().equals("payoffinvoice")) {
					new PayOffInvoiceCommand();
				}
				if (reqIn.getMethod().equals("payoffcoupon")) {
					new PayOffCouponCommand();
				}
				
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

}