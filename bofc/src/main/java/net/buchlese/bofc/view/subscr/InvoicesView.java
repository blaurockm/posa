package net.buchlese.bofc.view.subscr;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class InvoicesView extends AbstractBofcView {

	private final List<PosInvoice> invoices;
	
	public InvoicesView(SubscrDAO dao) {
		super("invoices.ftl");
		this.invoices = dao.getSubscrInvoices(3, LocalDate.now().minusWeeks(8)).stream().filter(i -> "subscr".equals(i.getType())).collect(Collectors.toList());
	}

	public List<PosInvoice> getInvoices() {
		return invoices;
	}

}
