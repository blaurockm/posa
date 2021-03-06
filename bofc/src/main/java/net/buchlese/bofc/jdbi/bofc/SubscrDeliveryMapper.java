package net.buchlese.bofc.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.subscr.SubscrDelivery;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubscrDeliveryMapper implements ResultSetMapper<SubscrDelivery> {

	public SubscrDelivery map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		SubscrDelivery cb = null;
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			cb = om.readValue(rs.getString("complJson"), SubscrDelivery.class);
		} catch (IOException e) {
			throw new SQLException(e);
		}
		// just to be shure
		cb.setId(rs.getLong("id"));
		cb.setPayed(rs.getBoolean("payed"));
		cb.setSlipped(rs.getBoolean("slipped"));
		cb.setInvoiceNumber(rs.getString("invoiceNumber"));
		cb.setSlipNumber(rs.getString("slipNumber"));
		return cb;
	}

}
