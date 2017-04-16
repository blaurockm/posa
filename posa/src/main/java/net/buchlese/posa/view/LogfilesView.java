package net.buchlese.posa.view;

import io.dropwizard.setup.Environment;
import io.dropwizard.views.View;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;

public class LogfilesView extends View {

	private final PosAdapterConfiguration cfg;
	@SuppressWarnings("unused")
	private final PosCashBalanceDAO dao;
	@SuppressWarnings("unused")
	private final PosInvoiceDAO daoInv;
	@SuppressWarnings("unused")
	private final Environment app;


	public LogfilesView(PosAdapterConfiguration config, PosCashBalanceDAO dao, PosInvoiceDAO daoInv, Environment app) {
		super("logfiles.ftl");
		this.app = app;
		this.cfg = config;
		this.dao = dao;
		this.daoInv = daoInv;
}

	public String getPosName() {
		return cfg.getName();
	}

	public String getPointId() {
		return String.valueOf(cfg.getPointOfSaleId());
	}

}
