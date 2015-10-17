package net.buchlese.posa.core;

import java.util.List;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;

import org.joda.time.DateTime;
import org.slf4j.Logger;

public class SendPosCashBalance extends Sender<PosCashBalance> {

	private final static String homeResource = "cashbalance/acceptBalance";
	
	private final int pointid;
	private final PosCashBalanceDAO cashBalanceDAO;

	public SendPosCashBalance(PosAdapterConfiguration config, PosCashBalanceDAO balDAO, Logger log) {
		super(config, log);
		this.pointid = config.getPointOfSaleId();
		this.cashBalanceDAO = balDAO;
	}

	public void sendCashBalances(List<PosCashBalance> bals) {
		for (PosCashBalance bal: bals) {
			accept(bal);
		}
	}
	

	protected void postSuccessfulSendHook(PosCashBalance bal) {
		bal.setExported(true);
		bal.setExportDate(new DateTime());
		cashBalanceDAO.markAsExported(bal);
	}

	protected void preSendHook(PosCashBalance bal) {
		bal.setPointid(pointid);
		if (bal.getTickets() != null) {
			bal.getTickets().forEach( t -> { t.setPointid(pointid); if (t.getTxs() != null)  t.getTxs().forEach( x -> x.setPointid(pointid)); } );
		}
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof PosCashBalance;
	}

}
