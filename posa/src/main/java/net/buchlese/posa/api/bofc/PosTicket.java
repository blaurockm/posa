package net.buchlese.posa.api.bofc;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosTicket {
	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private long belegNr;

	@NotEmpty
	@JsonProperty
	private Long total;

	@JsonProperty
	private boolean cancelled;

	@NotEmpty
	@JsonProperty
	private PaymentMethod paymentMethod;

	@NotEmpty
	@JsonProperty
	private DateTime timestamp;

	@JsonProperty
	private boolean toBeCheckedAgain;

	public long getId() {
		return id;
	}

	public long getBelegNr() {
		return belegNr;
	}

	public void setBelegNr(long belegNr) {
		this.belegNr = belegNr;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public boolean isToBeCheckedAgain() {
		return toBeCheckedAgain;
	}

	public void setToBeCheckedAgain(boolean toBeCheckedAgain) {
		this.toBeCheckedAgain = toBeCheckedAgain;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}


}
