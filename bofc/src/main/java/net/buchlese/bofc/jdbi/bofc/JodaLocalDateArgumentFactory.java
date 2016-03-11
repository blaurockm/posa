package net.buchlese.bofc.jdbi.bofc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.joda.time.LocalDate;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class JodaLocalDateArgumentFactory implements ArgumentFactory<LocalDate> {

	@Override
	public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
		return value instanceof LocalDate;
	}

	@Override
	public Argument build(Class<?> expectedType, LocalDate value, StatementContext ctx) {
		return new JodaLocalDateArgument(value);
	}
	
	
	public static class JodaLocalDateArgument implements Argument {

		private final LocalDate localDate;

		JodaLocalDateArgument(LocalDate ld) {
			this.localDate = ld;
		}
		
		@Override
		public void apply(int position, PreparedStatement statement,
				StatementContext ctx) throws SQLException {
			if (localDate != null) {
				statement.setDate(position, new java.sql.Date(localDate.toDateTimeAtStartOfDay().getMillis()));
			} else {
				statement.setNull(position, Types.DATE);
			}
		}
	}

}
