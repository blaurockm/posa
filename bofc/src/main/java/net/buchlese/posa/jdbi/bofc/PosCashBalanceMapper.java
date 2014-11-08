package net.buchlese.posa.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.bofc.PosCashBalance;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PosCashBalanceMapper implements ResultSetMapper<PosCashBalance> {

	public PosCashBalance map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosCashBalance cb = null;
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			cb = om.readValue(rs.getString("balanceSheet"), PosCashBalance.class);
		} catch (IOException e) {
			throw new SQLException(e);
		}
		// just to be shure
		cb.setId(rs.getLong("id"));
		cb.setRevenue(rs.getLong("revenue"));
		cb.setAbschlussId(rs.getString("abschlussid"));
		cb.setCreationtime(new DateTime(rs.getTimestamp("creationtime")));
		cb.setFirstCovered(new DateTime(rs.getTimestamp("firstCovered")));
		cb.setLastCovered(new DateTime(rs.getTimestamp("lastCovered")));
		cb.setExported(rs.getBoolean("exported"));
		cb.setOrigAbschluss(rs.getString("origSheet"));
		return cb;
	}

}
