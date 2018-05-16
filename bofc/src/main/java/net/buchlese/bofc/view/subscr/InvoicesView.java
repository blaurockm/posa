package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.view.AbstractBofcView;

public class InvoicesView extends AbstractBofcView {

	private final List<PosInvoice> invoices;
	
	public InvoicesView(List<PosInvoice> invoices) {
		super("invoices.ftl");
		this.invoices = invoices;
	}

	public List<PosInvoice> getInvoices() {
		return invoices;
	}

}
