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
	public String execute(Command req) {
		if (req.param1 == null) {
			return "falsche Anzahl an Parametern. Erwartet: rechnungsnummer";
		}
		String rechnr = req.getParam1();
	    Boolean bezahlt = posDb.createQuery("select Bezahlt from [dbo].kleinteilKopf where rechnungNummer = '" + rechnr + "'").first(Boolean.class);

	    if (bezahlt == null) {
	    	return "Rechnung " + rechnr + " gibt es nicht!";
	    }
	    	
	    if (bezahlt == Boolean.TRUE) {
	    	return "ACHTUNG! Rechnung " + rechnr + " ist schon als bezahlt merkiert!";
	    }

	    BigDecimal betrag = posDb.createQuery("select RechnungsBetrag from [dbo].kleinteilKopf where rechnungNummer = '" + rechnr + "'").first(BigDecimal.class);

		int c = posDb.update("update [dbo].kleinteilKopf set Bezahlt = true, Wie_Bezahlt = 'per Überweisung' where rechnungNummer = ? and bezahlt = false", rechnr);
		if (c >= 1) {
			return "erfolreich markiert, betrag = " + betrag + " anzahl=" + c;
		}
		return "Rechnung nicht aktualisiert";
	}

}
