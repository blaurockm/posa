package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.bofc.PosInvoice;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PosInvoiceMapper implements ResultSetMapper<PosInvoice> {

	public PosInvoice map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosInvoice tx = new PosInvoice();
		tx.setId(rs.getLong("id"));
		tx.setNumber(rs.getString("number"));
		tx.setCustomerId(rs.getInt("customer"));
		tx.setDebitorId(rs.getInt("debitor"));
		tx.setAmount(rs.getLong("amount"));
		tx.setAmountFull(rs.getLong("amountFull"));
		tx.setAmountHalf(rs.getLong("amountHalf"));
		tx.setAmountNone(rs.getLong("amountNone"));
		tx.setDate(new LocalDate(rs.getTimestamp("invDate")));
		tx.setCreationTime(new DateTime(rs.getTimestamp("creationtime")));
		tx.setPrintTime(new DateTime(rs.getTimestamp("printdate")));
		tx.setName1(rs.getString("name1"));
		tx.setName2(rs.getString("name2"));
		tx.setName3(rs.getString("name3"));
		tx.setStreet(rs.getString("street"));
		tx.setCity(rs.getString("city"));
		return tx;
	}

}
