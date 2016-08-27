package net.buchlese.bofc.api.bofc;

import io.dropwizard.jackson.Jackson;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table( name = "posissueslip" )
@XmlRootElement(name = "issueslip")
public class PosIssueSlip {
	@Id
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;
	@JsonProperty
	private String number;
	@JsonProperty
	private int customerId;
	@JsonProperty
	private int debitorId;
	@JsonProperty
	private String name1;
	@JsonProperty
	private String name2;
	@JsonProperty
	private String name3;
	@JsonProperty
	private String street;
	@JsonProperty
	private String city;
	@JsonProperty
	private Long amount;     // Lieferscheinbetrag
	@JsonProperty
	private Long amountHalf;     // Lieferscheinbetrag mit halben MwSt-Satz
	@JsonProperty
	private Long amountFull;     // Lieferscheinbetrag mit vollem MwSt-Satz
	@JsonProperty
	private Long amountNone;     // Lieferscheinbetrag ohne MwSt

	@JsonProperty
	private int actionum;
	@JsonProperty
	private Boolean payed;
	@JsonProperty
	private Boolean cancelled;
	@JsonProperty
	private DateTime creationTime;
	@JsonProperty
	private LocalDate date;
	@JsonProperty
	private DateTime printTime;
	@JsonProperty
	private boolean printed;

	@JsonProperty
	private boolean includeOnInvoice;

	@JsonProperty
	private String type;
	
	@JsonProperty
	@OneToMany(mappedBy="issueslip",cascade=CascadeType.ALL)
	private List<PosInvoiceDetail> details;

	@JsonProperty
	private LocalDate deliveryFrom;
	@JsonProperty
	private LocalDate deliveryTill;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPointid() {
		return pointid;
	}
	public void setPointid(int pointid) {
		this.pointid = pointid;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getDebitorId() {
		return debitorId;
	}
	public void setDebitorId(int debitorId) {
		this.debitorId = debitorId;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Long getAmountHalf() {
		return amountHalf;
	}
	public void setAmountHalf(Long amountHalf) {
		this.amountHalf = amountHalf;
	}
	public Long getAmountFull() {
		return amountFull;
	}
	public void setAmountFull(Long amountFull) {
		this.amountFull = amountFull;
	}
	public Long getAmountNone() {
		return amountNone;
	}
	public void setAmountNone(Long amountNone) {
		this.amountNone = amountNone;
	}
	public DateTime getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public DateTime getPrintTime() {
		return printTime;
	}
	public void setPrintTime(DateTime printTime) {
		this.printTime = printTime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return "PosInvoice " + number + " of " + String.valueOf(date) + (amount != null ? (" amount " + String.valueOf(amount / 100)) : "n.bek."); 
	}
	public Boolean getCancelled() {
		return cancelled;
	}
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	public int getActionum() {
		return actionum;
	}
	public void setActionum(int actionum) {
		this.actionum = actionum;
	}
	public Boolean getPayed() {
		return payed;
	}
	public void setPayed(Boolean payed) {
		this.payed = payed;
	}
	public boolean isPrinted() {
		return printed;
	}
	public void setPrinted(boolean printed) {
		this.printed = printed;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<PosInvoiceDetail> getDetails() {
		return details;
	}
	public void setDetails(List<PosInvoiceDetail> details) {
		this.details = details;
	}
	public LocalDate getDeliveryFrom() {
		return deliveryFrom;
	}
	public void setDeliveryFrom(LocalDate deliveryFrom) {
		this.deliveryFrom = deliveryFrom;
	}
	public LocalDate getDeliveryTill() {
		return deliveryTill;
	}
	public void setDeliveryTill(LocalDate deliveryTill) {
		this.deliveryTill = deliveryTill;
	}
	public PosInvoiceDetail addDetail(PosInvoiceDetail detail) {
		if (detail == null) {
			return null;
		}
		if (getDetails() == null) {
			setDetails(new ArrayList<PosInvoiceDetail>());
		}
		getDetails().add(detail);
		setAmount(safeAdd(getAmount(),detail.getAmount()));
		setAmountFull(safeAdd(getAmountFull(), detail.getAmountFull()));
		setAmountHalf(safeAdd(getAmountHalf(), detail.getAmountHalf()));
		return detail;
	}

	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
	}

	@JsonIgnore
	private Long safeAdd(Long a, Long b) {
		if (a == null && b == null) {
			return null;
		}
		if (a != null && b == null) {
			return a;
		}
		if (a == null && b != null) {
			return b;
		}
		return Long.valueOf(a + b);
	}
	public boolean isIncludeOnInvoice() {
		return includeOnInvoice;
	}
	public void setIncludeOnInvoice(boolean includeOnInvoice) {
		this.includeOnInvoice = includeOnInvoice;
	}

}
