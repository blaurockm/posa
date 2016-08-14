package net.buchlese.bofc.api.bofc;

import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import net.buchlese.bofc.api.subscr.PayIntervalType;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class InvoiceAgrDetail {
	public enum TYPE { SUBSCR, ISSUESLIP };	
	
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
	
}
