package net.buchlese.bofc.api.coupon;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Coupon {
	@Id
	@NotNull
	@JsonProperty
	private Long id;
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
