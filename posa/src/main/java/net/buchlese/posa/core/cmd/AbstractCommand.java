package net.buchlese.posa.core.cmd;

import java.net.MalformedURLException;
import java.util.function.Consumer;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.Command;

public abstract class AbstractCommand implements Consumer<Command> {

	private AbstractCommand successor;
	
	public AbstractCommand(PosAdapterConfiguration config) throws MalformedURLException {
		super();
	}
    
	public void accept(Command req) {
		if (req == null) {
			return;
		}
		if (canHandle(req)) {
			try {
				PosAdapterApplication.homingQueue.add(doExecute(req));
			} catch (Throwable t) {
				req.setResult("FATAL ERROR " + t.getMessage());
				PosAdapterApplication.homingQueue.add(req);
			}
		} else {
			if (successor != null) {
				successor.accept(req);
			} else {
				req.setResult("unknown RequestMethod, end of chain");
				PosAdapterApplication.homingQueue.add(req);
			}
		}
	}
	
	protected Command doExecute(final Command req) {
		req.setResult(execute(req));
		return req;
	}
	
	
	public abstract boolean canHandle(Command req);
	public abstract Object execute(Command req);
	
	public AbstractCommand concat(AbstractCommand succ) {
		succ.successor = this;
		return succ;
	}
}
