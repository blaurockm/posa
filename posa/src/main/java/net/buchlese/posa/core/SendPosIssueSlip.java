package net.buchlese.posa.core;

import java.util.List;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosIssueSlip;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

public class SendPosIssueSlip extends Sender<PosIssueSlip> {

	private final String homeResource;
	
	private final int pointid;
	private final PosInvoiceDAO invoiceDAO;
	
	public SendPosIssueSlip(PosAdapterConfiguration config, PosInvoiceDAO balDAO, Logger log, CloseableHttpClient c) {
		super(config, log, c);
		this.pointid = config.getPointOfSaleId();
		this.invoiceDAO = balDAO;
		this.homeResource = config.getIssueSlipResource();
	}

	public void sendCashBalances(List<PosIssueSlip> bals) {
		for (PosIssueSlip bal: bals) {
			accept(bal);
		}
	}
	

	protected void postSuccessfulSendHook(PosIssueSlip bal) {
		invoiceDAO.getClass();
	}

	protected void preSendHook(PosIssueSlip bal) {
		bal.setPointid(pointid);
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof PosIssueSlip;
	}

}
