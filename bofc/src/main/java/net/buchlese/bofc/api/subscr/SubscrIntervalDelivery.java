package net.buchlese.bofc.api.subscr;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;


@Entity
@Table( name = "subscrintervaldelivery" )
public class SubscrIntervalDelivery implements Comparable<SubscrIntervalDelivery> {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

	@JsonProperty
	private java.sql.Date deliveryDate;
	@JsonProperty
	private int quantity;
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
	private java.sql.Timestamp creationDate;
	@JsonProperty
	private long shipmentCost;

	@JsonIgnore
	private String subscriberName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "settdetail_id",
	foreignKey = @ForeignKey(name = "SETTDETAIL2_ID_FK")
)
	private InvoiceAgrDetail settDetail;

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

	@PostLoad
	public void initDelivery() {
		if (getSubscriberName() == null) {
			setSubscriberName(getSubscriber().getName());
		}
	}
	
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
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


	@XmlTransient
	public InvoiceAgrDetail getSettDetail() {
		return settDetail;
	}

	public void setSettDetail(InvoiceAgrDetail settDetail) {
		if (settDetail != null) {
			settDetail.addIntervalDelivery(this);
		} else {
			if (this.settDetail != null) {
				this.settDetail.removeIntervalDelivery(this);
			}
		}
		this.settDetail = settDetail;
		this.payed = settDetail != null;
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
		SubscrIntervalDelivery other = (SubscrIntervalDelivery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public java.sql.Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(java.sql.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}
	
}
