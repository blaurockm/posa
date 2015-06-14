package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SendPosCashBalance implements Consumer<PosCashBalance>, java.io.Closeable {

	private final static String homeResource = "cashbalance/acceptBalance";
	
	private final int pointid;
	private final String homeUrl;
	private final ObjectMapper om;
	private final Logger log;
	
	private CloseableHttpClient httpClient;
	private final PosCashBalanceDAO cashBalanceDAO;

	public SendPosCashBalance(PosAdapterConfiguration config, PosCashBalanceDAO balDAO, Logger log) {
		this.pointid = config.getPointOfSaleId();
		this.cashBalanceDAO = balDAO;
		this.homeUrl = config.getHomeUrl();
		this.om = Jackson.newObjectMapper();
		this.log = log;
		this.httpClient = HttpClients.createDefault();
	}

	public void sendCashBalances(List<PosCashBalance> bals) {
		for (PosCashBalance bal: bals) {
			accept(bal);
		}
	}
	
	@Override
	public void accept(PosCashBalance bal) {
		if (homeUrl == null || homeUrl.isEmpty() || homeUrl.equals("homeless") ) {
			// do nothing;
			return;
		}

		bal.setPointid(pointid);
		if (bal.getTickets() != null) {
			bal.getTickets().forEach( t -> { t.setPointid(pointid); if (t.getTxs() != null)  t.getTxs().forEach( x -> x.setPointid(pointid)); } );
		}
		// Get target URL

		HttpPost post = new HttpPost(this.homeUrl + homeResource);
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
				bal.setExported(true);
				bal.setExportDate(new DateTime());
				cashBalanceDAO.markAsExported(bal);
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


	@Override
	public void close() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
	} 


}
