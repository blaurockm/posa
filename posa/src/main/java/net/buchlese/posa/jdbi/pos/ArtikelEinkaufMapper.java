package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.ArtikelEinkauf;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ArtikelEinkaufMapper implements ResultSetMapper<ArtikelEinkauf> {

	public ArtikelEinkauf map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		ArtikelEinkauf vorg = new ArtikelEinkauf();
		vorg.setArtikelident(rs.getInt("artikelident"));
		vorg.seteK(rs.getBigDecimal("ek"));
		vorg.setLetzterEintrag(new DateTime(rs.getBigDecimal("LetzterEintrag")));
		return vorg;
	}

}
