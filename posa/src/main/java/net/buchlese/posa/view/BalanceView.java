package net.buchlese.posa.view;

import net.buchlese.posa.api.bofc.PosCashBalance;
import io.dropwizard.views.View;

public class BalanceView extends View {

	private final PosCashBalance bal;
	
	public BalanceView(PosCashBalance bal) {
		super("balance.ftl");
		this.bal = bal;
	}

	public PosCashBalance getBalance() {
		return bal;
	}
	
}
