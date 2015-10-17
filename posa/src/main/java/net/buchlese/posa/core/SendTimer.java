package net.buchlese.posa.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.jcraft.jsch.JSchException;

public class SendTimer extends TimerTask {

	private final PosAdapterConfiguration config;
	private final PosCashBalanceDAO cashBalDao;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(SendTimer.class);
	
	@Inject
	public SendTimer(PosAdapterConfiguration config, PosCashBalanceDAO balDao) {
		super();
		this.config = config;
		this.cashBalDao = balDao;
	}

    public static long lastHomingRun;
	
	@Override
	public void run() {
		if (PosAdapterApplication.homingQueue.isEmpty() == false) {
			lastHomingRun = System.currentTimeMillis();
			List<SendableObject> toDo = new ArrayList<>(PosAdapterApplication.homingQueue);
			PosAdapterApplication.homingQueue.clear();
			
			try (SendPosCashBalance sender = new SendPosCashBalance(config, cashBalDao, log);
					CloudConnect cloud = new CloudConnect(config, log)) {
				toDo.forEach(x -> { if (x instanceof PosCashBalance) { sender.accept((PosCashBalance) x); } });
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
