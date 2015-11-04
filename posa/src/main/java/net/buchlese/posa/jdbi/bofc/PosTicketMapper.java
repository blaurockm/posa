package net.buchlese.posa.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.bofc.PaymentMethod;
import net.buchlese.posa.api.bofc.PosTicket;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PosTicketMapper implements ResultSetMapper<PosTicket> {

	public PosTicket map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosTicket tx = new PosTicket();
		tx.setId(rs.getLong("id"));
		tx.setTotal(rs.getLong("total"));
		tx.setBelegNr(rs.getLong("belegNr"));
		tx.setCancelled(rs.getBoolean("cancelled"));
		tx.setCancel(rs.getBoolean("cancel"));
		tx.setPaymentMethod(PaymentMethod.fromDbKey(rs.getString("paymentMethod")));
		tx.setTimestamp(new DateTime(rs.getTimestamp("timest")));
		return tx;
	}

}
