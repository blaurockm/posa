package net.buchlese.bofc.api.bofc;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceAgrDetail {
	@JsonProperty
	private long agreementId;

	@JsonProperty
	private LocalDate deliveryFrom;

	@JsonProperty
	private LocalDate deliveryTill;

	@JsonProperty
	private List<Long> deliveryIds;

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

	
}