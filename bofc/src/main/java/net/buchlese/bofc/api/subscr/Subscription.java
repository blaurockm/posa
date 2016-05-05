package net.buchlese.bofc.api.subscr;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscription {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private int subscriberId;
	@JsonProperty
	private int productId;
	@JsonProperty
	private int quantity;
//	@JsonProperty
//	private List<SubscrDelivery> deliveries;
	@JsonProperty
	private boolean needsInvoice;
	@JsonProperty
	private boolean needsDeliveryNote;
	@JsonProperty
	private boolean collectiveInvoice;
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
	public int getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public boolean isNeedsInvoice() {
		return needsInvoice;
	}
	public void setNeedsInvoice(boolean needsInvoice) {
		this.needsInvoice = needsInvoice;
	}
	public boolean isNeedsDeliveryNote() {
		return needsDeliveryNote;
	}
	public void setNeedsDeliveryNote(boolean needsDeliveryNote) {
		this.needsDeliveryNote = needsDeliveryNote;
	}
	public boolean isCollectiveInvoice() {
		return collectiveInvoice;
	}
	public void setCollectiveInvoice(boolean collectiveInvoice) {
		this.collectiveInvoice = collectiveInvoice;
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
	
	
}
