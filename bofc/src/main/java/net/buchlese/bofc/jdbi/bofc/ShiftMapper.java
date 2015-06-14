package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.Shift;
import net.buchlese.bofc.api.shift.Shop;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ShiftMapper implements ResultSetMapper<Shift>{

	@Override
	public Shift map(int arg0, ResultSet rs, StatementContext arg2) throws SQLException {
		Shift tx = new Shift();
		tx.setId(rs.getLong("id"));
		tx.setDate(rs.getDate("date_").toLocalDate());
		tx.setEmployee(Employee.getEmployee(rs.getInt("employee")));
		tx.setStore(Shop.getShop(rs.getInt("store")));
		tx.setWorkFrom(rs.getTimestamp("workFrom").toInstant());
		tx.setWorkTill(rs.getTimestamp("workTill").toInstant());
		tx.setWorkHours(rs.getDouble("workHours"));
		tx.setSettled(rs.getBoolean("settled"));
		return tx;
	}

}
