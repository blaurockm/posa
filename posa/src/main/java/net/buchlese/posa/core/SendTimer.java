package net.buchlese.posa.core;

import java.util.TimerTask;
import java.util.function.Consumer;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;

public class SendTimer extends TimerTask {

	private final PosAdapterConfiguration config;
	
	
	public SendTimer(PosAdapterConfiguration config) {
		super();
		this.config = config;
	}


	@Override
	public void run() {
		if (PosAdapterApplication.homingQueue.isEmpty() == false) {
			Consumer<? super PosCashBalance> sender = new SendPosCashBalance(config);
			PosAdapterApplication.homingQueue.forEach(sender);
		}
		
	}

}
