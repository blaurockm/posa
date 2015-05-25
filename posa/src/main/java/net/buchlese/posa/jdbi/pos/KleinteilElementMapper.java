package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.KleinteilElement;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KleinteilElementMapper implements ResultSetMapper<KleinteilElement> {

	public KleinteilElement map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		KleinteilElement vorg = new KleinteilElement();
		vorg.setRechnungsNummer(rs.getInt("KopfNummer"));
//		[LfdNummer] [int] NOT NULL,
		vorg.setPositionsNummer(rs.getInt("PositionsNummer"));
//		[ISBN] [varchar](20) NULL,
		vorg.setIsbn(rs.getString("isbn"));
//		[ArtikelNummer] [nvarchar](30) NULL,
		vorg.setArtikelNummer(rs.getString("artikelNummer"));
//		[MatchCode] [varchar](70) NULL,
		vorg.setMatchCode(rs.getString("matchCode"));
//		[Menge] [float] NULL,
		vorg.setMenge(rs.getBigDecimal("menge"));
//		[VK] [float] NULL,
		vorg.setvK(rs.getBigDecimal("vk"));
		vorg.setmWSt(rs.getString("mwst") != null ? rs.getString("mwst").charAt(0) : null);
		vorg.setRabatt(rs.getBigDecimal("rabatt"));
		return vorg;
	}

}
