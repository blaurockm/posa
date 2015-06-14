package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.shift.Employee;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class EmployeeMapper implements ResultSetMapper<Employee>{

	@Override
	public Employee map(int arg0, ResultSet rs, StatementContext arg2) throws SQLException {
		Employee tx = new Employee();
		tx.setId(rs.getLong("id"));
		tx.setName(rs.getString("name"));
		tx.setPersonellNumber(rs.getInt("personellNumer"));
		return tx;
	}

}
