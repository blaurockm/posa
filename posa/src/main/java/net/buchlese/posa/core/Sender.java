package net.buchlese.posa.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.SendableObject;

public abstract class Sender<T extends SendableObject> implements Consumer<SendableObject> {
	
	private final URL homeUrl;
	private final ObjectMapper om;
	private final Logger log;
	private final CloseableHttpClient httpClient;
	
	
	private Sender<?> succ;

	public Sender(PosAdapterConfiguration config, Logger log, CloseableHttpClient client) {
		try {
			this.homeUrl = new URL(config.getSendHomeUrl());
		} catch (MalformedURLException e) {
			log.error("problem creating HomeURL " + config.getSendHomeUrl(), e);
			throw new IllegalArgumentException(e);
		}
		this.om = Jackson.newObjectMapper();
		this.log = log;
		this.httpClient = client;
	}
	

	public void send(T bal) {
		if (homeUrl == null || homeUrl.equals("homeless") ) {
			// do nothing;
			return;
		}

		// Get target URL
		HttpPost post;
		try {
			post = new HttpPost(new URL(this.homeUrl, getHomeResource()).toURI());
		} catch (MalformedURLException e2) {
			log.error("problem creating HomeResource " + getHomeResource(), e2);
			throw new IllegalArgumentException(e2);
		} catch (URISyntaxException e2) {
			log.error("problem creating HomeResource " + getHomeResource(), e2);
			throw new IllegalArgumentException(e2);
		}

		preSendHook(bal);
		try {
			StringEntity cashBalEntity = new StringEntity(om.writeValueAsString(bal), ContentType.create(MediaType.APPLICATION_JSON, "UTF8"));
			post.setEntity(cashBalEntity);
		} catch (JsonProcessingException e1) {
			log.error("problem while creating json-entity " + bal, e1);
		}

		try(CloseableHttpResponse response = httpClient.execute(post)) {
			StatusLine statusLine = response.getStatusLine();
			log.info("Sending " + bal + " with " + post + " :: " + statusLine);
			if (statusLine.getStatusCode() == 200) {
				postSuccessfulSendHook(bal);
			} else {
				postUnSuccessfulSendHook(bal);
			}
		} catch (SocketException e ) {
			// wir konnten keine Verbindung aufbauen, bzw. es ging was schief beim senden (endpunkt läuft net o.ä.)
			log.warn("problem while connecting home, evtly mark for retry " + bal);
			postUnSuccessfulSendHook(bal);
			// hier sollte wir auch jemanden informieren, am besten per mail..
		} catch (IOException e) {
			log.error("problem while sending " + bal, e);
		}

	}

    public Sender<?> addSender(Sender<?> x) {
    	x.succ = this;
    	return x;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void accept(SendableObject s) {
		if (canHandle(s)) {
			send( (T) s);
		} else {
			if (succ != null)
				succ.accept(s);
		}
	}
	
	public abstract boolean canHandle(SendableObject x);
	
	protected abstract String getHomeResource();
	
	protected abstract void postSuccessfulSendHook(T bal);

	protected abstract void postUnSuccessfulSendHook(T bal);

	protected abstract void preSendHook(T bal);


}
