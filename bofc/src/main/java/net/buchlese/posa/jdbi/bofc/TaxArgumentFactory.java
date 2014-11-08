package net.buchlese.posa.jdbi.bofc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import net.buchlese.posa.api.bofc.Tax;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class TaxArgumentFactory implements ArgumentFactory<Tax> {

	@Override
	public boolean accepts(Class<?> expectedType, Object value,
			StatementContext ctx) {
		return value instanceof Tax;
	}

	@Override
	public Argument build(Class<?> expectedType, Tax value, StatementContext ctx) {
		return new TaxArgument(value);
	}

	public static class TaxArgument implements Argument {
		private final Tax value;
		
		public TaxArgument(Tax value) {
			this.value = value;
		}

		@Override
		public void apply(int position, PreparedStatement statement,
				StatementContext ctx) throws SQLException {
			if (value != null) {
				statement.setString(position, value.getDbKey());
			} else {
				statement.setNull(position, Types.CHAR);
			}
		}
		
	}
}
