package net.buchlese.bofc.api.bofc;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "posissueslip" )
@XmlRootElement(name = "deliverynote")
public class PosIssueSlip {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;
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
	private java.sql.Timestamp creationTime;
	@JsonProperty
	private java.sql.Date date;
	@JsonProperty
	private java.sql.Timestamp printTime;
	@JsonProperty
	private boolean printed;

	@JsonProperty
	private boolean includeOnInvoice;

	@JsonProperty
	private String type;
	
	@JsonProperty
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="issueslip_id")
	private List<PosInvoiceDetail> details;

	@JsonProperty
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="issueslip_id")
	private List<PosIssueSlipDetail> deliveryDetails;

	@JsonProperty
	private Date deliveryFrom;
	@JsonProperty
	private Date deliveryTill;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Timestamp getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}
	
	public Timestamp getPrintTime() {
		return printTime;
	}
	public void setPrintTime(Timestamp printTime) {
		this.printTime = printTime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return "PosIssueSlip " + number + " of " + String.valueOf(date) + (amount != null ? (" amount " + String.valueOf(amount / 100)) : "n.bek."); 
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
	public List<PosIssueSlipDetail> getDeliveryDetails() {
		return deliveryDetails;
	}
	public void setDeliveryDetails(List<PosIssueSlipDetail> details) {
		this.deliveryDetails = details;
	}
	public Date getDeliveryFrom() {
		return deliveryFrom;
	}
	public void setDeliveryFrom(Date deliveryFrom) {
		this.deliveryFrom = deliveryFrom;
	}
	public Date getDeliveryTill() {
		return deliveryTill;
	}
	public void setDeliveryTill(Date deliveryTill) {
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

	public PosIssueSlipDetail addDetail(PosIssueSlipDetail detail) {
		if (detail == null) {
			return null;
		}
		if (getDeliveryDetails() == null) {
			setDeliveryDetails(new ArrayList<PosIssueSlipDetail>());
		}
		getDeliveryDetails().add(detail);
		return detail;
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
	public java.sql.Date getDate() {
		return date;
	}
	public void setDate(java.sql.Date date) {
		this.date = date;
	}

}
