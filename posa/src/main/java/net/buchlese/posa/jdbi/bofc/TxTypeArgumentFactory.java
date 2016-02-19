package net.buchlese.posa.jdbi.bofc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import net.buchlese.posa.api.bofc.TxType;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class TxTypeArgumentFactory implements ArgumentFactory<TxType> {

	@Override
	public boolean accepts(Class<?> expectedType, Object value,	StatementContext ctx) {
		return value instanceof TxType;
	}

	@Override
	public Argument build(Class<?> expectedType, TxType value, StatementContext ctx) {
		return new TxTypeArgument(value);
	}

	public static class TxTypeArgument implements Argument {
		private final TxType value;
		
		public TxTypeArgument(TxType value) {
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
