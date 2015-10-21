package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.net.ConnectException;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.SendableObject;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Sender<T extends SendableObject> implements Consumer<SendableObject> {
	
	private final String homeUrl;
	private final ObjectMapper om;
	private final Logger log;
	private final CloseableHttpClient httpClient;
	
	
	private Sender<?> succ;

	public Sender(PosAdapterConfiguration config, Logger log, CloseableHttpClient client) {
		this.homeUrl = config.getHomeUrl();
		this.om = Jackson.newObjectMapper();
		this.log = log;
		this.httpClient = client;
	}
	

	public void send(T bal) {
		if (homeUrl == null || homeUrl.isEmpty() || homeUrl.equals("homeless") ) {
			// do nothing;
			return;
		}

		preSendHook(bal);
		// Get target URL

		HttpPost post = new HttpPost(this.homeUrl + getHomeResource());
		try {
			StringEntity cashBalEntity = new StringEntity(om.writeValueAsString(bal), ContentType.create(MediaType.APPLICATION_JSON, "UTF8"));
			post.setEntity(cashBalEntity);
		} catch (JsonProcessingException e1) {
			log.error("problem while creating json-entity " + bal, e1);
		}

		try(CloseableHttpResponse response = httpClient.execute(post)) {
			StatusLine statusLine = response.getStatusLine();
			log.info("Sending " + bal + " :: " + statusLine);
			if (statusLine.getStatusCode() == 200) {
				postSuccessfulSendHook(bal);
			}
		} catch (ConnectException e) {
			// wir konnten keine Verbindung aufbauen.
			// mark PosCashBalance for retry next hour
			log.warn("problem while connecting home, mark for retry " + bal);
			PosAdapterApplication.homingQueue.offer(bal);
		} catch (IOException e) {
			log.error("problem while sending " + bal, e);
		}

	}

    public Sender<?> addSender(Sender<?> x) {
    	this.succ = x;
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
	
	protected abstract void preSendHook(T bal);


}
