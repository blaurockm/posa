package net.buchlese.posa.api.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import net.buchlese.posa.core.Validator;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement(name = "CashBalance")
public class PosCashBalance {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;
	@NotEmpty
	@JsonProperty
	private String abschlussId;
	@JsonProperty
	private Map<Tax, Long> taxBalance = new EnumMap<Tax, Long>(Tax.class);
	@JsonProperty
	private Map<String, Long> articleGroupBalance;
	@JsonProperty
	private Long goodsOut;  // Warenverkäufe
	@JsonProperty
	private Map<PaymentMethod, Long> paymentMethodBalance = new EnumMap<PaymentMethod, Long>(PaymentMethod.class);
	@JsonProperty
	private Map<String, Long> newCoupon;  // neue Gutscheine (nr + betrag)
	@JsonProperty
	private Map<String, Long> oldCoupon;  // eingel. Gutscheine (nr + betrag)
	@JsonProperty
	private Long couponTradeIn;  // Gutscheine angenommen
	@JsonProperty
	private Long couponTradeOut;  // Gutscheine verkauft
	@JsonProperty
	private Map<String, Long> payedInvoices;  // bezahlte rechnungen ( nr + betrag)
	@JsonProperty
	private Map<String, Long> createdInvoices;  // erstellte rechnungen ( nr + betrag)
	@JsonProperty
	private Long payedInvoicesSum;  // Rechnungen bezahlt
	@JsonProperty
	private Long createdInvoicesSum;  // Rechnungen ausgestellt
	@JsonProperty
	private Map<String, Long> cashIn;  // Einzahlungen
	@JsonProperty
	private Map<String, Long> cashOut;  // Auszahlungen
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
	private Long revenue;     // Umsatz
	@JsonProperty
	private Long profit;     // Profit
	@JsonProperty
	private int ticketCount;
	@JsonProperty
	private int cancelledticketCount;
	@JsonProperty
	private DateTime firstTimestamp;
	@JsonProperty
	private long firstBelegNr;
	@JsonProperty
	private long lastBelegNr; 
	@JsonProperty
	private DateTime lastTimestamp;
	@JsonProperty
	private DateTime creationtime;
	@JsonProperty
	private String origAbschluss;
	@JsonProperty
	private boolean exported;
	@JsonProperty
	private DateTime exportDate;
	@JsonProperty
	private DateTime firstCovered;
	@JsonProperty
	private DateTime lastCovered;
	@JsonProperty
	private List<PosTicket> tickets;
	
	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getBalanceSheet() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
	}

	// Die Daten des orginalen KassenAbschlusses als Map bereitstellen
	@JsonIgnore
	public Map<?, ?> getPosData() throws IOException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.readValue(origAbschluss, Map.class);
	}

	@JsonIgnore
	private boolean checked;
	
	@JsonProperty("checked")
	public boolean getChecked() {
		return Validator.validBalance(this);
	}
	
	@JsonIgnore
	public void setChecked() {
		
	}
	
	public String toString() {
		return "PosCashBalance of " + String.valueOf(abschlussId) + (revenue != null ? (", rev " + String.valueOf(revenue / 100)) : "n.bek."); 
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public DateTime getFirstTimestamp() {
		return firstTimestamp;
	}
	public void setFirstTimestamp(DateTime firstTimestamp) {
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
	public DateTime getLastTimestamp() {
		return lastTimestamp;
	}
	public void setLastTimestamp(DateTime lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}
	public DateTime getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(DateTime creationtime) {
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
	public DateTime getExportDate() {
		return exportDate;
	}
	public DateTime getFirstCovered() {
		return firstCovered;
	}
	public void setFirstCovered(DateTime firstCovered) {
		this.firstCovered = firstCovered;
	}
	public DateTime getLastCovered() {
		return lastCovered;
	}
	public void setLastCovered(DateTime lastCovered) {
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

	public void setExportDate(DateTime exportDate) {
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
	
}
