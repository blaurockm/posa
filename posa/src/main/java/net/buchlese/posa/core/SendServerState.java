package net.buchlese.posa.core;

import org.slf4j.Logger;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosState;
import net.buchlese.posa.api.bofc.SendableObject;

public class SendServerState extends Sender<PosState> {
	private final int pointid;

	public SendServerState(PosAdapterConfiguration config, Logger log) {
		super(config, log);
		this.pointid = config.getPointOfSaleId();
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return false;
	}

	@Override
	protected String getHomeResource() {
		return "serverState/acceptState";
	}

	@Override
	protected void postSuccessfulSendHook(PosState bal) {

	}

	@Override
	protected void preSendHook(PosState bal) {
		bal.setPointid(pointid);
	}

}
