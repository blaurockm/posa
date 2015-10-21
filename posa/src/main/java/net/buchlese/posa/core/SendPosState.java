package net.buchlese.posa.core;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosState;
import net.buchlese.posa.api.bofc.SendableObject;

public class SendPosState extends Sender<PosState> {
	private final int pointid;

	public SendPosState(PosAdapterConfiguration config, Logger log, CloseableHttpClient httpClient) {
		super(config, log, httpClient);
		this.pointid = config.getPointOfSaleId();
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return false;
	}

	@Override
	protected String getHomeResource() {
		return "posState/acceptState";
	}

	@Override
	protected void postSuccessfulSendHook(PosState bal) {

	}

	@Override
	protected void preSendHook(PosState bal) {
		bal.setPointid(pointid);
	}

}
