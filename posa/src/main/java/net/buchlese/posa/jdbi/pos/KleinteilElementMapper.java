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
		vorg.setLaufendeNummer(rs.getInt("LaufendeNummer"));
		vorg.setPositionsNummer(rs.getInt("PositionsNummer"));
//		[ISBN] [varchar](20) NULL,
		vorg.setIsbn(rs.getString("isbn"));
//		[ArtikelNummer] [nvarchar](30) NULL,
		vorg.setArtikelNummer(rs.getString("artikelNummer"));
//		[MatchCode] [varchar](70) NULL,
		vorg.setMatchCode(rs.getString("matchCode"));
		vorg.setBezeichnung(rs.getString("bezeichnung"));
		vorg.setKennziffer(rs.getString("kennziffer"));
		vorg.setTextbaustein(rs.getInt("vonPos"));
//		[Menge] [float] NULL,
		vorg.setMenge(rs.getBigDecimal("stueck"));
//		[VK] [float] NULL,
		vorg.setBruttoEinzel(rs.getBigDecimal("vk1"));
		vorg.setRabattEinzel(rs.getBigDecimal("vk2"));
		vorg.setMwstkz(rs.getString("mwst") != null ? rs.getString("mwst").charAt(0) : '0');
		vorg.setRabattSatz(rs.getBigDecimal("rabatt"));
		return vorg;
	}

}
