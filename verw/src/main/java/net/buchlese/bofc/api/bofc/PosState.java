package net.buchlese.bofc.api.bofc;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosState  {

	@JsonProperty
	private int pointid;

	@JsonProperty
	private java.sql.Timestamp timest;

	@JsonProperty
	private java.sql.Date stateDate;

	@JsonProperty
	private Long revenue;     // Umsatz
	
	@JsonProperty
	private Long profit;

	@JsonProperty
	private Map<PaymentMethod, Long> paymentMethodBalance = new EnumMap<PaymentMethod, Long>(PaymentMethod.class);

	@JsonProperty
	private List<PosSyncState> syncStates;
	
	public java.sql.Timestamp getTimest() {
		return timest;
	}

	public void setTimest(java.sql.Timestamp timest) {
		this.timest = timest;
	}

	public java.sql.Date getStateDate() {
		return stateDate;
	}

	public void setStateDate(java.sql.Date stateDate) {
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

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public List<PosSyncState> getSyncStates() {
		return syncStates;
	}

	public void setSyncStates(List<PosSyncState> syncStates) {
		this.syncStates = syncStates;
	}

	
}
