package net.buchlese.bofc.view;

import net.buchlese.bofc.api.bofc.AccountingExport;

public class AccountingExportView extends AbstractBofcView {
	private final AccountingExport export;
	
	public AccountingExportView(AccountingExport ae) {
		super("accexport.ftl");
		this.export = ae;
	}

	public AccountingExport getExport() {
		return export;
	}
	
	public String getDescription() {
		return export.getDescription();
	}

	public String getAccount() {
		return String.valueOf(export.getRefAccount());
	}
	
	

}
