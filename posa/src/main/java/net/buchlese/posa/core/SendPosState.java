package net.buchlese.posa.core;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosState;
import net.buchlese.posa.api.bofc.SendableObject;

public class SendPosState extends Sender<PosState> {
	private final String homeResource;
	private final int pointid;

	public SendPosState(PosAdapterConfiguration config, Logger log, CloseableHttpClient httpClient) {
		super(config, log, httpClient);
		this.pointid = config.getPointOfSaleId();
		this.homeResource = config.getPosStateResource();
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof PosState;
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	protected void postSuccessfulSendHook(PosState bal) {

	}

	@Override
	protected void preSendHook(PosState bal) {
		bal.setPointid(pointid);
	}

}
