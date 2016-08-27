package net.buchlese.bofc.api.bofc;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.bofc.api.subscr.PayIntervalType;

@Embeddable
public class InvoiceAgrDetail {
	public enum TYPE { SUBSCR, ISSUESLIP };	
	@Id
	@JsonProperty
	private Long id;

	@JsonProperty
	private long agreementId;

	@JsonProperty
	private LocalDate deliveryFrom;

	@JsonProperty
	private LocalDate deliveryTill;

	@JsonProperty
	@Transient
	private List<Long> deliveryIds;

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

	public List<Long> getDeliveryIds() {
		return deliveryIds;
	}

	public void setDeliveryIds(List<Long> deliveryIds) {
		this.deliveryIds = deliveryIds;
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
	
}
