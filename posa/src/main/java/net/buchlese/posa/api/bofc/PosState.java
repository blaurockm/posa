package net.buchlese.posa.api.bofc;

import java.util.EnumMap;
import java.util.Map;

import org.joda.time.Instant;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosState implements SendableObject {

	@JsonProperty
	private Instant timest;

	@JsonProperty
	private LocalDate stateDate;

	@JsonProperty
	private Long revenue;     // Umsatz
	
	@JsonProperty
	private Long profit;

	@JsonProperty
	private Map<PaymentMethod, Long> paymentMethodBalance = new EnumMap<PaymentMethod, Long>(PaymentMethod.class);

	public Instant getTimest() {
		return timest;
	}

	public void setTimest(Instant timest) {
		this.timest = timest;
	}

	public LocalDate getStateDate() {
		return stateDate;
	}

	public void setStateDate(LocalDate stateDate) {
		this.stateDate = stateDate;
	}

	public Long getProfit() {
		return profit;
	}

	public void setProfit(Long profit) {
		this.profit = profit;
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
