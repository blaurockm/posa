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
@Table( name = "posinvoice")
@XmlRootElement(name = "invoice")
public class PosInvoice {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	private Long amount;     // Rechnungsbetrag
	@JsonProperty
	private Long amountHalf;     // Rechnungsbetrag mit halben MwSt-Satz
	@JsonProperty
	private Long amountFull;     // Rechnungsbetrag mit vollem MwSt-Satz
	@JsonProperty
	private Long amountNone;     // Rechnungsbetrag ohne MwSt

	@JsonProperty
	private Long tax;     // Steuerbetrag
	@JsonProperty
	private Long taxHalf;     // Steuerbetrag halben MwSt-Satz
	@JsonProperty
	private Long taxFull;     // Steuerbetrag vollem MwSt-Satz

	@JsonProperty
	private Long netto;     // Nettobetrag
	@JsonProperty
	private Long nettoHalf;     // Nettobetrag halben MwSt-Satz
	@JsonProperty
	private Long nettoFull;     // Nettobetrag vollem MwSt-Satz

	@JsonProperty
	private int actionum;
	@JsonProperty
	private Boolean payed;
	@JsonProperty
	private Boolean cancelled;
	@JsonProperty
	private Timestamp creationTime;
	@JsonProperty
//	@DateTimeFormat(pattern="dd.MM.yyyy")
	private Date date;
	@JsonProperty
	private Timestamp printTime;

	@JsonProperty
	private boolean printed;
	
	@JsonProperty
	private boolean collective;
	@JsonProperty
	private boolean temporary;
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="invoice_id")
	private List<PosInvoiceDetail> details;

	@JsonProperty
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="invoice_id")
	private List<InvoiceAgrDetail> agreementDetails;

	@JsonProperty
	private java.sql.Date deliveryFrom;
	@JsonProperty
	private java.sql.Date deliveryTill;

	@JsonProperty
	private java.sql.Timestamp exportDate;

	@JsonProperty
	private boolean exported;
	
	@JsonProperty
	private double rebate;
	
	@JsonProperty
	private long rebateAmount;
	
	@JsonProperty
	private String vorText;
	
	@JsonProperty
	private String schlussText;
	
	@JsonProperty
	private String zahlText;
	

	@JsonIgnore
	public PosInvoiceDetail addInvoiceDetail(PosInvoiceDetail detail) {
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

	@JsonIgnore
	public InvoiceAgrDetail addAgreementDetail(InvoiceAgrDetail detail) {
		if (detail == null) {
			return null;
		}
		if (getAgreementDetails() == null) {
			setAgreementDetails(new ArrayList<InvoiceAgrDetail>());
		}
		getAgreementDetails().add(detail);
		if (deliveryFrom == null || deliveryFrom.after(detail.getDeliveryFrom())) {
			setDeliveryFrom(detail.getDeliveryFrom());
		}
		if (deliveryTill == null || deliveryTill.before(detail.getDeliveryTill())) {
			setDeliveryTill(detail.getDeliveryTill());
		}
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return "PosInvoice " + number + " of " + String.valueOf(date) + (amount != null ? (" amount " + String.valueOf(amount / 100)) : "n.bek."); 
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
	public Boolean getCancelled() {
		return cancelled;
	}
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	public List<PosInvoiceDetail> getDetails() {
		return details;
	}
	public void setDetails(List<PosInvoiceDetail> details) {
		this.details = details;
	}

	public Long getTax() {
		return tax;
	}

	public void setTax(Long tax) {
		this.tax = tax;
	}

	public Long getTaxHalf() {
		return taxHalf;
	}

	public void setTaxHalf(Long taxHalf) {
		this.taxHalf = taxHalf;
	}

	public Long getTaxFull() {
		return taxFull;
	}

	public void setTaxFull(Long taxFull) {
		this.taxFull = taxFull;
	}

	public Long getNetto() {
		return netto;
	}

	public void setNetto(Long netto) {
		this.netto = netto;
	}

	public Long getNettoHalf() {
		return nettoHalf;
	}

	public void setNettoHalf(Long nettoHalf) {
		this.nettoHalf = nettoHalf;
	}

	public Long getNettoFull() {
		return nettoFull;
	}

	public void setNettoFull(Long nettoFull) {
		this.nettoFull = nettoFull;
	}

	public List<InvoiceAgrDetail> getAgreementDetails() {
		return agreementDetails;
	}

	public void setAgreementDetails(List<InvoiceAgrDetail> agreementDetails) {
		this.agreementDetails = agreementDetails;
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

	public boolean isPrinted() {
		return printed;
	}

	public void setPrinted(boolean printed) {
		this.printed = printed;
	}

	public boolean isCollective() {
		return collective;
	}

	public void setCollective(boolean collective) {
		this.collective = collective;
	}

	public boolean isTemporary() {
		return temporary;
	}

	public void setTemporary(boolean temporary) {
		this.temporary = temporary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getExportDate() {
		return exportDate;
	}

	public void setExportDate(Timestamp exportDate) {
		this.exportDate = exportDate;
	}

	public boolean isExported() {
		return exported;
	}

	public void setExported(boolean exported) {
		this.exported = exported;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Timestamp getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Timestamp printTime) {
		this.printTime = printTime;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public Date getDate() {
		return date;
	}

	public double getRebate() {
		return rebate;
	}

	public void setRebate(double rebate) {
		this.rebate = rebate;
	}

	public long getRebateAmount() {
		return rebateAmount;
	}

	public void setRebateAmount(long rebateAmount) {
		this.rebateAmount = rebateAmount;
	}

	public String getVorText() {
		return vorText;
	}

	public void setVorText(String vorText) {
		this.vorText = vorText;
	}

	public String getSchlussText() {
		return schlussText;
	}

	public void setSchlussText(String schlussText) {
		this.schlussText = schlussText;
	}

	public String getZahlText() {
		return zahlText;
	}

	public void setZahlText(String zahlText) {
		this.zahlText = zahlText;
	}

}
