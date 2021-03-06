package net.buchlese.bofc.core;

import org.h2.tools.Server;

import io.dropwizard.lifecycle.Managed;

public class H2TcpServerManager implements Managed {
	private Server server;

	/**
	 * Supported options are:
	 * -tcpPort, -tcpSSL, -tcpPassword, -tcpAllowOthers, -tcpDaemon,
	 * -trace, -ifExists, -baseDir, -key.
	 * 
	 */
	@Override
	public void start() throws Exception {
		server = Server.createTcpServer(new String[] {"-tcpAllowOthers"}).start();
	}

	@Override
	public void stop() throws Exception {
		server.stop();
	}

}
