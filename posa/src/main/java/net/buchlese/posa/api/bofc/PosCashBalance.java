package net.buchlese.posa.api.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.util.EnumMap;
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
	@NotEmpty
	@JsonProperty
	private String abschlussId;
	@JsonProperty
	private Map<Tax, Long> taxBalance = new EnumMap<Tax, Long>(Tax.class);
	@JsonProperty
	private Map<String, Long> articleGroupBalance;
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
	private Long goodsOut;  // Warenverkäufe
	@JsonProperty
	private Long cashIn;  // Einzahlungen
	@JsonProperty
	private Long cashOut;  // Auszahlungen
	@JsonProperty
	private Long absorption;  // Abschöpfung
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

	public boolean getChecked() {
		return Validator.validBalance(this);
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
	public Long getCashIn() {
		return cashIn;
	}
	public void setCashIn(Long cashIn) {
		this.cashIn = cashIn;
	}
	public Long getCashOut() {
		return cashOut;
	}
	public void setCashOut(Long cashOut) {
		this.cashOut = cashOut;
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
	
}
