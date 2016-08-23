package net.buchlese.bofc.api.subscr;

import java.time.LocalDate;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "subscription" )
public class Subscription {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	@Transient
	private long subscriberId;
	@JsonProperty
	@Transient
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

	@JsonIgnore
	@ManyToOne
	private Subscriber subscriber;
	public Subscriber getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(Subscriber s) {
		this.subscriber = s;
	}

	@JsonIgnore
	@ManyToOne
	private SubscrProduct product;

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
	public SubscrProduct getProduct() {
		return product;
	}
	public void setProduct(SubscrProduct subscrProduct) {
		this.product = subscrProduct;
	}
	
}
