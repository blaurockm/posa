package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SendPosCashBalance implements Consumer<PosCashBalance>{

	private final int pointId;
	private final String homeUrl;
	private final ObjectMapper om;

	public SendPosCashBalance(PosAdapterConfiguration config) {
		this.pointId = config.getPointOfSaleId();
		this.homeUrl = config.getHomeUrl();
		this.om = Jackson.newObjectMapper();
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

		bal.setPointid(pointId);
		// Get target URL

		try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpPost post = new HttpPost(this.homeUrl);
			StringEntity cashBalEntity = new StringEntity(om.writeValueAsString(bal),
					ContentType.create(MediaType.APPLICATION_JSON));

			post.setEntity(cashBalEntity);

			try(CloseableHttpResponse response = httpclient.execute(post)) {
				System.out.println("----------");
				System.out.println(":::" + response.getStatusLine());
			}
		} catch (ConnectException e) {
			// wir konnten keine Verbindung aufbauen.
			// mark PosCashBalance for retry next hour
		} catch (IOException e) {
			e.printStackTrace();
		}

	} 


}
