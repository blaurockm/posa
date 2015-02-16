package net.buchlese.posa.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;

import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;

public class SendTimer extends TimerTask {

	private final PosAdapterConfiguration config;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(SendTimer.class);
	
	
	public SendTimer(PosAdapterConfiguration config) {
		super();
		this.config = config;
	}


	@Override
	public void run() {
		if (PosAdapterApplication.homingQueue.isEmpty() == false) {
			List<PosCashBalance> toDo = new ArrayList<>(PosAdapterApplication.homingQueue);
			PosAdapterApplication.homingQueue.clear();
			
			try (SendPosCashBalance sender = new SendPosCashBalance(config, log)) {
				if (config.isSshEnabled()) {
					sender.sshConnect();
				}
				toDo.forEach(sender);
			} catch (JSchException e1) {
				// problem connecting to ssh-server
				log.error("problem connecting ssh-session", e1);
			} catch (IOException e) {
				// problem closing httpClient
				log.error("problem closing session", e);
			}
			
		}
		
	}

}
