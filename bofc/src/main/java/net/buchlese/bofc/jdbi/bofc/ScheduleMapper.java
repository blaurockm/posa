package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.shift.Shift;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ScheduleMapper implements ResultSetMapper<Shift>{

	@Override
	public Shift map(int arg0, ResultSet rs, StatementContext arg2) throws SQLException {
		Shift tx = new Shift();
		tx.setId(rs.getLong("id"));
		return tx;
	}

}
