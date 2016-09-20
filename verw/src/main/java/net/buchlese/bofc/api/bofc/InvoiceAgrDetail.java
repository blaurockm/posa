package net.buchlese.bofc.api.bofc;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;

@Entity
@Table(name="posinvoice_agrdetail")
public class InvoiceAgrDetail {
	public enum TYPE { SUBSCR, ISSUESLIP };	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

	@JsonProperty
	private long agreementId;

	@JsonProperty
	private LocalDate deliveryFrom;

	@JsonProperty
	private LocalDate deliveryTill;

	@JsonIgnore
	@OneToMany(mappedBy="settDetail")
	private Set<SubscrDelivery> deliveries;

	@JsonIgnore
	@OneToMany(mappedBy="settDetail")
	private Set<SubscrIntervalDelivery> intervalDeliveries;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private TYPE type = TYPE.SUBSCR;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PayIntervalType payType;

	public long getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(long agreementId) {
		this.agreementId = agreementId;
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

	public void setDeliveries(Set<SubscrDelivery> deliveries) {
		this.deliveries = deliveries;
	}

	public Set<SubscrIntervalDelivery> getIntervalDeliveries() {
		return intervalDeliveries;
	}

	public void setIntervalDeliveries(Set<SubscrIntervalDelivery> intervalDeliveries) {
		this.intervalDeliveries = intervalDeliveries;
	}
	
}
