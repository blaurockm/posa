package net.buchlese.posa.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.bofc.PosSyncState;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PosSyncStateMapper implements ResultSetMapper<PosSyncState> {
//    <column name="key" type="varchar(20)"/>
//    <column name="value" type="DATETIME"/>
//    <column name="bigvalue" type="bigint"/>

	public PosSyncState map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosSyncState cb = new PosSyncState();
		// just to be shure
		cb.setKey(rs.getString("key"));
		cb.setTimest(new DateTime(rs.getTimestamp("value")));
		cb.setMark(rs.getBigDecimal("bigvalue"));
		return cb;
	}

}
