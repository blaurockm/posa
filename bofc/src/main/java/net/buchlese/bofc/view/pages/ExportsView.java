package net.buchlese.bofc.view.pages;

import java.util.Collection;

import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.core.AccountingExportFactory;
import net.buchlese.bofc.view.AbstractBofcView;

public class ExportsView extends AbstractBofcView {


	public ExportsView() {
		super("exports.ftl");
	}
	
	public Collection<AccountingExport> getExports() {
		return AccountingExportFactory.getExports();
	}
	
}
