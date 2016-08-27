package net.buchlese.bofc.api.subscr;

import io.dropwizard.jackson.Jackson;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table( name = "subscription" )
public class Subscription {
	@Id
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
	@Embedded
	@JsonProperty
	private Address deliveryAddress;
	@JsonProperty
	private String deliveryInfo1;
	@JsonProperty
	private String deliveryInfo2;
	
	@JsonProperty
	@Enumerated(EnumType.STRING)
	private ShipType shipmentType;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PayIntervalType paymentType;

	@JsonProperty
	private LocalDate payedUntil;

	@JsonProperty
	private LocalDate lastInvoiceDate;
	
	@JsonProperty
	private boolean needsAttention;
	
	@JsonProperty
	private String memo;

	// in verw we need this, not for transfer
//	@ManyToOne
//	@JoinColumn(name = "subscriber_id")
//	private Subscriber subscriber;
//
//	@ManyToOne
//	@JoinColumn(name = "product_id",
//	foreignKey = @ForeignKey(name = "PRODUCT_ID_FK23")
//)	private SubscrProduct product;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Subscription other = (Subscription) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
