package net.buchlese.posa.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.bofc.PosInvoice;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PosInvoiceMapper implements ResultSetMapper<PosInvoice> {

	public PosInvoice map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosInvoice inv = new PosInvoice();
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			String x = rs.getString("complJson");
			if (x != null && x.isEmpty() == false) {
				inv = om.readValue(rs.getString("complJson"), PosInvoice.class);
			}
		} catch (IOException e) {
			throw new SQLException(e);
		}
		inv.setId(rs.getLong("id"));
		inv.setNumber(rs.getString("number"));
		inv.setCustomerId(rs.getInt("customer"));
		inv.setDebitorId(rs.getInt("debitor"));
		inv.setAmount(rs.getLong("amount"));
		inv.setAmountFull(rs.getLong("amountFull"));
		inv.setAmountHalf(rs.getLong("amountHalf"));
		inv.setAmountNone(rs.getLong("amountNone"));
		inv.setDate(new LocalDate(rs.getTimestamp("invDate")));
		inv.setCreationTime(new DateTime(rs.getTimestamp("creationtime")));
		inv.setPrintTime(new DateTime(rs.getTimestamp("printdate")));
		inv.setName1(rs.getString("name1"));
		inv.setName2(rs.getString("name2"));
		inv.setName3(rs.getString("name3"));
		inv.setStreet(rs.getString("street"));
		inv.setCity(rs.getString("city"));
		inv.setActionum(rs.getInt("actionum"));
		inv.setPayed(rs.getBoolean("payed"));
		inv.setCancelled(rs.getBoolean("cancelled"));
		return inv;
	}

}
