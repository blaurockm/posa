package net.buchlese.bofc.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.LocalDate;
import org.skife.jdbi.v2.util.TypedMapper;

public class JodaLocalDateMapper extends TypedMapper<LocalDate> {

	@Override
	protected LocalDate extractByIndex(ResultSet r, int index) throws SQLException {
        final java.sql.Date timestamp = r.getDate(index);
        if (timestamp == null) {
            return null;
        }
        return new LocalDate(timestamp.getTime());
	}

	@Override
	protected LocalDate extractByName(ResultSet r, String name) throws SQLException {
        final java.sql.Date timestamp = r.getDate(name);
        if (timestamp == null) {
            return null;
        }
        return new LocalDate(timestamp.getTime());
	}

}
