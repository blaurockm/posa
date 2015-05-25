package net.buchlese.posa.api.pos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KassenGutschrift {
	@NotEmpty
	@JsonProperty
	private int gutschriftNr;

	@JsonProperty
	private DateTime anlageZeit;

	@JsonProperty
	private BigDecimal betrag;

	@JsonProperty
	private Boolean erledigt;

	@JsonProperty
	private DateTime erledigtAm;

	public int getGutschriftNr() {
		return gutschriftNr;
	}

	public void setGutschriftNr(int gutschriftNr) {
		this.gutschriftNr = gutschriftNr;
	}

	public DateTime getAnlageZeit() {
		return anlageZeit;
	}

	public void setAnlageZeit(DateTime anlageZeit) {
		this.anlageZeit = anlageZeit;
	}

	public BigDecimal getBetrag() {
		return betrag;
	}

	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}

	public Boolean getErledigt() {
		return erledigt;
	}

	public void setErledigt(Boolean erledigt) {
		this.erledigt = erledigt;
	}

	public DateTime getErledigtAm() {
		return erledigtAm;
	}

	public void setErledigtAm(DateTime erledigtAm) {
		this.erledigtAm = erledigtAm;
	}

}
