package net.buchlese.posa.api.bofc;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import net.buchlese.posa.xml.LocalDateSerializer;

import org.joda.time.Instant;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PosState implements SendableObject {

	@JsonProperty
	private int pointid;

	@JsonProperty
	private Instant timest;

	@JsonProperty
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate stateDate;

	@JsonProperty
	private Long revenue;     // Umsatz
	
	@JsonProperty
	private Long profit;

	@JsonProperty
	private Map<PaymentMethod, Long> paymentMethodBalance = new EnumMap<PaymentMethod, Long>(PaymentMethod.class);

	@JsonProperty
	private List<PosSyncState> syncStates;

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

	public int getPointid() {
		return pointid;
	}

	public void setPointid(final int pointid) {
		if (syncStates != null) {
			syncStates.forEach(x -> x.setPointid(pointid));
		}
		this.pointid = pointid;
	}

	@Override
	public String toString() {
		return "PosState [timest=" + timest + "]";
	}

	public List<PosSyncState> getSyncStates() {
		return syncStates;
	}

	public void setSyncStates(List<PosSyncState> syncStates) {
		this.syncStates = syncStates;
	}

	
}
