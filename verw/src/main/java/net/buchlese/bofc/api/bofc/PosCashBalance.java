package net.buchlese.bofc.api.bofc;


import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.verw.util.JPAMapper;

@Entity
@Table( name = "poscashbalance")
@XmlRootElement(name = "CashBalance")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosCashBalance {
	@Id
	@JsonProperty
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@JsonProperty
	private int pointid;
	@NotEmpty
	@JsonProperty
	private String abschlussId;

	@JsonProperty
	private transient Map<Tax, Long> taxBalance = new EnumMap<Tax, Long>(Tax.class);
	@JsonProperty
	private transient Map<PaymentMethod, Long> paymentMethodBalance = new EnumMap<PaymentMethod, Long>(PaymentMethod.class);
	@JsonProperty
	private transient Map<String, Long> articleGroupBalance;
	@JsonProperty
	private transient Map<String, Long> newCoupon;  // neue Gutscheine (nr + betrag)
	@JsonProperty
	private transient Map<String, Long> oldCoupon;  // eingel. Gutscheine (nr + betrag)
	@JsonProperty
	private transient Map<String, Long> payedInvoices;  // bezahlte rechnungen ( nr + betrag)
	@JsonProperty
	private transient Map<String, Long> createdInvoices;  // erstellte rechnungen ( nr + betrag)
	@JsonProperty
	private transient Map<String, Long> cashIn;  // Einzahlungen
	@JsonProperty
	private transient Map<String, Long> cashOut;  // Auszahlungen
	@JsonProperty
	private transient List<PosTicket> tickets;

	@JsonProperty
	private Long goodsOut;  // Warenverkäufe
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
	private Long absorption;  // Abschöpfung
	@JsonProperty
	private Long cashStart;  // Kassen Anfangsbestand
	@JsonProperty
	private Long cashEnd;  // Kassen Endbestand
	@JsonProperty
	private Long cashDiff;  // Kassen Zähldifferenz
	@JsonProperty
	private Long cash;  // Kassen Zählbestand
	@JsonProperty
	private Long revenue;     // Umsatz
	@JsonProperty
	private Long profit;     // Profit
	@JsonProperty
	private int ticketCount;
	@JsonProperty
	private int cancelledticketCount;
	@JsonProperty
	private java.sql.Timestamp firstTimestamp;
	@JsonProperty
	private long firstBelegNr;
	@JsonProperty
	private long lastBelegNr; 
	@JsonProperty
	private java.sql.Timestamp lastTimestamp;
	@JsonProperty
	private java.sql.Timestamp creationtime;
	@JsonProperty
	private boolean exported;
	@JsonProperty
	private java.sql.Timestamp exportDate;
	@JsonProperty
	private java.sql.Timestamp firstCovered;
	@JsonProperty
	private java.sql.Timestamp lastCovered;

	@JsonProperty
	@Column
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String origAbschluss;
	
	@JsonIgnore
	@Lob
	private String balanceSheet;
	
	@JsonIgnore
	public String getBalanceSheet() {
		return balanceSheet;
	}

	@JsonIgnore
	@PostLoad
	public void computeMapValues() {
		if (balanceSheet != null) {
			PosCashBalance bal = JPAMapper.readValue(balanceSheet, this.getClass());
			this.articleGroupBalance = bal.articleGroupBalance;
			this.cashIn = bal.cashIn;
			this.cashOut = bal.cashOut;
			this.createdInvoices = bal.createdInvoices;
			this.tickets = bal.tickets;
			this.paymentMethodBalance = bal.paymentMethodBalance;
			this.payedInvoices = bal.payedInvoices;
			this.taxBalance = bal.taxBalance;
			this.oldCoupon = bal.oldCoupon;
			this.newCoupon = bal.newCoupon;
		}
	}
	
	public void setBalanceSheet(String bals)  {
		this.balanceSheet = bals;
	}
	
	public void export(AccountingBalanceExport x) {
		if (exported || x == null) {
			return;
		}
		if (x.getExecDate() != null) {
			setExportDate(x.getExecDate());
		}
		setExported(true);
	}
	
	public void unexport() {
		if (!exported) {
			return;
		}
		setExportDate(null);
		setExported(false);
	}
	
	public void setChecked() {}
	
	public void getChecked() {}
	
	public String toString() {
		return "PosCashBalance of " + abschlussId + ", rev " + revenue / 100; 
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAbschlussId() {
		return abschlussId;
	}
	public void setAbschlussId(String abschlussId) {
		this.abschlussId = abschlussId;
	}
	public Long getGoodsOut() {
		return goodsOut;
	}
	public void setGoodsOut(Long goodsOut) {
		this.goodsOut = goodsOut;
	}
	public Map<Tax, Long> getTaxBalance() {
		return taxBalance;
	}
	public void setTaxBalance(Map<Tax, Long> taxBalance) {
		this.taxBalance = taxBalance;
	}
	public Map<String, Long> getArticleGroupBalance() {
		return articleGroupBalance;
	}
	public void setArticleGroupBalance(Map<String, Long> articleGroupBalance) {
		this.articleGroupBalance = articleGroupBalance;
	}
	public Map<PaymentMethod, Long> getPaymentMethodBalance() {
		return paymentMethodBalance;
	}
	public Long getProfit() {
		return profit;
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

	public void setProfit(Long profit) {
		this.profit = profit;
	}
	public void setPaymentMethodBalance(Map<PaymentMethod, Long> paymentMethodBalance) {
		this.paymentMethodBalance = paymentMethodBalance;
	}
	public Map<String, Long> getNewCoupon() {
		return newCoupon;
	}
	public void setNewCoupon(Map<String, Long> newCoupon) {
		this.newCoupon = newCoupon;
	}
	public Map<String, Long> getPayedInvoices() {
		return payedInvoices;
	}
	public void setPayedInvoices(Map<String, Long> payedInvoices) {
		this.payedInvoices = payedInvoices;
	}
	public Map<String, Long> getCreatedInvoices() {
		return createdInvoices;
	}

	public void setCreatedInvoices(Map<String, Long> createdInvoices) {
		this.createdInvoices = createdInvoices;
	}

	public Map<String, Long> getCashIn() {
		return cashIn;
	}

	public void setCashIn(Map<String, Long> cashIn) {
		this.cashIn = cashIn;
	}

	public Map<String, Long> getCashOut() {
		return cashOut;
	}

	public void setCashOut(Map<String, Long> cashOut) {
		this.cashOut = cashOut;
	}

	public Long getCashStart() {
		return cashStart;
	}

	public void setCashStart(Long cashStart) {
		this.cashStart = cashStart;
	}

	public Long getCashEnd() {
		return cashEnd;
	}

	public void setCashEnd(Long cashEnd) {
		this.cashEnd = cashEnd;
	}

	public Long getAbsorption() {
		return absorption;
	}
	public void setAbsorption(Long absorption) {
		this.absorption = absorption;
	}
	public Long getRevenue() {
		return revenue;
	}
	public void setRevenue(Long revenue) {
		this.revenue = revenue;
	}
	public int getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}
	public Map<String, Long> getOldCoupon() {
		return oldCoupon;
	}
	public void setOldCoupon(Map<String, Long> oldCoupon) {
		this.oldCoupon = oldCoupon;
	}
	public int getCancelledticketCount() {
		return cancelledticketCount;
	}
	public void setCancelledticketCount(int cancelledticketCount) {
		this.cancelledticketCount = cancelledticketCount;
	}
	public java.sql.Timestamp getFirstTimestamp() {
		return firstTimestamp;
	}
	public void setFirstTimestamp(java.sql.Timestamp firstTimestamp) {
		this.firstTimestamp = firstTimestamp;
	}
	public long getFirstBelegNr() {
		return firstBelegNr;
	}
	public void setFirstBelegNr(long firstBelegNr) {
		this.firstBelegNr = firstBelegNr;
	}
	public long getLastBelegNr() {
		return lastBelegNr;
	}
	public void setLastBelegNr(long lastBelegNr) {
		this.lastBelegNr = lastBelegNr;
	}
	public java.sql.Timestamp getLastTimestamp() {
		return lastTimestamp;
	}
	public void setLastTimestamp(java.sql.Timestamp lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}
	public java.sql.Timestamp getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(java.sql.Timestamp creationtime) {
		this.creationtime = creationtime;
	}
	public String getOrigAbschluss() {
		return origAbschluss;
	}
	public void setOrigAbschluss(String origAbschluss) {
		this.origAbschluss = origAbschluss;
	}
	public boolean isExported() {
		return exported;
	}
	public void setExported(boolean exported) {
		this.exported = exported;
	}
	public java.sql.Timestamp getExportDate() {
		return exportDate;
	}
	public java.sql.Timestamp getFirstCovered() {
		return firstCovered;
	}
	public void setFirstCovered(java.sql.Timestamp firstCovered) {
		this.firstCovered = firstCovered;
	}
	public java.sql.Timestamp getLastCovered() {
		return lastCovered;
	}
	public void setLastCovered(java.sql.Timestamp lastCovered) {
		this.lastCovered = lastCovered;
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

	public void setExportDate(java.sql.Timestamp exportDate) {
		this.exportDate = exportDate;
	}

	public List<PosTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<PosTicket> tickets) {
		this.tickets = tickets;
	}

	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public Long getCashDiff() {
		return cashDiff;
	}

	public void setCashDiff(Long cashDiff) {
		this.cashDiff = cashDiff;
	}

	public Long getCash() {
		return cash;
	}

	public void setCash(Long cash) {
		this.cash = cash;
	}
	
}
