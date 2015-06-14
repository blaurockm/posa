package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.Shop;
import net.buchlese.bofc.api.shift.ShopEvent;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ShopEventMapper implements ResultSetMapper<ShopEvent>{

	@Override
	public ShopEvent map(int arg0, ResultSet rs, StatementContext arg2) throws SQLException {
		ShopEvent tx = new ShopEvent();
		tx.setId(rs.getLong("id"));
		tx.setDate(rs.getDate("date_").toLocalDate());
		tx.setDoneBy(Employee.getEmployee(rs.getInt("doneBy")));
		tx.setDoneWhere(Shop.getShop(rs.getInt("doneWhere")));
		tx.setFrom(rs.getTimestamp("from") != null ? rs.getTimestamp("from").toInstant():rs.getTimestamp("date_").toInstant());
		tx.setTill(rs.getTimestamp("till") != null ? rs.getTimestamp("till").toInstant():rs.getTimestamp("date_").toInstant());
		tx.setWholeDay(rs.getBoolean("wholeDay"));
		tx.setWorkFree(rs.getBoolean("workFree"));
		return tx;
	}

}
