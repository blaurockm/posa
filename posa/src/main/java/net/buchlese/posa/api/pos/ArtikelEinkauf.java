package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtikelEinkauf {
	@NotEmpty
	@JsonProperty
	private int artikelident;

	@JsonProperty
	private BigDecimal eK;

	@JsonProperty
	private DateTime letzterEintrag;

	@JsonProperty
	private BigDecimal zeitmarke;

	public int getArtikelident() {
		return artikelident;
	}

	public void setArtikelident(int artikelident) {
		this.artikelident = artikelident;
	}

	public BigDecimal geteK() {
		return eK;
	}

	public void seteK(BigDecimal eK) {
		this.eK = eK;
	}

	public DateTime getLetzterEintrag() {
		return letzterEintrag;
	}

	public void setLetzterEintrag(DateTime letzterEintrag) {
		this.letzterEintrag = letzterEintrag;
	}

	public BigDecimal getZeitmarke() {
		return zeitmarke;
	}

	public void setZeitmarke(BigDecimal zeitmarke) {
		this.zeitmarke = zeitmarke;
	}

}
