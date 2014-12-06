package net.buchlese.posa.view;

import net.buchlese.posa.api.bofc.PosCashBalance;
import io.dropwizard.views.View;

public class CashBalView extends View {

	private final PosCashBalance cashBal;
	
	public CashBalView(PosCashBalance cashBal) {
		super("cashbal.ftl");
		this.cashBal = cashBal;
	}

	public PosCashBalance getBalance() {
		return cashBal;
	}

	
}
