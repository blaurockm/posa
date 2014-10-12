package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KassenBeleg {
	@NotEmpty
	@JsonProperty
	private int kassennr;

	@NotEmpty
	@JsonProperty
	private long belegnr;

	@JsonProperty
	private String bemerkung;

	@JsonProperty
	private String rechnungNr;

	@NotEmpty
	@JsonProperty
	private DateTime zahlungszeit;

	@JsonProperty
	private boolean storniert;

	@JsonProperty
	private double nachlassProz;

	@JsonProperty
	private String stornoBemerkung;

	@NotEmpty
	@JsonProperty
	private BigDecimal zahlungsBetrag;

	@JsonProperty
	private int zahlungsart;

	@NotEmpty
	@JsonProperty
	private boolean geparkt; 
	
	@JsonProperty
	private int belegart;
//			CREATE TABLE [dbo].[KassenBelege](
//				[KassenNr] [int] NOT NULL,
//				[BelegNr] [int] NOT NULL,
//				[KassiererNummer] [int] NULL,  -----------------
//				[Bemerkung] [nvarchar](30) NULL,
//				[RechnungNr] [varchar](15) NULL,
//				[AdrGruppe] [tinyint] NULL,   -----------------
//				[AdrNummer] [int] NULL,     -----------------
//				[Kundensuchname] [nvarchar](40) NULL,    -----------------
//				[ZahlungsZeit] [datetime] NULL,
//				[Datum] [datetime] NULL,
//				[Geparkt] [bit] NOT NULL,
//				[Storniert] [bit] NOT NULL,
//				[Storno] [bit] NOT NULL,    -----------------
//				[Nachlass_Proz] [float] NULL,
//				[Nachlass_DM] [float] NULL,     -----------------
//				[StornoBemerkung] [nvarchar](50) NULL,
//				[Initialien] [nvarchar](50) NULL,     -----------------
//				[LetzterAbgleich] [datetime] NULL,     -----------------
//				[Gebucht] [bit] NOT NULL,     -----------------
//				[BuchungsDatum] [datetime] NULL,     -----------------
//				[ZahlungsBetrag] [float] NULL,
//				[ÜbergabeBetrag] [float] NULL,     -----------------
//				[RückgabeBetrag] [float] NULL,     -----------------
//				[Skonto] [float] NULL,     -----------------
//				[ZahlungsArt] [smallint] NULL,
//				[GiroKontoNr] [varchar](10) NULL,     -----------------
//				[BLZ] [varchar](8) NULL,     -----------------
//				[BelegArt] [tinyint] NULL,      -----------------
//				[Konto_Inhaber] [varchar](30) NULL,     -----------------
//				[Gesellschaft] [varchar](15) NULL,     -----------------
//				[Zeitmarke] [timestamp] NOT NULL,     -----------------
//				[IBAN] [varchar](34) NULL,     -----------------
//				[BIC] [varchar](11) NULL,     -----------------
//			 CONSTRAINT [PK_KassenBelege] PRIMARY KEY CLUSTERED 
//			(
//				[KassenNr] ASC,
//				[BelegNr] ASC

	public int getKassennr() {
		return kassennr;
	}

	public void setKassennr(int kassennr) {
		this.kassennr = kassennr;
	}

	public long getBelegnr() {
		return belegnr;
	}

	public void setBelegnr(long belegnr) {
		this.belegnr = belegnr;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public String getRechnungNr() {
		return rechnungNr;
	}

	public void setRechnungNr(String rechnungNr) {
		this.rechnungNr = rechnungNr;
	}

	public DateTime getZahlungszeit() {
		return zahlungszeit;
	}

	public void setZahlungszeit(DateTime zahlungszeit) {
		this.zahlungszeit = zahlungszeit;
	}

	public boolean isStorniert() {
		return storniert;
	}

	public void setStorniert(boolean storniert) {
		this.storniert = storniert;
	}

	public double getNachlassProz() {
		return nachlassProz;
	}

	public void setNachlassProz(double nachlassProz) {
		this.nachlassProz = nachlassProz;
	}

	public String getStornoBemerkung() {
		return stornoBemerkung;
	}

	public void setStornoBemerkung(String stornoBemerkung) {
		this.stornoBemerkung = stornoBemerkung;
	}

	public BigDecimal getZahlungsBetrag() {
		return zahlungsBetrag;
	}

	public void setZahlungsBetrag(BigDecimal zahlungsBetrag) {
		this.zahlungsBetrag = zahlungsBetrag;
	}

	public int getZahlungsart() {
		return zahlungsart;
	}

	public void setZahlungsart(int zahlungsart) {
		this.zahlungsart = zahlungsart;
	}

	public int getBelegart() {
		return belegart;
	}

	public void setBelegart(int belegart) {
		this.belegart = belegart;
	}

	public boolean isGeparkt() {
		return geparkt;
	}

	public void setGeparkt(boolean geparkt) {
		this.geparkt = geparkt;
	}


}
