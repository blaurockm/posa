package net.buchlese.bofc.jdbi.bofc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import net.buchlese.bofc.api.bofc.PaymentMethod;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class PayMethArgumentFactory implements ArgumentFactory<PaymentMethod> {

	@Override
	public boolean accepts(Class<?> expectedType, Object value,
			StatementContext ctx) {
		return value instanceof PaymentMethod;
	}

	@Override
	public Argument build(Class<?> expectedType, PaymentMethod value, StatementContext ctx) {
		return new PaxMethArgument(value);
	}

	public static class PaxMethArgument implements Argument {
		private final PaymentMethod value;
		
		public PaxMethArgument(PaymentMethod value) {
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
