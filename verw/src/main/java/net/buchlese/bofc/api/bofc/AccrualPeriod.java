package net.buchlese.bofc.api.bofc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AccrualPeriod {
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
	private LocalDateTime firstDay;
	@JsonProperty
	private LocalDateTime lastDay;
	@JsonProperty
	private Long goodsOut;  // Warenverkäufe
	@JsonProperty
	private Map<Integer, List<PosCashBalance>> balances;
	@JsonProperty
	private Map<String, Long> articleGroupBalance;
	@JsonProperty
	private Long couponTradeIn;  // Gutscheine angenommen
	@JsonProperty
	private Long couponTradeOut;  // Gutscheine verkauft
	@JsonProperty
	private Long payedInvoicesSum;  // Rechnungen bezahlt
	@JsonProperty
	private Long createdInvoicesSum;  // Rechnungen ausgestellt
	@JsonProperty
	private Long cashInSum;  // Rechnungen bezahlt
	@JsonProperty
	private Long cashOutSum;  // Rechnungen ausgestellt
	@JsonProperty
	private Long absorption;  // abschöpfung
	@JsonProperty
	private Long cash;  // bar einnahmen
	@JsonProperty
	private Long telecash;  // telecash einnahmen
	@JsonProperty
	private String memo;
	@JsonIgnore
	private List<String> problems;
	@JsonIgnore
	private List<PosCashBalance> allBalances;
	
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
	public LocalDateTime getFirstDay() {
		return firstDay;
	}
	public void setFirstDay(LocalDateTime firstDay) {
		this.firstDay = firstDay;
	}
	public LocalDateTime getLastDay() {
		return lastDay;
	}
	public void setLastDay(LocalDateTime lastDay) {
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
	public Map<String, Long> getArticleGroupBalance() {
		return articleGroupBalance;
	}
	public void setArticleGroupBalance(Map<String, Long> articleGroupBalance) {
		this.articleGroupBalance = articleGroupBalance;
	}
	public Long getCouponTradeIn() {
		return couponTradeIn;
	}
	public void setCouponTradeIn(Long couponTradeIn) {
		this.couponTradeIn = couponTradeIn;
	}
	public Long getCouponTradeOut() {
		return couponTradeOut;
	}
	public void setCouponTradeOut(Long couponTradeOut) {
		this.couponTradeOut = couponTradeOut;
	}
	public Long getPayedInvoicesSum() {
		return payedInvoicesSum;
	}
	public void setPayedInvoicesSum(Long payedInvoicesSum) {
		this.payedInvoicesSum = payedInvoicesSum;
	}
	public Long getCreatedInvoicesSum() {
		return createdInvoicesSum;
	}
	public void setCreatedInvoicesSum(Long createdInvoicesSum) {
		this.createdInvoicesSum = createdInvoicesSum;
	}
	public Long getCashInSum() {
		return cashInSum;
	}
	public void setCashInSum(Long cashInSum) {
		this.cashInSum = cashInSum;
	}
	public Long getCashOutSum() {
		return cashOutSum;
	}
	public void setCashOutSum(Long cashOutSum) {
		this.cashOutSum = cashOutSum;
	}
	public Long getAbsorption() {
		return absorption;
	}
	public void setAbsorption(Long absorption) {
		this.absorption = absorption;
	}
	public Long getCash() {
		return cash;
	}
	public void setCash(Long cash) {
		this.cash = cash;
	}
	public Long getTelecash() {
		return telecash;
	}
	public void setTelecash(Long telecash) {
		this.telecash = telecash;
	}
	public Long getGoodsOut() {
		return goodsOut;
	}
	public void setGoodsOut(Long goodsOut) {
		this.goodsOut = goodsOut;
	}
	public List<String> getProblems() {
		return problems;
	}
	public void setProblems(List<String> problems) {
		this.problems = problems;
	}
	public List<PosCashBalance> getAllBalances() {
		return allBalances;
	}
	public void setAllBalances(List<PosCashBalance> allBalances) {
		this.allBalances = allBalances;
	}

}
