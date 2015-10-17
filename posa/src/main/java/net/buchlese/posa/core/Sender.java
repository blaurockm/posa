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
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Sender<T extends SendableObject> implements Consumer<T>, java.io.Closeable {
	
	private final String homeUrl;
	private final ObjectMapper om;
	private final Logger log;
	
	private CloseableHttpClient httpClient;

	public Sender(PosAdapterConfiguration config, Logger log) {
		this.homeUrl = config.getHomeUrl();
		this.om = Jackson.newObjectMapper();
		this.log = log;
		this.httpClient = HttpClients.createDefault();
	}
	
	@Override
	public void accept(T bal) {
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

	protected abstract String getHomeResource();
	
	protected abstract void postSuccessfulSendHook(T bal);
	
	protected abstract void preSendHook(T bal);


	@Override
	public void close() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
	} 


}
