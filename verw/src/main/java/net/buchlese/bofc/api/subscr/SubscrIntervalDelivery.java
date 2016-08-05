package net.buchlese.bofc.api.subscr;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "subscrIntervalDelivery" )
public class SubscrIntervalDelivery implements Comparable<SubscrIntervalDelivery> {
	@Id
	@NotNull
	@JsonProperty
	private long id;

	@JsonProperty
	private LocalDate deliveryDate;
	@JsonProperty
	private int quantity;
	@JsonProperty
	@Transient
	private long subscriptionId;
	@JsonProperty
	@Transient
	private long subscriberId;
	@JsonProperty
	@Transient
	private long intervalId;
	@JsonProperty
	private String intervalName;
	@JsonProperty
	private long total;
	@JsonProperty
	private long totalFull;
	@JsonProperty
	private long totalHalf;
	@JsonProperty
	private boolean payed;
	@JsonProperty
	private String invoiceNumber;
	@JsonProperty
	private LocalDateTime creationDate;
	@JsonProperty
	private long shipmentCost;

	@JsonIgnore
	public String subscriberName;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "interval_id",
	foreignKey = @ForeignKey(name = "INTERVAL_ID_FK")
)
	private SubscrInterval interval;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "subscriber_id",
	foreignKey = @ForeignKey(name = "CUSTOMER_ID_FK2")
)
	private Subscriber subscriber;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "subscription_id",
    			foreignKey = @ForeignKey(name = "SUBSCRIPTION_ID_FK2")
    )
	private Subscription subscription;

	@JsonIgnore
	public String getSubscriberName() {
		return subscriberName;
	}
	

	@JsonIgnore
	public void updateBrutto(long br, double halfPercentage) {
		total =  br;
		totalHalf = (long) (total * halfPercentage);
		totalFull = total - totalHalf;
	}

	@JsonIgnore
	public void updateBruttoHalf(long br) {
		totalHalf =  br;
		totalFull = total - totalHalf;
	}

	@JsonIgnore
	public void updateBruttoFull(long br) {
		totalFull =  br;
		totalHalf = total - totalFull;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public boolean isPayed() {
		return payed;
	}
	public void setPayed(boolean payed) {
		this.payed = payed;
	}
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	public void setSubscriptionId(long subcriptionId) {
		this.subscriptionId = subcriptionId;
	}
	public long getSubscriberId() {
		return subscriberId;
	}
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriberId(long subscriberId) {
		this.subscriberId = subscriberId;
	}
	public long getTotalFull() {
		return totalFull;
	}
	public void setTotalFull(long totalFull) {
		this.totalFull = totalFull;
	}
	public long getTotalHalf() {
		return totalHalf;
	}
	public void setTotalHalf(long totalHalf) {
		this.totalHalf = totalHalf;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	@Override
	public int compareTo(SubscrIntervalDelivery o) {
		return getDeliveryDate().compareTo(o.getDeliveryDate());
	}

	public long getShipmentCost() {
		return shipmentCost;
	}

	public void setShipmentCost(long shipmentCost) {
		this.shipmentCost = shipmentCost;
	}

	public long getIntervalId() {
		return intervalId;
	}

	public void setIntervalId(long intervalId) {
		this.intervalId = intervalId;
	}

	public String getIntervalName() {
		return intervalName;
	}

	public void setIntervalName(String intervalName) {
		this.intervalName = intervalName;
	}

	public SubscrInterval getInterval() {
		return interval;
	}

	public void setInterval(SubscrInterval interval) {
		this.interval = interval;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	
}
