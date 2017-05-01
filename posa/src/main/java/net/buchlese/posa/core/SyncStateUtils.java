package net.buchlese.posa.core;

import io.dropwizard.jdbi.args.JodaDateTimeMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.util.BigDecimalMapper;

public class SyncStateUtils {

	
	public static void recordSyncRun(Handle bofc) {
	    DateTime lastSync = DateTime.now().minusMinutes(30); // Änderungen der letzten 30 minuten
	    List<DateTime> lastRuns = bofc.createQuery("select value from dynamicstate where key='lastsyncrun'").map(new JodaDateTimeMapper()).list();
	    
	    if (lastRuns.isEmpty()) {
	    	bofc.execute("insert into dynamicstate (key, value) values('lastsyncrun', ?)", lastSync);
	    } else {
	    	lastSync = lastRuns.get(0);
	    	bofc.execute("update dynamicstate set value = ? where key = 'lastsyncrun'", DateTime.now());
	    }
	}
	

	public static void synchronize(Handle bofc, String key, Function<BigDecimal, BigDecimal> syncmethod) {
	    BigDecimal balRowVer = null;
	    List<BigDecimal> balRowVers = bofc.createQuery("select bigvalue from dynamicstate where key='" + key + "'").map(new BigDecimalMapper()).list();
	    
	    if (balRowVers.isEmpty()) {
	    	bofc.execute("insert into dynamicstate (key, bigvalue) values('" + key + "', 0)");
	    } else {
	    	balRowVer = balRowVers.get(0);
	    }

		balRowVer = syncmethod.apply(balRowVer);
    	bofc.execute("update dynamicstate set bigvalue = ?, value = current_timestamp where key = ?", balRowVer, key);
	}
}
