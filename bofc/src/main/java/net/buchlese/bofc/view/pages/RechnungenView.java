package net.buchlese.bofc.view.pages;

import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.BackOfcConfiguration;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class RechnungenView extends AbstractBofcView {

	private final BackOfcConfiguration cfg;
	private final PosInvoiceDAO daoInv;

	public RechnungenView(BackOfcConfiguration config, PosInvoiceDAO daoInv) {
		super("rechnungen.ftl");
		this.cfg = config;
		this.daoInv = daoInv;
	}

	public String getPosName() {
		cfg.getMetricsFactory();
		return "Backend";
	}
	
	public List<PosInvoice> getInvoices() {
		List<PosInvoice> res = daoInv.fetchAllAfter(LocalDate.now().minusDays(10).toDateTimeAtStartOfDay().toDate());
		Collections.reverse(res);
		return res;
	}
	
}
