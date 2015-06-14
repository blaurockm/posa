package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.shift.Shop;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ShopMapper implements ResultSetMapper<Shop>{

	@Override
	public Shop map(int arg0, ResultSet rs, StatementContext arg2) throws SQLException {
		Shop tx = new Shop();
		tx.setId(rs.getLong("id"));
		tx.setAbbrev(rs.getString("abbrev"));
		tx.setName(rs.getString("name"));
		return tx;
	}

}
