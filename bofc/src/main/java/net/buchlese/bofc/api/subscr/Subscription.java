package net.buchlese.bofc.api.subscr;

import io.dropwizard.jackson.Jackson;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Subscription {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private long subscriberId;
	@JsonProperty
	private long productId;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private LocalDate startDate;
	@JsonProperty
	private LocalDate endDate;
	@JsonProperty
	private Address deliveryAddress;
	@JsonProperty
	private String deliveryInfo1;
	@JsonProperty
	private String deliveryInfo2;
	
	@JsonProperty
	private ShipType shipmentType;


	@JsonProperty
	private PayIntervalType paymentType;

	@JsonProperty
	private LocalDate payedUntil;

	@JsonProperty
	private LocalDate lastInvoiceDate;
	
	@JsonProperty
	private boolean needsAttention;
	
	@JsonProperty
	private String memo;

	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
	}

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
	public long getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(long subscriberId) {
		this.subscriberId = subscriberId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getDeliveryInfo1() {
		return deliveryInfo1;
	}
	public void setDeliveryInfo1(String deliveryInfo1) {
		this.deliveryInfo1 = deliveryInfo1;
	}
	public String getDeliveryInfo2() {
		return deliveryInfo2;
	}
	public void setDeliveryInfo2(String deliveryInfo2) {
		this.deliveryInfo2 = deliveryInfo2;
	}
	public ShipType getShipmentType() {
		return shipmentType;
	}
	public void setShipmentType(ShipType shipmentType) {
		this.shipmentType = shipmentType;
	}
	public PayIntervalType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PayIntervalType paymentType) {
		this.paymentType = paymentType;
	}
	public LocalDate getPayedUntil() {
		return payedUntil;
	}
	public void setPayedUntil(LocalDate payedUntil) {
		this.payedUntil = payedUntil;
	}
	public LocalDate getLastInvoiceDate() {
		return lastInvoiceDate;
	}
	public void setLastInvoiceDate(LocalDate lastInvoiceDate) {
		this.lastInvoiceDate = lastInvoiceDate;
	}

	public boolean isNeedsAttention() {
		return needsAttention;
	}

	public void setNeedsAttention(boolean needsAttention) {
		this.needsAttention = needsAttention;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
