package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtikelBestandBuchung {
	@NotEmpty
	@JsonProperty
	private int id;
	
	@NotEmpty
	@JsonProperty
	private int artikelident;

	@JsonProperty
	private int bestandAlt;
	
	@JsonProperty
	private int menge;
	
	@JsonProperty
	private String bemerkung;
	
	@JsonProperty
	private int bestandNeu;

	@JsonProperty
	private DateTime datum;

	@JsonProperty
	private BigDecimal zeitmarke;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArtikelident() {
		return artikelident;
	}

	public void setArtikelident(int artikelident) {
		this.artikelident = artikelident;
	}

	public int getBestandAlt() {
		return bestandAlt;
	}

	public void setBestandAlt(int bestandAlt) {
		this.bestandAlt = bestandAlt;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public String getBemerkung() {
		return bemerkung;
	}

	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}

	public int getBestandNeu() {
		return bestandNeu;
	}

	public void setBestandNeu(int bestandNeu) {
		this.bestandNeu = bestandNeu;
	}

	public DateTime getDatum() {
		return datum;
	}

	public void setDatum(DateTime datum) {
		this.datum = datum;
	}

	public BigDecimal getZeitmarke() {
		return zeitmarke;
	}

	public void setZeitmarke(BigDecimal zeitmarke) {
		this.zeitmarke = zeitmarke;
	}

}
