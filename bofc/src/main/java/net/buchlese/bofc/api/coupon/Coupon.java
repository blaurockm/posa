package net.buchlese.bofc.api.coupon;

import io.dropwizard.jackson.Jackson;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Coupon {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private int customerId;  // die Schule
	@JsonProperty
	private String pupilsname;
	@JsonProperty
	private String reason;
	@JsonProperty
	private String pupilyear;
	@JsonProperty
	private String pupilclass;
	@JsonProperty
	private LocalDate acceptDate;
	@JsonProperty
	private long amount;
	@JsonProperty
	private boolean payed;
	
	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPointid() {
		return pointid;
	}
	public void setPointid(int pointid) {
		this.pointid = pointid;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getPupilsname() {
		return pupilsname;
	}

	public void setPupilsname(String pupilsname) {
		this.pupilsname = pupilsname;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPupilyear() {
		return pupilyear;
	}

	public void setPupilyear(String pupilyear) {
		this.pupilyear = pupilyear;
	}

	public String getPupilclass() {
		return pupilclass;
	}

	public void setPupilclass(String pupilclass) {
		this.pupilclass = pupilclass;
	}

	public LocalDate getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(LocalDate acceptDate) {
		this.acceptDate = acceptDate;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

}
