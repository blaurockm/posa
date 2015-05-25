package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;

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
	private String matchcode;
	
	@JsonProperty
	private String bezeichnung;
	
	@JsonProperty
	private BigDecimal vK;

	@JsonProperty
	private BigDecimal eK;
	
	@JsonProperty
	private Character mWSt;

	@JsonProperty
	private Character warGrIndex;
	
	@JsonProperty
	private int bestand;

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

	public int getBestand() {
		return bestand;
	}

	public void setBestand(int bestand) {
		this.bestand = bestand;
	}
	
	
}
