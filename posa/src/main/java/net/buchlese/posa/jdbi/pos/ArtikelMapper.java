package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.Artikel;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ArtikelMapper implements ResultSetMapper<Artikel> {

	public Artikel map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		Artikel vorg = new Artikel();
		vorg.setArtikelident(rs.getInt("artikelident"));
		vorg.setIsbn(rs.getString("isbn"));
		vorg.setArtikelnummer(rs.getString("artikelNummer"));
		vorg.setMatchcode(rs.getString("matchCode"));
		vorg.setBezeichnung(rs.getString("Bezeichnung"));
		vorg.setBestand(rs.getInt("bestand"));
		vorg.setvK(rs.getBigDecimal("vk"));
		vorg.seteK(rs.getBigDecimal("ek"));
		vorg.setmWSt(rs.getString("mwst") != null ? rs.getString("mwst").charAt(0) : null);
		vorg.setWarGrIndex(rs.getString("WarGrIndex") != null ? rs.getString("WarGrIndex").charAt(0) : null);
		return vorg;
	}

}