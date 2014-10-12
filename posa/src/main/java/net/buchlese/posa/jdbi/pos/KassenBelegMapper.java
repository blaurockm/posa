package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.KassenBeleg;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KassenBelegMapper implements ResultSetMapper<KassenBeleg> {

	@Override
	public KassenBeleg map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
		KassenBeleg beleg = new KassenBeleg();
//		[KassenNr] [int] NOT NULL,
		beleg.setKassennr(rs.getInt("kassenNr"));
//		[BelegNr] [int] NOT NULL,
		beleg.setBelegnr(rs.getInt("belegNr"));
//		[KassiererNummer] [int] NULL,  -----------------
//		[Bemerkung] [nvarchar](30) NULL,
		beleg.setBemerkung(rs.getString("bemerkung"));
//		[RechnungNr] [varchar](15) NULL,
		beleg.setRechnungNr(rs.getString("rechnungNr"));
//		[AdrGruppe] [tinyint] NULL,   -----------------
//		[AdrNummer] [int] NULL,     -----------------
//		[Kundensuchname] [nvarchar](40) NULL,    -----------------
//		[ZahlungsZeit] [datetime] NULL,
//		[Datum] [datetime] NULL,
		beleg.setZahlungszeit(new DateTime(rs.getTimestamp("Datum")));
//		[Geparkt] [bit] NOT NULL,
//		[Storniert] [bit] NOT NULL,
		beleg.setStorniert(rs.getBoolean("storniert"));
//		[Storno] [bit] NOT NULL,    -----------------
//		[Nachlass_Proz] [float] NULL,
		beleg.setNachlassProz(rs.getDouble("nachlass_proz"));
//		[Nachlass_DM] [float] NULL,     -----------------
//		[StornoBemerkung] [nvarchar](50) NULL,
		beleg.setStornoBemerkung(rs.getString("StornoBemerkung"));
//		[Initialien] [nvarchar](50) NULL,     -----------------
//		[LetzterAbgleich] [datetime] NULL,     -----------------
//		[Gebucht] [bit] NOT NULL,     -----------------
//		[BuchungsDatum] [datetime] NULL,     -----------------
//		[ZahlungsBetrag] [float] NULL,
		beleg.setZahlungsBetrag(rs.getBigDecimal("zahlungsbetrag"));
//		[ÜbergabeBetrag] [float] NULL,     -----------------
//		[RückgabeBetrag] [float] NULL,     -----------------
//		[Skonto] [float] NULL,     -----------------
//		[ZahlungsArt] [smallint] NULL,
		beleg.setZahlungsart(rs.getInt("zahlungsart"));
//		[GiroKontoNr] [varchar](10) NULL,     -----------------
//		[BLZ] [varchar](8) NULL,     -----------------
//		[BelegArt] [tinyint] NULL,    
		beleg.setBelegart(rs.getInt("belegart"));
//		[Konto_Inhaber] [varchar](30) NULL,     -----------------
//		[Gesellschaft] [varchar](15) NULL,     -----------------
//		[Zeitmarke] [timestamp] NOT NULL,     -----------------
//		[IBAN] [varchar](34) NULL,     -----------------
//		[BIC] [varchar](11) NULL,     -----------------
		return beleg;
	}

}
