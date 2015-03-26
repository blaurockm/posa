package net.buchlese.bofc.api.bofc;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccrualWeek {
	@JsonProperty
	private Long revenue;     // Umsatz
	@JsonProperty
	private Long profit;     // Profit
	@JsonProperty
	private int ticketCount;
	@JsonProperty
	private int week;
	@JsonProperty
	private int year;
	@JsonProperty
	private DateTime firstDay;
	@JsonProperty
	private DateTime lastDay;
	@JsonProperty
	private String memo;
	@JsonProperty
	private Map<Integer, List<PosCashBalance>> balances;
	public Long getRevenue() {
		return revenue;
	}
	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}
	public Long getProfit() {
		return profit;
	}
	public void setProfit(Long profit) {
		this.profit = profit;
	}
	public int getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public DateTime getFirstDay() {
		return firstDay;
	}
	public void setFirstDay(DateTime firstDay) {
		this.firstDay = firstDay;
	}
	public DateTime getLastDay() {
		return lastDay;
	}
	public void setLastDay(DateTime lastDay) {
		this.lastDay = lastDay;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Map<Integer, List<PosCashBalance>> getBalances() {
		return balances;
	}
	public void setBalances(Map<Integer, List<PosCashBalance>> balances) {
		this.balances = balances;
	}

}
