package net.buchlese.posa.api.bofc;

import java.util.EnumMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosState {

	@JsonProperty
	private long timest;

	@JsonProperty
	private java.sql.Date stateDate;

	@JsonProperty
	private Long revenue;     // Umsatz

	@JsonProperty
	private Map<PaymentMethod, Long> paymentMethodBalance = new EnumMap<PaymentMethod, Long>(PaymentMethod.class);

	public long getTimest() {
		return timest;
	}

	public void setTimest(long timest) {
		this.timest = timest;
	}

	public java.sql.Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(java.sql.Date stateDate) {
		this.stateDate = stateDate;
	}

	public Long getRevenue() {
		return revenue;
	}

	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}

	public Map<PaymentMethod, Long> getPaymentMethodBalance() {
		return paymentMethodBalance;
	}

	public void setPaymentMethodBalance(
			Map<PaymentMethod, Long> paymentMethodBalance) {
		this.paymentMethodBalance = paymentMethodBalance;
	}
	
}
