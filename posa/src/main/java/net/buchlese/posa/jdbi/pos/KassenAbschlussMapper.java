package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.KassenAbschluss;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KassenAbschlussMapper implements ResultSetMapper<KassenAbschluss> {

	@Override
	public KassenAbschluss map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
		KassenAbschluss ka = new KassenAbschluss();
//		CREATE TABLE [dbo].[Kasse_AbschlussDaten](
//		[ID] [int] IDENTITY(1,1) NOT NULL,
		ka.setId(rs.getInt("ID"));
//		[Kasse] [int] NULL,
		ka.setKasse(rs.getInt("Kasse"));
//		[AbschlussNr] [int] NULL,
		ka.setAbschlussnr(rs.getInt("AbschlussNr"));
//		[AbschlussID] [nvarchar](8) NULL,
		ka.setAbschlussid(rs.getString("Abschlussid"));
//		[Bar] [float] NULL,
		ka.setBar(rs.getBigDecimal("bar"));
//		[Lastschriften_Ist] [float] NULL, --------------------
//		[Lastschriften_Soll] [float] NULL, --------------------
//		[Bücherschecks_Ist] [float] NULL, --------------------
//		[Bücherschecks_Soll] [float] NULL, --------------------
//		[Kreditkarten_Ist] [float] NULL, --------------------
//		[Kreditkarten_Soll] [float] NULL, --------------------
//		[Telecash_Ist] [float] NULL,
		ka.setTelecashIst(rs.getBigDecimal("telecash_ist"));
//		[Teleash_Soll] [float] NULL, -------------------
//		[Anfang] [float] NULL,
		ka.setAnfang(rs.getBigDecimal("anfang"));
//		[Ist] [float] NULL,
		ka.setIst(rs.getBigDecimal("Ist"));
//		[Soll] [float] NULL,  -------------------
		ka.setSoll(rs.getBigDecimal("Soll"));
//		[Differenz] [float] NULL,  -------------------
		ka.setDifferenz(rs.getBigDecimal("Differenz"));
//		[Abschöpfung] [float] NULL,
		ka.setAbschoepfung(rs.getBigDecimal("Abschöpfung"));
//		[Vortrag] [float] NULL,    -------------------
//		[Einzahlungen] [float] NULL,
		ka.setEinzahlungen(rs.getBigDecimal("Einzahlungen"));
//		[Auszahlungen] [float] NULL,
		ka.setAuszahlungen(rs.getBigDecimal("Auszahlungen"));
//		[Gutscheine] [float] NULL,
		ka.setGutscheine(rs.getBigDecimal("Gutscheine"));
//		[Gutschriften] [float] NULL,
		ka.setGutschriften(rs.getBigDecimal("Gutschriften"));
//		[Umsatz_Voll] [float] NULL,
		ka.setUmsatzVoll(rs.getBigDecimal("Umsatz_Voll"));
//		[Umsatz_Halb] [float] NULL,
		ka.setUmsatzHalb(rs.getBigDecimal("Umsatz_Halb"));
//		[Umsatz_Ohne] [float] NULL,
		ka.setUmsatzOhne(rs.getBigDecimal("Umsatz_Ohne"));
//		[BezahlteRechnungen] [float] NULL,
		ka.setBezahlteRechnungen(rs.getBigDecimal("BezahlteRechnungen"));
//		[AnzahlKunden] [float] NULL,
		ka.setAnzahlKunden(rs.getInt("AnzahlKunden"));
//		[LadenUmsatz] [float] NULL,
		ka.setLadenUmsatz(rs.getBigDecimal("LadenUmsatz"));
//		[BesorgungsUmsatz] [float] NULL,
		ka.setBesorgungsUmsatz(rs.getBigDecimal("BesorgungsUmsatz"));
//		[Geändert] [int] NULL,   ------------------
//		[LetzteÄnderung] [datetime] NULL,  -------------------------
//		[Bemerkung] [nvarchar](100) NULL,  ----------------------
//		[Zeitmarke] [timestamp] NULL,  ----------------------
//		[VonDatum] [datetime] NULL,
		ka.setVonDatum(new DateTime(rs.getTimestamp("vondatum")));
//		[BisDatum] [datetime] NULL,
		ka.setBisDatum(new DateTime(rs.getTimestamp("bisdatum")));
//		[Kassierer] [int] NULL,  --------------------
//		[TeleCash_soll] [float] NULL,  --------------------
//		[Gebucht] [bit] NOT NULL,   --------------------
//		[Auszahlungen_Voll] [float] NULL,
		ka.setAuszahlungenVoll(rs.getBigDecimal("Auszahlungen_Voll"));
//		[Auszahlungen_Halb] [float] NULL,
		ka.setAuszahlungenHalb(rs.getBigDecimal("auszahlungen_halb"));
//		[Auszahlungen_Ohne] [float] NULL,
		ka.setAuszahlungenOhne(rs.getBigDecimal("auszahlungen_ohne"));
//	 CONSTRAINT [PK_Kasse_AbschlussDaten] PRIMARY KEY CLUSTERED 
//	(
//		[ID] ASC
//	)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
//	) ON [PRIMARY]
		
		return ka;
	}

}
