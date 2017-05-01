package net.buchlese.posa.core;

import java.util.List;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.PosArticleStockChange;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;

public class SendStockChange extends Sender<PosArticleStockChange> {

	private final String homeResource ;
	
	private final int pointid;
	private final PosArticleDAO articleDao;
	
	public SendStockChange(PosAdapterConfiguration config, PosArticleDAO balDAO, Logger log, CloseableHttpClient c) {
		super(config, log, c);
		this.pointid = config.getPointOfSaleId();
		this.articleDao = balDAO;
		this.homeResource = config.getStockChangeResource();
	}

	public void sendCashBalances(List<PosArticleStockChange> bals) {
		for (PosArticleStockChange bal: bals) {
			accept(bal);
		}
	}
	

	protected void postSuccessfulSendHook(PosArticleStockChange bal) {
		articleDao.getClass();
	}

	protected void postUnSuccessfulSendHook(PosArticleStockChange bal) {
		PosAdapterApplication.homingQueue.offer(bal);
	}

	protected void preSendHook(PosArticleStockChange bal) {
		bal.setPointid(pointid);
	}

	@Override
	protected String getHomeResource() {
		return homeResource;
	}

	@Override
	public boolean canHandle(SendableObject x) {
		return x instanceof PosArticleStockChange;
	}

}
