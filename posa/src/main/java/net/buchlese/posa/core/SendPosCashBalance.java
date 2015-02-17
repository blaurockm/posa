package net.buchlese.posa.core;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SendPosCashBalance implements Consumer<PosCashBalance>, java.io.Closeable {

	private final int pointid;
	private final String homeUrl;
	private final String homeHost;
	private final int localPort;
	private static final String sshUser = "posclient";
	private final ObjectMapper om;
	private final Logger log;
	
	private Session sshSession;
	private CloseableHttpClient httpClient;

	public SendPosCashBalance(PosAdapterConfiguration config, Logger log) {
		this.pointid = config.getPointOfSaleId();
		this.homeUrl = config.getHomeUrl();
		int lp = 8080;
		try {
			URL url = new URL(config.getHomeUrl());
			lp = url.getPort();
		} catch (MalformedURLException e) {
			log.error("problem parsing homeUrl", e);
		}
		this.homeHost = config.getHomeHost();
		this.localPort = lp;
		this.om = Jackson.newObjectMapper();
		this.log = log;
		this.httpClient = HttpClients.createDefault();
	}


	public void sendCashBalances(List<PosCashBalance> bals) {
		for (PosCashBalance bal: bals) {
			accept(bal);
		}
	}

	public void sshConnect() throws JSchException {
		 JSch jsch=new JSch();
		 // den bofcclient.pub muss man in die .ssh/authorized_keys datei des users posclient packen
	     jsch.addIdentity("/etc/posa/bofcclient","KennstDuDasLandWoJederLacht");
//	     jsch.setKnownHosts("/etc/posa/known_hosts");
	     JSch.setConfig("StrictHostKeyChecking", "no"); // die ip vom server kann sich ändern. wir können keine statisches known_hosts dafür pflegen.
	     // alternative wäre VerifyHostKeyDNS
	     // dazu brauchen wir einen secure fingerprint im dynDNS-Service, wer macht sowas? selfhost nicht
		 sshSession=jsch.getSession(sshUser, homeHost, 22);
		 
		 sshSession.connect();
		 sshSession.setPortForwardingL(localPort, "localhost", 8080);
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

		HttpPost post = new HttpPost(this.homeUrl);
		try {
			StringEntity cashBalEntity = new StringEntity(om.writeValueAsString(bal), ContentType.create(MediaType.APPLICATION_JSON));
			post.setEntity(cashBalEntity);
		} catch (JsonProcessingException e1) {
			log.error("problem while creating json-entity " + bal, e1);
		}

		try(CloseableHttpResponse response = httpClient.execute(post)) {
			log.info("Sending " + bal + " :: " + response.getStatusLine());
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
		if (sshSession != null && sshSession.isConnected()) {
			sshSession.disconnect();
		}
		if (httpClient != null) {
			httpClient.close();
		}
	} 


}
