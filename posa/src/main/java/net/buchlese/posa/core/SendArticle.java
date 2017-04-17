package net.buchlese.posa.core;

import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosArticle;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

public class SendArticle extends Sender<PosArticle> {

	private final String homeResource ;
	
	private final int pointid;
	private final PosArticleDAO invoiceDAO;
	
	public SendArticle(PosAdapterConfiguration config, PosArticleDAO balDAO, Logger log, CloseableHttpClient c) {
		super(config, log, c);
		this.pointid = config.getPointOfSaleId();
		this.invoiceDAO = balDAO;
		this.homeResource = config.getArticleResource();
	}

	public void sendCashBalances(List<PosArticle> bals) {
		for (PosArticle bal: bals) {
			accept(bal);
		}
	}
	

	protected void postSuccessfulSendHook(PosArticle bal) {
		invoiceDAO.getClass();
	}

	protected void postUnSuccessfulSendHook(PosArticle bal) {
		PosAdapterApplication.homingQueue.offer(bal);
	}

	protected void preSendHook(PosArticle bal) {
		bal.setPointid(pointid);
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof PosArticle;
	}

}
