package net.buchlese.bofc.api.bofc;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscription;


@Entity
@Table(name="posinvoice_agrdetail")
public class InvoiceAgrDetail {
	public enum TYPE { SUBSCR, ISSUESLIP };	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

	@ManyToOne
	@JoinColumn(name = "settled_agreement_id")
	private Subscription settledAgreement;
	
	@ManyToOne 
	@JoinColumn(name = "settled_delivery_note_id")
	private PosIssueSlip settledDeliveryNote;
	
	@JsonProperty
	private java.sql.Date deliveryFrom;

	@JsonProperty
	private java.sql.Date deliveryTill;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "settdetail_id")
	private Set<SubscrDelivery> deliveries;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL )
	@JoinColumn(name = "settdetail_id")
	private Set<SubscrIntervalDelivery> intervalDeliveries;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private TYPE type = TYPE.SUBSCR;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PayIntervalType payType;


	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public PayIntervalType getPayType() {
		return payType;
	}

	public void setPayType(PayIntervalType payType) {
		this.payType = payType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceAgrDetail other = (InvoiceAgrDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Set<SubscrDelivery> getDeliveries() {
		return deliveries;
	}

	public void addSubscrDelivery(SubscrDelivery d) {
		if (deliveries == null) {
			deliveries = new HashSet<>();
		}
		deliveries.add(d);
	}

	public void removeSubscrDelivery(SubscrDelivery d) {
		if (deliveries == null) {
			deliveries = new HashSet<>();
		}
		deliveries.remove(d);
	}

	public void setDeliveries(Set<SubscrDelivery> deliveries) {
		this.deliveries = deliveries;
	}

	public Set<SubscrIntervalDelivery> getIntervalDeliveries() {
		return intervalDeliveries;
	}

	public void setIntervalDeliveries(Set<SubscrIntervalDelivery> intervalDeliveries) {
		this.intervalDeliveries = intervalDeliveries;
	}

	public void addIntervalDelivery(SubscrIntervalDelivery d) {
		if (intervalDeliveries == null) {
			intervalDeliveries = new HashSet<>();
		}
		intervalDeliveries.add(d);
	}

	public void removeIntervalDelivery(SubscrIntervalDelivery d) {
		if (intervalDeliveries == null) {
			intervalDeliveries = new HashSet<>();
		}
		intervalDeliveries.remove(d);
	}

	public Subscription getSettledAgreement() {
		return settledAgreement;
	}

	public void setSettledAgreement(Subscription settledAgreement) {
		this.settledAgreement = settledAgreement;
	}

	public PosIssueSlip getSettledDeliveryNote() {
		return settledDeliveryNote;
	}

	public void setSettledDeliveryNote(PosIssueSlip settledDeliveryNote) {
		this.settledDeliveryNote = settledDeliveryNote;
	}

	public java.sql.Date getDeliveryFrom() {
		return deliveryFrom;
	}

	public void setDeliveryFrom(java.sql.Date deliveryFrom) {
		this.deliveryFrom = deliveryFrom;
	}

	public java.sql.Date getDeliveryTill() {
		return deliveryTill;
	}

	public void setDeliveryTill(java.sql.Date deliveryTill) {
		this.deliveryTill = deliveryTill;
	}
	
}
