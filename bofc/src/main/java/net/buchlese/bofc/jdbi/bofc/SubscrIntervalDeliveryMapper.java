package net.buchlese.bofc.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubscrIntervalDeliveryMapper implements ResultSetMapper<SubscrIntervalDelivery> {

	public SubscrIntervalDelivery map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		SubscrIntervalDelivery cb = null;
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			cb = om.readValue(rs.getString("complJson"), SubscrIntervalDelivery.class);
		} catch (IOException e) {
			throw new SQLException(e);
		}
		// just to be shure
		cb.setId(rs.getLong("id"));
		cb.setPayed(rs.getBoolean("payed"));
		return cb;
	}

}
