package net.buchlese.posa.core.cmd;

import net.buchlese.posa.PosAdapterConfiguration;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class PayOffCouponCommand extends AbstractCommand {

	public PayOffCouponCommand(PosAdapterConfiguration config) {
		super(config);
	}

	@Override
	public boolean canHandle(JSONRPC2Request req) {
		return req.getMethod().equals("payoffcoupon");
	}

	@Override
	public Object execute(JSONRPC2Request req) {
		System.out.println(" executing PayOffCoupon-cmd...." + req.getPositionalParams());
		return "erfolreich";
	}

}
