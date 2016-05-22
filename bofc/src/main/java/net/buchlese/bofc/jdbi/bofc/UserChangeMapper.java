package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.bofc.UserChange;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UserChangeMapper implements ResultSetMapper<UserChange> {

	public UserChange map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		UserChange tx = new UserChange();
		tx.setId(rs.getLong("id"));
		tx.setObjectId(rs.getLong("objectid"));
		tx.setLogin(rs.getString("login"));
		tx.setFieldId(rs.getString("fieldId"));
		tx.setOldValue(rs.getString("oldValue"));
		tx.setNewValue(rs.getString("newValue"));
		tx.setAction(rs.getString("action"));
		tx.setModDate(new DateTime(rs.getTimestamp("modDate")));
		return tx;
	}

}
