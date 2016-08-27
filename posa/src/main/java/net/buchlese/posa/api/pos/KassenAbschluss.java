package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KassenAbschluss {
	@NotEmpty
	@JsonProperty
	private int id;
	@NotEmpty
	@JsonProperty
	private int kasse;
	@NotEmpty
	@JsonProperty
	private int abschlussnr;
	@NotEmpty
	@JsonProperty
	private String abschlussid;
	@JsonProperty
	private BigDecimal bar;
	@JsonProperty
	private BigDecimal telecashIst;
	@JsonProperty
	private BigDecimal anfang;
	@JsonProperty
	private BigDecimal ist;
	@JsonProperty
	private BigDecimal soll;
	@JsonProperty
	private BigDecimal differenz;
	@JsonProperty
	private BigDecimal abschoepfung;
	@JsonProperty
	private BigDecimal einzahlungen;
	@JsonProperty
	private BigDecimal auszahlungen;
	@JsonProperty
	private BigDecimal gutscheine;
	@JsonProperty
	private BigDecimal gutschriften;
	@JsonProperty
	private BigDecimal umsatzVoll;
	@JsonProperty
	private BigDecimal umsatzHalb;
	@JsonProperty
	private BigDecimal umsatzOhne;
	@JsonProperty
	private BigDecimal bezahlteRechnungen;
	@JsonProperty
	private int anzahlKunden;
	@JsonProperty
	private BigDecimal ladenUmsatz;
	@JsonProperty
	private BigDecimal besorgungsUmsatz;
	@JsonProperty
	private DateTime vonDatum;
	@JsonProperty
	private DateTime bisDatum;
	@JsonProperty
	private BigDecimal auszahlungenVoll;
	@JsonProperty
	private BigDecimal auszahlungenHalb;
	@JsonProperty
	private BigDecimal auszahlungenOhne;
	@JsonProperty
	private BigDecimal zeitmarke;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getKasse() {
		return kasse;
	}
	public void setKasse(int kasse) {
		this.kasse = kasse;
	}
	public int getAbschlussnr() {
		return abschlussnr;
	}
	public void setAbschlussnr(int abschlussnr) {
		this.abschlussnr = abschlussnr;
	}
	public String getAbschlussid() {
		return abschlussid;
	}
	public void setAbschlussid(String abschlussid) {
		this.abschlussid = abschlussid;
	}
	public BigDecimal getBar() {
		return bar;
	}
	public void setBar(BigDecimal bar) {
		this.bar = bar;
	}
	public BigDecimal getTelecashIst() {
		return telecashIst;
	}
	public void setTelecashIst(BigDecimal telecashIst) {
		this.telecashIst = telecashIst;
	}
	public BigDecimal getAnfang() {
		return anfang;
	}
	public void setAnfang(BigDecimal anfang) {
		this.anfang = anfang;
	}
	public BigDecimal getIst() {
		return ist;
	}
	public void setIst(BigDecimal ist) {
		this.ist = ist;
	}
	public BigDecimal getAbschoepfung() {
		return abschoepfung;
	}
	public void setAbschoepfung(BigDecimal abschoepfung) {
		this.abschoepfung = abschoepfung;
	}
	public BigDecimal getEinzahlungen() {
		return einzahlungen;
	}
	public void setEinzahlungen(BigDecimal einzahlungen) {
		this.einzahlungen = einzahlungen;
	}
	public BigDecimal getAuszahlungen() {
		return auszahlungen;
	}
	public void setAuszahlungen(BigDecimal auszahlungen) {
		this.auszahlungen = auszahlungen;
	}
	public BigDecimal getGutscheine() {
		return gutscheine;
	}
	public void setGutscheine(BigDecimal gutscheine) {
		this.gutscheine = gutscheine;
	}
	public BigDecimal getGutschriften() {
		return gutschriften;
	}
	public void setGutschriften(BigDecimal gutschriften) {
		this.gutschriften = gutschriften;
	}
	public BigDecimal getUmsatzVoll() {
		return umsatzVoll;
	}
	public void setUmsatzVoll(BigDecimal umsatzVoll) {
		this.umsatzVoll = umsatzVoll;
	}
	public BigDecimal getUmsatzHalb() {
		return umsatzHalb;
	}
	public void setUmsatzHalb(BigDecimal umsatzHalb) {
		this.umsatzHalb = umsatzHalb;
	}
	public BigDecimal getUmsatzOhne() {
		return umsatzOhne;
	}
	public void setUmsatzOhne(BigDecimal umsatzOhne) {
		this.umsatzOhne = umsatzOhne;
	}
	public BigDecimal getBezahlteRechnungen() {
		return bezahlteRechnungen;
	}
	public void setBezahlteRechnungen(BigDecimal bezahlteRechungen) {
		this.bezahlteRechnungen = bezahlteRechungen;
	}
	public int getAnzahlKunden() {
		return anzahlKunden;
	}
	public void setAnzahlKunden(int anzahlKunden) {
		this.anzahlKunden = anzahlKunden;
	}
	public BigDecimal getLadenUmsatz() {
		return ladenUmsatz;
	}
	public void setLadenUmsatz(BigDecimal ladenUmsatz) {
		this.ladenUmsatz = ladenUmsatz;
	}
	public BigDecimal getBesorgungsUmsatz() {
		return besorgungsUmsatz;
	}
	public void setBesorgungsUmsatz(BigDecimal besorgungsUmsatz) {
		this.besorgungsUmsatz = besorgungsUmsatz;
	}
	public DateTime getVonDatum() {
		return vonDatum;
	}
	public void setVonDatum(DateTime vonDatum) {
		this.vonDatum = vonDatum;
	}
	public DateTime getBisDatum() {
		return bisDatum;
	}
	public void setBisDatum(DateTime bisDatum) {
		this.bisDatum = bisDatum;
	}
	public BigDecimal getAuszahlungenVoll() {
		return auszahlungenVoll;
	}
	public void setAuszahlungenVoll(BigDecimal auszahlungenVoll) {
		this.auszahlungenVoll = auszahlungenVoll;
	}
	public BigDecimal getAuszahlungenHalb() {
		return auszahlungenHalb;
	}
	public void setAuszahlungenHalb(BigDecimal auszahlungenHalb) {
		this.auszahlungenHalb = auszahlungenHalb;
	}
	public BigDecimal getAuszahlungenOhne() {
		return auszahlungenOhne;
	}
	public void setAuszahlungenOhne(BigDecimal auszahlungenOhne) {
		this.auszahlungenOhne = auszahlungenOhne;
	}
	public BigDecimal getSoll() {
		return soll;
	}
	public void setSoll(BigDecimal soll) {
		this.soll = soll;
	}
	public BigDecimal getDifferenz() {
		return differenz;
	}
	public void setDifferenz(BigDecimal differenz) {
		this.differenz = differenz;
	}
	public BigDecimal getZeitmarke() {
		return zeitmarke;
	}
	public void setZeitmarke(BigDecimal zeitmarke) {
		this.zeitmarke = zeitmarke;
	}
	
	
//			CREATE TABLE [dbo].[Kasse_AbschlussDaten](
//				[ID] [int] IDENTITY(1,1) NOT NULL,
//				[Kasse] [int] NULL,
//				[AbschlussNr] [int] NULL,
//				[AbschlussID] [nvarchar](8) NULL,
//				[Bar] [float] NULL,
//				[Lastschriften_Ist] [float] NULL, --------------------
//				[Lastschriften_Soll] [float] NULL, --------------------
//				[Bücherschecks_Ist] [float] NULL, --------------------
//				[Bücherschecks_Soll] [float] NULL, --------------------
//				[Kreditkarten_Ist] [float] NULL, --------------------
//				[Kreditkarten_Soll] [float] NULL, --------------------
//				[Telecash_Ist] [float] NULL,
//				[Teleash_Soll] [float] NULL, -------------------
//				[Anfang] [float] NULL,
//				[Ist] [float] NULL,
//				[Soll] [float] NULL,  -------------------
//				[Differenz] [float] NULL,  -------------------
//				[Abschöpfung] [float] NULL,
//				[Vortrag] [float] NULL,    -------------------
//				[Einzahlungen] [float] NULL,
//				[Auszahlungen] [float] NULL,
//				[Gutscheine] [float] NULL,
//				[Gutschriften] [float] NULL,
//				[Umsatz_Voll] [float] NULL,
//				[Umsatz_Halb] [float] NULL,
//				[Umsatz_Ohne] [float] NULL,
//				[BezahlteRechnungen] [float] NULL,
//				[AnzahlKunden] [float] NULL,
//				[LadenUmsatz] [float] NULL,
//				[BesorgungsUmsatz] [float] NULL,
//				[Geändert] [int] NULL,   ------------------
//				[LetzteÄnderung] [datetime] NULL,  -------------------------
//				[Bemerkung] [nvarchar](100) NULL,  ----------------------
//				[Zeitmarke] [timestamp] NULL,  ----------------------
//				[VonDatum] [datetime] NULL,
//				[BisDatum] [datetime] NULL,
//				[Kassierer] [int] NULL,  --------------------
//				[TeleCash_soll] [float] NULL,  --------------------
//				[Gebucht] [bit] NOT NULL,   --------------------
//				[Auszahlungen_Voll] [float] NULL,
//				[Auszahlungen_Halb] [float] NULL,
//				[Auszahlungen_Ohne] [float] NULL,
//			 CONSTRAINT [PK_Kasse_AbschlussDaten] PRIMARY KEY CLUSTERED 
//			(
//				[ID] ASC
//			)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
//			) ON [PRIMARY]

}
