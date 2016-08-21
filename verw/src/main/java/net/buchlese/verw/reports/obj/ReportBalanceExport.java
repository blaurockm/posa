package net.buchlese.verw.reports.obj;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import net.buchlese.bofc.api.bofc.PosCashBalance;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "balanceExport")
public class ReportBalanceExport {

	@JsonProperty
	private LocalDateTime execDate;
	@JsonProperty
	private String description;
	@JsonProperty
	private PosCashBalance firstBalance;
	@JsonProperty
	private PosCashBalance lastBalance;
	@JsonProperty
	private long cashStart;
	@JsonProperty
	private long cashEnd;
	@JsonProperty
	private long absorptionSum;
	@JsonProperty
	private long telecashSum;
	@JsonProperty
	private long revenueSum;
	@JsonProperty
	private long cashInSum;
	@JsonProperty
	private long cashOutSum;
	@JsonProperty
	private long invoicesPayedSum;
	@JsonProperty
	private long couponInSum;
	@JsonProperty
	private long couponOutSum;
	@JsonProperty
	private PosCashBalance[] balances;
	@JsonProperty
	private List<InvoicePay> invoicePays;
	
	public long getCashStart() {
		return cashStart;
	}
	public void setCashStart(long cashStart) {
		this.cashStart = cashStart;
	}
	public long getCashEnd() {
		return cashEnd;
	}
	public void setCashEnd(long cashEnd) {
		this.cashEnd = cashEnd;
	}
	public long getAbsorptionSum() {
		return absorptionSum;
	}
	public void setAbsorptionSum(long absorptionSum) {
		this.absorptionSum = absorptionSum;
	}
	public long getTelecashSum() {
		return telecashSum;
	}
	public void setTelecashSum(long telecashSum) {
		this.telecashSum = telecashSum;
	}
	public long getRevenueSum() {
		return revenueSum;
	}
	public void setRevenueSum(long revenueSum) {
		this.revenueSum = revenueSum;
	}
	public long getCashInSum() {
		return cashInSum;
	}
	public void setCashInSum(long cashInSum) {
		this.cashInSum = cashInSum;
	}
	public long getCashOutSum() {
		return cashOutSum;
	}
	public void setCashOutSum(long cashOutSum) {
		this.cashOutSum = cashOutSum;
	}
	public long getInvoicesPayedSum() {
		return invoicesPayedSum;
	}
	public void setInvoicesPayedSum(long invoicesPayedSum) {
		this.invoicesPayedSum = invoicesPayedSum;
	}
	public long getCouponInSum() {
		return couponInSum;
	}
	public void setCouponInSum(long couponInSum) {
		this.couponInSum = couponInSum;
	}
	public long getCouponOutSum() {
		return couponOutSum;
	}
	public void setCouponOutSum(long couponOutSum) {
		this.couponOutSum = couponOutSum;
	}
	public PosCashBalance[] getBalances() {
		return balances;
	}
	public void setBalances(PosCashBalance[] balances) {
		this.balances = balances;
	}
	
	public static class InvoicePay {
		private long amount;
		private String invNum;
		private LocalDate payDate;
		public long getAmount() {
			return amount;
		}
		public void setAmount(long amount) {
			this.amount = amount;
		}
		public String getInvNum() {
			return invNum;
		}
		public void setInvNum(String invNum) {
			this.invNum = invNum;
		}
		public LocalDate getPayDate() {
			return payDate;
		}
		public void setPayDate(LocalDate payDate) {
			this.payDate = payDate;
		}
	}

	public List<InvoicePay> getInvoicePays() {
		return invoicePays;
	}
	public void setInvoicePays(List<InvoicePay> invoicePays) {
		this.invoicePays = invoicePays;
	}
	public LocalDateTime getExecDate() {
		return execDate;
	}
	public void setExecDate(LocalDateTime execDate) {
		this.execDate = execDate;
	}
	public PosCashBalance getFirstBalance() {
		return firstBalance;
	}
	public void setFirstBalance(PosCashBalance firstBalance) {
		this.firstBalance = firstBalance;
	}
	public PosCashBalance getLastBalance() {
		return lastBalance;
	}
	public void setLastBalance(PosCashBalance lastBalance) {
		this.lastBalance = lastBalance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
