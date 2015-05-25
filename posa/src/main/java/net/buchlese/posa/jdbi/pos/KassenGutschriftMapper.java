package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.KassenGutschrift;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KassenGutschriftMapper implements ResultSetMapper<KassenGutschrift> {

	public KassenGutschrift map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		KassenGutschrift vorg = new KassenGutschrift();
//		[KassenNr] [int] NOT NULL,  -------------------
//		vorg.setKassenNr(rs.getInt("kassenNr"));
		vorg.setGutschriftNr(rs.getInt("gutschriftNr"));
		vorg.setBetrag(rs.getBigDecimal("betrag"));
		vorg.setAnlageZeit(new DateTime(rs.getTimestamp("anlageZeit")));
		vorg.setErledigt(rs.getBoolean("erledigt"));
		vorg.setErledigtAm(new DateTime(rs.getTimestamp("eingelöstAm")));
		return vorg;
	}

}
