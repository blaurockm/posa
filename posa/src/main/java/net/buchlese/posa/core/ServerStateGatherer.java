package net.buchlese.posa.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.joda.time.Instant;

import com.google.inject.Singleton;

import net.buchlese.posa.api.bofc.ServerState;

@Singleton
public class ServerStateGatherer extends AbstractGatherer {

	private ServerState lastServerState = new ServerState();
	
	@Override
	public void gatherData() {
		try {
			lastServerState = new ServerState();
			lastServerState.setIpAddress(InetAddress.getLocalHost().getHostAddress());
			lastServerState.setLastDbConnection(new Instant(SyncTimer.lastRunWithDbConnection));
			lastServerState.setTimest(Instant.now());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public ServerState getState() {
		return lastServerState;
	}

}
