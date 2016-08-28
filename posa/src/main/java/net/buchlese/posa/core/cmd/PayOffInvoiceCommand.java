package net.buchlese.posa.core.cmd;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.Command;

import org.skife.jdbi.v2.Handle;

public class PayOffInvoiceCommand extends AbstractCommand {

	private final Handle posDb;
	
	public PayOffInvoiceCommand(PosAdapterConfiguration config, Handle posDB) throws MalformedURLException {
		super(config);
		this.posDb = posDB;
	}

	@Override
	public boolean canHandle(Command req) {
		return req.getAction().equals("payoffinvoice");
	}

	@Override
	public Object execute(Command req) {
		if (req.params == null || req.params.length != 1) {
			return "falsche Anzahl an Parametern. Erwartet: gutscheinnummer";
		}
		String gutsch = String.valueOf(req.getParams()[0]);
	    Boolean bezahlt = posDb.createQuery("select erledigt from [dbo].kasse_gutschriften where gutschriftnr = '" + gutsch + "'").first(Boolean.class);

	    if (bezahlt == null) {
	    	return "Gutschein " + gutsch + " gibt es nicht!";
	    }
	    	
	    if (bezahlt == Boolean.TRUE) {
	    	return "ACHTUNG! Gutschein " + gutsch + " ist schon als bezahlt merkiert!";
	    }

	    BigDecimal betrag = posDb.createQuery("select betrag from [dbo].kasse_gutschriften where gutschriftnr = '" + gutsch + "'").first(BigDecimal.class);

		int c = posDb.update("update [dbo].kasse_gutschriften set erledigt = true where gutschriftnr = ? and erledigt = false", gutsch);
		if (c == 1) {
			return "erfolreich markiert, betrag = " + betrag;
		}
		return "Gutschein nicht aktualisiert";
	}

}
