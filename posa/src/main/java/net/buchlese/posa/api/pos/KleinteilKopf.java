package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KleinteilKopf {
	@NotEmpty
	@JsonProperty
	private int id;
	@NotEmpty
	@JsonProperty
	private int rechnungsNummer;
	@NotEmpty
	@JsonProperty
	private int kundenNummer;
	@JsonProperty
	private String name1;
	@JsonProperty
	private String name2;
	@JsonProperty
	private String name3;
	@JsonProperty
	private String strasse;
	@JsonProperty
	private String ort;
	@JsonProperty
	private DateTime erfassungsDatum;
	@JsonProperty
	private DateTime rechnungsDatum;
	@JsonProperty
	private BigDecimal rabatt;
	@JsonProperty
	private BigDecimal skonto;
	@JsonProperty
	private BigDecimal brutto;
	@JsonProperty
	private BigDecimal mwst7;
	@JsonProperty
	private BigDecimal mwst19;
	@JsonProperty
	private BigDecimal netto0;
	@JsonProperty
	private BigDecimal netto7;
	@JsonProperty
	private BigDecimal netto19;
	@JsonProperty
	private Boolean bezahlt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRechnungsNummer() {
		return rechnungsNummer;
	}

	public void setRechnungsNummer(int rechnungsNummer) {
		this.rechnungsNummer = rechnungsNummer;
	}

	public int getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(int kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public DateTime getErfassungsDatum() {
		return erfassungsDatum;
	}

	public void setErfassungsDatum(DateTime erfassungsDatum) {
		this.erfassungsDatum = erfassungsDatum;
	}

	public DateTime getRechnungsDatum() {
		return rechnungsDatum;
	}

	public void setRechnungsDatum(DateTime rechnungsDatum) {
		this.rechnungsDatum = rechnungsDatum;
	}

	public BigDecimal getRabatt() {
		return rabatt;
	}

	public void setRabatt(BigDecimal rabatt) {
		this.rabatt = rabatt;
	}

	public BigDecimal getSkonto() {
		return skonto;
	}

	public void setSkonto(BigDecimal skonto) {
		this.skonto = skonto;
	}


	public void setBrutto(BigDecimal netto) {
		this.brutto = netto;
	}

	public BigDecimal getMwst7() {
		return mwst7;
	}

	public void setMwst7(BigDecimal mwst7) {
		this.mwst7 = mwst7;
	}

	public BigDecimal getMwst19() {
		return mwst19;
	}

	public void setMwst19(BigDecimal mwst19) {
		this.mwst19 = mwst19;
	}

	public void setNetto7(BigDecimal brutto7) {
		this.netto7 = brutto7;
	}
	public void setNetto19(BigDecimal brutto19) {
		this.netto19 = brutto19;
	}

	public Boolean getBezahlt() {
		return bezahlt;
	}

	public void setBezahlt(Boolean bezahlt) {
		this.bezahlt = bezahlt;
	}

	public BigDecimal getBrutto() {
		return brutto;
	}

	public BigDecimal getNetto7() {
		return netto7;
	}

	public BigDecimal getNetto19() {
		return netto19;
	}

	public BigDecimal getNetto0() {
		return netto0;
	}

	public void setNetto0(BigDecimal netto0) {
		this.netto0 = netto0;
	}

}
