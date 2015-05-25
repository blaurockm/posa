package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KleinteilElement {
	@NotEmpty
	@JsonProperty
	private int rechnungsNummer;
	
	@JsonProperty
	private int positionsNummer;
	@JsonProperty
	private String isbn;
	@JsonProperty
	private String artikelNummer;
	@JsonProperty
	private String matchCode;

	@JsonProperty
	private BigDecimal menge;
	@JsonProperty
	private BigDecimal vK;
	@JsonProperty
	private BigDecimal rabatt;
	@JsonProperty
	private Character mWSt;

	@JsonProperty
	private DateTime letzeBearbeitung;

	public int getRechnungsNummer() {
		return rechnungsNummer;
	}

	public void setRechnungsNummer(int rechnungsNummer) {
		this.rechnungsNummer = rechnungsNummer;
	}

	public int getPositionsNummer() {
		return positionsNummer;
	}

	public void setPositionsNummer(int positionsNummer) {
		this.positionsNummer = positionsNummer;
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

	public BigDecimal getMenge() {
		return menge;
	}

	public void setMenge(BigDecimal menge) {
		this.menge = menge;
	}

	public BigDecimal getvK() {
		return vK;
	}

	public void setvK(BigDecimal vK) {
		this.vK = vK;
	}

	public BigDecimal getRabatt() {
		return rabatt;
	}

	public void setRabatt(BigDecimal rabatt) {
		this.rabatt = rabatt;
	}

	public Character getmWSt() {
		return mWSt;
	}

	public void setmWSt(Character mWSt) {
		this.mWSt = mWSt;
	}

	public DateTime getLetzeBearbeitung() {
		return letzeBearbeitung;
	}

	public void setLetzeBearbeitung(DateTime letzeBearbeitung) {
		this.letzeBearbeitung = letzeBearbeitung;
	}


}
