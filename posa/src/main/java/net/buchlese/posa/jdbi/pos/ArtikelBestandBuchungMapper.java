package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.ArtikelBestandBuchung;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ArtikelBestandBuchungMapper implements ResultSetMapper<ArtikelBestandBuchung> {

	public ArtikelBestandBuchung map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		ArtikelBestandBuchung vorg = new ArtikelBestandBuchung();
		vorg.setId(rs.getInt("ID"));
		vorg.setArtikelident(rs.getInt("artikelident"));
		vorg.setMenge(rs.getInt("Menge"));
		vorg.setBestandAlt(rs.getInt("BestandAlt"));
		vorg.setBestandNeu(rs.getInt("BestandNeu"));
		vorg.setBemerkung(rs.getString("Bemerkung"));
		vorg.setDatum(new DateTime(rs.getTimestamp("Datum")));
		return vorg;
	}

}
