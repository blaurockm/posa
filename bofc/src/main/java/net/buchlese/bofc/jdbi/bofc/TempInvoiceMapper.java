package net.buchlese.bofc.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;



import net.buchlese.bofc.api.bofc.PosInvoice;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TempInvoiceMapper implements ResultSetMapper<PosInvoice> {

	public PosInvoice map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosInvoice cb = null;
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			cb = om.readValue(rs.getString("complJson"), PosInvoice.class);
		} catch (IOException e) {
			throw new SQLException(e);
		}
		return cb;
	}

}
