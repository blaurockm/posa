package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KassenVorgang {
	@NotEmpty
	@JsonProperty
	private int kassenNr;
	@NotEmpty
	@JsonProperty
	private int belegNr;
	@NotEmpty
	@JsonProperty
	private int lfdNummer;
	@JsonProperty
	private int artikelident;
	@JsonProperty
	private String isbn;
	@JsonProperty
	private String artikelNummer;
	@JsonProperty
	private String matchCode;
	@JsonProperty
	private String Bezeichnung;
	@JsonProperty
	private String Beschreibung;
	@JsonProperty
	private BigDecimal menge;
	@JsonProperty
	private BigDecimal vK;
	@JsonProperty
	private BigDecimal gesamt;
	@JsonProperty
	private Character mWSt;
	@JsonProperty
	private Character vorgangsArt;
	@JsonProperty
	private double rabatt;
	@JsonProperty
	private String reservierungsnummer;
	@JsonProperty
	private DateTime datum;
	@JsonProperty
	private double mWStSatz;
//			CREATE TABLE [dbo].[KassenVorgänge](
//				[KassenNr] [int] NOT NULL,  -------------------
//				[BelegNr] [int] NOT NULL,
//				[LfdNummer] [int] NOT NULL,
//				[ArtikelIdent] [int] NULL,
//				[ISBN] [varchar](20) NULL,
//				[ArtikelNummer] [nvarchar](30) NULL,
//				[MatchCode] [varchar](70) NULL,
//				[Bezeichnung] [ntext] NULL,
//				[Beschreibung] [ntext] NULL,
//				[Menge] [float] NULL,
//				[VK] [float] NULL,
//				[Gesamt] [float] NULL,
//				[Datum] [datetime] NULL,
//				[LagerNummer] [nvarchar](6) NULL, -------------------
//				[MWSt] [nvarchar](1) NULL,
//				[VorgangsArt] [nvarchar](1) NULL,
//				[NichtImLager] [bit] NOT NULL,  ------------------------
//				[VorgangsArtikel] [bit] NOT NULL, -------------------------
//				[Rabatt] [float] NULL,
//				[ReservierungsNummer] [nvarchar](12) NULL,
//				[Lager] [int] NULL,  -------------------------
//				[Vk2] [float] NULL,  ------------------------
//				[BestandNachVK] [float] NULL,  ---------------------
//				[LetzterAbgleich] [datetime] NULL,  -------------------
//				[LagerBereitsGebucht] [bit] NOT NULL, -------------------
//				[VerleihTage] [smallint] NULL,  -----------------
//				[VerleihVorgang] [bit] NOT NULL,    -----------------
//				[VerleihvorgangsNummer] [int] NULL,    -----------------
//				[VerleihRückgabe] [bit] NOT NULL,    -----------------
//				[GutscheinVonKasse] [int] NULL,   -----------------
//				[LG_Buch_Übertragen] [bit] NOT NULL,   -----------------
//				[VonVerlag] [bit] NOT NULL,   -----------------
//				[FiBu_Konto] [varchar](5) NULL,   -----------------
//				[ReservierungsArt] [int] NULL,  -----------------
//				[Zeitmarke] [timestamp] NOT NULL,
//				[Gebucht] [bit] NOT NULL,  ----------------
//				[MwstSatz] [float] NULL,
//				[GutscheinVonFilialNr] [int] NULL,    ----------------
//			 CONSTRAINT [PK_KassenVorgänge] PRIMARY KEY CLUSTERED 
//			(
//				[KassenNr] ASC,
//				[BelegNr] ASC,
//				[LfdNummer] ASC
//			)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
//			) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
	public int getBelegNr() {
		return belegNr;
	}
	public int getKassenNr() {
		return kassenNr;
	}
	public void setKassenNr(int kassenNr) {
		this.kassenNr = kassenNr;
	}
	public void setBelegNr(int belegNr) {
		this.belegNr = belegNr;
	}
	public int getLfdNummer() {
		return lfdNummer;
	}
	public void setLfdNummer(int lfdNummer) {
		this.lfdNummer = lfdNummer;
	}
	public int getArtikelident() {
		return artikelident;
	}
	public void setArtikelident(int artikelident) {
		this.artikelident = artikelident;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getArtikelNummer() {
		return artikelNummer;
	}
	public void setArtikelNummer(String artikelNummer) {
		this.artikelNummer = artikelNummer;
	}
	public String getMatchCode() {
		return matchCode;
	}
	public void setMatchCode(String matchCode) {
		this.matchCode = matchCode;
	}
	public String getBezeichnung() {
		return Bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		Bezeichnung = bezeichnung;
	}
	public String getBeschreibung() {
		return Beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		Beschreibung = beschreibung;
	}
	public BigDecimal getMenge() {
		return menge;
	}
	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}
	public BigDecimal getVK() {
		return vK;
	}
	public void setVK(BigDecimal vK) {
		this.vK = vK;
	}
	public BigDecimal getGesamt() {
		return gesamt;
	}
	public void setGesamt(BigDecimal gesamt) {
		this.gesamt = gesamt;
	}
	public Character getMWSt() {
		return mWSt;
	}
	public void setMWSt(Character mWSt) {
		this.mWSt = mWSt;
	}
	public Character getVorgangsArt() {
		return vorgangsArt;
	}
	public void setVorgangsArt(Character vorgangsArt) {
		this.vorgangsArt = vorgangsArt;
	}
	public double getRabatt() {
		return rabatt;
	}
	public void setRabatt(double rabatt) {
		this.rabatt = rabatt;
	}
	public String getReservierungsnummer() {
		return reservierungsnummer;
	}
	public void setReservierungsnummer(String reservierungsnummer) {
		this.reservierungsnummer = reservierungsnummer;
	}
	public DateTime getDatum() {
		return datum;
	}
	public void setDatum(DateTime datume) {
		this.datum = datume;
	}
	public double getMWStSatz() {
		return mWStSatz;
	}
	public void setMWStSatz(double mWStSatz) {
		this.mWStSatz = mWStSatz;
	}

}
