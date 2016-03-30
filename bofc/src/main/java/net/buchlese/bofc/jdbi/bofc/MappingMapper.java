package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.bofc.Mapping;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class MappingMapper implements ResultSetMapper<Mapping> {

	public Mapping map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		Mapping tx = new Mapping();
		tx.setCustomerId(rs.getInt("customer"));
		tx.setPointid(rs.getInt("pointid"));
		tx.setDebitorId(rs.getInt("debitorid"));
		tx.setName1(rs.getString("name1"));
		tx.setCount(rs.getInt("anz"));
		return tx;
	}

}
