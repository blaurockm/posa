package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.KassenVorgang;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KassenVorgangMapper implements ResultSetMapper<KassenVorgang> {

	public KassenVorgang map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		KassenVorgang vorg = new KassenVorgang();
//		[KassenNr] [int] NOT NULL,  -------------------
		vorg.setKassenNr(rs.getInt("kassenNr"));
//		[BelegNr] [int] NOT NULL,
		vorg.setBelegNr(rs.getInt("belegNr"));
//		[LfdNummer] [int] NOT NULL,
		vorg.setLfdNummer(rs.getInt("lfdNummer"));
//		[ArtikelIdent] [int] NULL,
		vorg.setArtikelident(rs.getInt("artikelIdent"));
//		[ISBN] [varchar](20) NULL,
		vorg.setIsbn(rs.getString("isbn"));
//		[ArtikelNummer] [nvarchar](30) NULL,
		vorg.setArtikelNummer(rs.getString("artikelNummer"));
//		[MatchCode] [varchar](70) NULL,
		vorg.setMatchCode(rs.getString("matchCode"));
//		[Bezeichnung] [ntext] NULL,
		String bezeichnung = rs.getString("bezeichnung");
		if (bezeichnung != null && bezeichnung.isEmpty() == false) {
			if (bezeichnung.length() > 255) {
				vorg.setBezeichnung(bezeichnung.substring(0, 254));
			} else {
				vorg.setBezeichnung(bezeichnung);
			}
		}
//		[Beschreibung] [ntext] NULL,
		String beschreibung = rs.getString("Beschreibung");
		if (beschreibung != null && beschreibung.isEmpty() == false) {
			if (beschreibung.length() > 255) {
				vorg.setBeschreibung(beschreibung.substring(0, 254));
			} else {
				vorg.setBeschreibung(beschreibung);
			}
		}
//		[Menge] [float] NULL,
		vorg.setMenge(rs.getBigDecimal("menge"));
//		[VK] [float] NULL,
		vorg.setVK(rs.getBigDecimal("vk"));
//		[Gesamt] [float] NULL,
		vorg.setGesamt(rs.getBigDecimal("gesamt"));
//		[Datum] [datetime] NULL,
		vorg.setDatum(new DateTime(rs.getTimestamp("datum")));
//		[LagerNummer] [nvarchar](6) NULL, -------------------
//		[MWSt] [nvarchar](1) NULL,
		vorg.setMWSt(rs.getString("mwst") != null ? rs.getString("mwst").charAt(0) : null);
//		[VorgangsArt] [nvarchar](1) NULL,
		vorg.setVorgangsArt(rs.getString("vorgangsart") != null ? rs.getString("vorgangsart").charAt(0) : null);
//		[NichtImLager] [bit] NOT NULL,  ------------------------
//		[VorgangsArtikel] [bit] NOT NULL, -------------------------
//		[Rabatt] [float] NULL,
		vorg.setRabatt(rs.getDouble("rabatt"));
//		[ReservierungsNummer] [nvarchar](12) NULL,
//		[Lager] [int] NULL,  -------------------------
//		[Vk2] [float] NULL,  ------------------------
//		[BestandNachVK] [float] NULL,  ---------------------
//		[LetzterAbgleich] [datetime] NULL,  -------------------
//		[LagerBereitsGebucht] [bit] NOT NULL, -------------------
//		[VerleihTage] [smallint] NULL,  -----------------
//		[VerleihVorgang] [bit] NOT NULL,    -----------------
//		[VerleihvorgangsNummer] [int] NULL,    -----------------
//		[VerleihRückgabe] [bit] NOT NULL,    -----------------
//		[GutscheinVonKasse] [int] NULL,   -----------------
//		[LG_Buch_Übertragen] [bit] NOT NULL,   -----------------
//		[VonVerlag] [bit] NOT NULL,   -----------------
//		[FiBu_Konto] [varchar](5) NULL,   -----------------
//		[ReservierungsArt] [int] NULL,  -----------------
//		[Zeitmarke] [timestamp] NOT NULL, -----------------
//		[Gebucht] [bit] NOT NULL,  ----------------
//		[MwstSatz] [float] NULL,
		vorg.setMWStSatz(rs.getDouble("mwstsatz"));
//		[GutscheinVonFilialNr] [int] NULL,    ----------------
		return vorg;
	}

}
