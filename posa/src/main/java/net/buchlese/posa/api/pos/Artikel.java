package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artikel {
	@NotEmpty
	@JsonProperty
	private int artikelident;
	
	@NotEmpty
	@JsonProperty
	private String isbn;

	@JsonProperty
	private String artikelnummer;

	@JsonProperty
	private String ean;

	@JsonProperty
	private String matchcode;
	
	@JsonProperty
	private String bezeichnung;

	@JsonProperty
	private String verlag;

	@JsonProperty
	private String autor;

	@JsonProperty
	private BigDecimal vK;

	@JsonProperty
	private BigDecimal eK;
	
	@JsonProperty
	private Character mWSt;

	@JsonProperty
	private Character warGrIndex;
	
	@JsonProperty
	private BigDecimal bestand;

	@JsonProperty
	private DateTime letztesEinkaufsdatum;

	@JsonProperty
	private DateTime letztesVerkaufsdatum;

	@JsonProperty
	private BigDecimal zeitmarke;
	

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

	public String getArtikelnummer() {
		return artikelnummer;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artikelnummer = artikelnummer;
	}

	public String getMatchcode() {
		return matchcode;
	}

	public void setMatchcode(String matchcode) {
		this.matchcode = matchcode;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public BigDecimal getvK() {
		return vK;
	}

	public void setvK(BigDecimal vK) {
		this.vK = vK;
	}

	public BigDecimal geteK() {
		return eK;
	}

	public void seteK(BigDecimal eK) {
		this.eK = eK;
	}

	public Character getmWSt() {
		return mWSt;
	}

	public void setmWSt(Character mWSt) {
		this.mWSt = mWSt;
	}

	public Character getWarGrIndex() {
		return warGrIndex;
	}

	public void setWarGrIndex(Character warGrIndex) {
		this.warGrIndex = warGrIndex;
	}

	public BigDecimal getBestand() {
		return bestand;
	}

	public void setBestand(BigDecimal bestand) {
		this.bestand = bestand;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getVerlag() {
		return verlag;
	}

	public void setVerlag(String verlag) {
		this.verlag = verlag;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public DateTime getLetztesEinkaufsdatum() {
		return letztesEinkaufsdatum;
	}

	public void setLetztesEinkaufsdatum(DateTime letztesEinkaufsdatum) {
		this.letztesEinkaufsdatum = letztesEinkaufsdatum;
	}

	public DateTime getLetztesVerkaufsdatum() {
		return letztesVerkaufsdatum;
	}

	public void setLetztesVerkaufsdatum(DateTime letztesVerkaufsdatum) {
		this.letztesVerkaufsdatum = letztesVerkaufsdatum;
	}

	public BigDecimal getZeitmarke() {
		return zeitmarke;
	}

	public void setZeitmarke(BigDecimal zeitmarke) {
		this.zeitmarke = zeitmarke;
	}
	
	
}
