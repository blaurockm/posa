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
	private String bezeichnung;

	@JsonProperty
	private String kennziffer;

	@JsonProperty
	private int textbaustein;

	@JsonProperty
	private BigDecimal menge;
	@JsonProperty
	private BigDecimal bruttoEinzel;
	@JsonProperty
	private BigDecimal rabattEinzel;
	@JsonProperty
	private BigDecimal rabattSatz;
	@JsonProperty
	private Character mwstkz;

	@JsonProperty
	private DateTime letzteBearbeitung;

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


	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getKennziffer() {
		return kennziffer;
	}

	public void setKennziffer(String kennziffer) {
		this.kennziffer = kennziffer;
	}

	public int getTextbaustein() {
		return textbaustein;
	}

	public void setTextbaustein(int textbaustein) {
		this.textbaustein = textbaustein;
	}

	public DateTime getLetzteBearbeitung() {
		return letzteBearbeitung;
	}

	public void setLetzteBearbeitung(DateTime letzteBearbeitung) {
		this.letzteBearbeitung = letzteBearbeitung;
	}


	public Character getMwstkz() {
		return mwstkz;
	}

	public void setMwstkz(Character mwstkz) {
		this.mwstkz = mwstkz;
	}

	public BigDecimal getBruttoEinzel() {
		return bruttoEinzel;
	}

	public void setBruttoEinzel(BigDecimal bruttoEinzel) {
		this.bruttoEinzel = bruttoEinzel;
	}

	public BigDecimal getRabattEinzel() {
		return rabattEinzel;
	}

	public void setRabattEinzel(BigDecimal rabattEinzel) {
		this.rabattEinzel = rabattEinzel;
	}

	public BigDecimal getRabattSatz() {
		return rabattSatz;
	}

	public void setRabattSatz(BigDecimal rabattSatz) {
		this.rabattSatz = rabattSatz;
	}



}
