package net.buchlese.posa.core;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

public class SendPosInvoice extends Sender<PosInvoice> {

	private final String homeResource ;
	
	private final int pointid;
	private final PosInvoiceDAO invoiceDAO;
	
	public SendPosInvoice(PosAdapterConfiguration config, PosInvoiceDAO balDAO, Logger log, CloseableHttpClient c) {
		super(config, log, c);
		this.pointid = config.getPointOfSaleId();
		this.invoiceDAO = balDAO;
		this.homeResource = config.getInvoiceResource();
	}

	public void sendCashBalances(List<PosInvoice> bals) {
		for (PosInvoice bal: bals) {
			accept(bal);
		}
	}
	

	protected void postSuccessfulSendHook(PosInvoice bal) {
		invoiceDAO.getClass();
	}

	protected void postUnSuccessfulSendHook(PosInvoice bal) {
		PosAdapterApplication.homingQueue.offer(bal);
	}

	protected void preSendHook(PosInvoice bal) {
		bal.setPointid(pointid);
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof PosInvoice;
	}

}
