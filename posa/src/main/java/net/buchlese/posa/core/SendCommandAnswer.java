package net.buchlese.posa.core;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.Command;
import net.buchlese.posa.api.bofc.SendableObject;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

public class SendCommandAnswer extends Sender<Command> {
	private final String homeResource;

	public SendCommandAnswer(PosAdapterConfiguration config, Logger log, CloseableHttpClient httpClient) {
		super(config, log, httpClient);
		homeResource = config.getCommandSendResource();
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof Command;
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	protected void postSuccessfulSendHook(Command bal) {

	}

	@Override
	protected void preSendHook(Command bal) {
	}

}
