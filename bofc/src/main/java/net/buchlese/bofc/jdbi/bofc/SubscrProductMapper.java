package net.buchlese.bofc.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.subscr.SubscrProduct;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubscrProductMapper implements ResultSetMapper<SubscrProduct> {

	public SubscrProduct map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		SubscrProduct cb = null;
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			cb = om.readValue(rs.getString("complJson"), SubscrProduct.class);
		} catch (IOException e) {
			throw new SQLException(e);
		}
		// just to be shure
		cb.setId(rs.getLong("id"));
		return cb;
	}

}
