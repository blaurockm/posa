package net.buchlese.posa.core.cmd;

import java.math.BigDecimal;
import java.net.MalformedURLException;

import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.bofc.Command;

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.BigDecimalMapper;
import org.skife.jdbi.v2.util.BooleanMapper;

public class PayOffCouponCommand extends AbstractCommand {

	private final Handle posDb;
	
	public PayOffCouponCommand(PosAdapterConfiguration config, Handle posDB) throws MalformedURLException {
		super(config);
		this.posDb = posDB;
	}

	@Override
	public boolean canHandle(Command req) {
		return req.getAction().equals("payoffcoupon");
	}

	@Override
	public Object execute(Command req) {
		if (req.params == null || req.params.length != 1) {
			return "falsche Anzahl an Parametern. Erwartet: gutscheinnummer";
		}
		String gutsch = String.valueOf(req.getParams()[0]);
	    Boolean bezahlt = posDb.createQuery("select erledigt from [dbo].kasse_gutschriften where gutschriftnr = '" + gutsch + "'").map(new BooleanMapper()).first();

	    if (bezahlt == null) {
	    	return "Gutschein " + gutsch + " gibt es nicht!";
	    }
	    	
	    if (bezahlt == Boolean.TRUE) {
	    	return "ACHTUNG! Gutschein " + gutsch + " ist schon als bezahlt merkiert!";
	    }

	    BigDecimal betrag = posDb.createQuery("select betrag from [dbo].kasse_gutschriften where gutschriftnr = '" + gutsch + "'").map(new BigDecimalMapper()).first();

		int c = posDb.update("update [dbo].kasse_gutschriften set erledigt = '1' where gutschriftnr = ? and erledigt = '0'", gutsch);
		if (c == 1) {
			return "erfolreich markiert, betrag = " + betrag;
		}
		return "Gutschein nicht aktualisiert";
	}

}
