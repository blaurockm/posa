package net.buchlese.posa.core.cmd;

import net.buchlese.posa.PosAdapterConfiguration;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class PayOffInvoiceCommand extends AbstractCommand {

	public PayOffInvoiceCommand(PosAdapterConfiguration config) {
		super(config);
	}

	@Override
	public boolean canHandle(JSONRPC2Request req) {
		return false;
	}

	@Override
	public Object execute(JSONRPC2Request req) {
		return null;
	}

}
