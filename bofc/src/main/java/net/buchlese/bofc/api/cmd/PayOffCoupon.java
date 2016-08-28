package net.buchlese.bofc.api.cmd;

public class PayOffCoupon extends AbstractBofcCommand {

	private static long ids = 1000;
	
	public PayOffCoupon() {
		super("payoffcoupon");
		setId(ids++);
	}

}
