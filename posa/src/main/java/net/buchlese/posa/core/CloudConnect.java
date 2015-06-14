package net.buchlese.posa.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import net.buchlese.posa.PosAdapterConfiguration;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class CloudConnect implements Closeable {
	private Session sshSession;
	private static final String sshUser = "posclient";
	private final String homeHost;
	private final int localPort;

	public CloudConnect(PosAdapterConfiguration config, Logger log) throws JSchException {
		int lp = 8080;
		try {
			URL url = new URL(config.getHomeUrl());
			lp = url.getPort();
		} catch (MalformedURLException e) {
			log.error("problem parsing homeUrl", e);
		}
		this.homeHost = config.getHomeHost();
		this.localPort = lp;
		if (config.isSshEnabled()) {
			sshConnect();
		}
	}
	
	/**
	 * called on instantiation if ssh is desired
	 * @throws JSchException
	 */
	private void sshConnect() throws JSchException {
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
	public void close() throws IOException {
		if (sshSession != null && sshSession.isConnected()) {
			sshSession.disconnect();
		}
	}

}
