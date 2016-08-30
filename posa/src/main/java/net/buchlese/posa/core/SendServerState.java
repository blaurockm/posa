package net.buchlese.posa.core;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.api.bofc.ServerState;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

public class SendServerState extends Sender<ServerState> {
	private final String homeResource;
	private final int pointid;

	public SendServerState(PosAdapterConfiguration config, Logger log, CloseableHttpClient httpClient) {
		super(config, log, httpClient);
		this.pointid = config.getPointOfSaleId();
		this.homeResource = config.getServerStateResource();
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof ServerState;
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	protected void postSuccessfulSendHook(ServerState bal) {

	}

	@Override
	protected void preSendHook(ServerState bal) {
		bal.setPointid(pointid);
	}

}
