package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

import org.joda.time.LocalDate;

public class InvoicesView extends AbstractBofcView {

	private final SubscrDAO dao;
	private final PosInvoiceDAO invDao;
	private final List<PosInvoice> invoices;
	
	public InvoicesView(SubscrDAO dao, PosInvoiceDAO invDAO) {
		super("invoices.ftl");
		this.dao = dao;
		this.invDao = invDAO;
		this.invoices = invDao.fetchAllAfter(LocalDate.now().minusWeeks(8).toDate());
	}



	public List<PosInvoice> getInvoices() {
		return invoices;
	}

	public List<PosInvoice> getTempInvoices() {
		return dao.getTempInvoices();
	}

}
