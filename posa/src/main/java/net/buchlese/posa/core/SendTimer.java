package net.buchlese.posa.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.jcraft.jsch.JSchException;

public class SendTimer extends TimerTask {

	private final PosAdapterConfiguration config;
	private final PosCashBalanceDAO cashBalDao;
	private final PosInvoiceDAO invoiceDao;
	private final PosArticleDAO articleDao;
	private final URL baseUrl;
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(SendTimer.class);

	@Inject
	public SendTimer(PosAdapterConfiguration config, PosCashBalanceDAO balDao, PosArticleDAO artDao, PosInvoiceDAO invDAO) throws MalformedURLException {
		super();
		this.baseUrl = new URL(config.getSendHomeUrl());
		this.config = config;
		this.cashBalDao = balDao;
		this.invoiceDao = invDAO;
		this.articleDao = artDao;
	}

	public static long lastHomingRun;

	@Override
	public void run() {
		synchronized(PosAdapterApplication.homingQueue) {
			if (PosAdapterApplication.homingQueue.isEmpty() == false) {
				lastHomingRun = System.currentTimeMillis();
				List<SendableObject> toDo = new ArrayList<>(PosAdapterApplication.homingQueue);
				PosAdapterApplication.homingQueue.clear();

				try (CloseableHttpClient httpClient = HttpClients.createDefault();
						CloudConnect cloud = new CloudConnect(baseUrl, config, log)) {

					Sender<?> sender = new SendPosCashBalance(config, cashBalDao, log, httpClient);
					sender = sender.addSender(new SendPosState(config, log, httpClient));
					sender = sender.addSender(new SendServerState(config, log, httpClient));
					sender = sender.addSender(new SendPosInvoice(config, invoiceDao, log, httpClient));
					sender = sender.addSender(new SendPosIssueSlip(config, invoiceDao, log, httpClient));
					sender = sender.addSender(new SendCommandAnswer(config, log, httpClient));
					sender = sender.addSender(new SendArticle(config, articleDao,  log, httpClient));

					toDo.forEach(sender);
				} catch (JSchException e1) {
					// problem connecting to ssh-server
					log.error("problem connecting ssh-session", e1);
				} catch (IOException e) {
					// problem closing httpClient
					log.error("problem closing cloud", e);
				} catch (Throwable t) {
					log.error("komisches problem", t);
					EmailNotification.sendAdminMail("komisches Problem SendTime", t);
				}

			}
		}
	}

}
