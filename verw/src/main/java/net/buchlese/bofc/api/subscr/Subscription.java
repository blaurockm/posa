package net.buchlese.bofc.api.subscr;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
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
	private Long id;
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
	private Date startDate;
	@JsonProperty
	private Date endDate;
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

	@ManyToOne
	@JoinColumn(name = "subscriber_id")
	private Subscriber subscriber;
	
	public Subscriber getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(Subscriber s) {
		if (s != null) {
			s.addSubscription(this);
		} else {
			if (this.subscriber != null) {
			 this.subscriber.removeSubscription(this);
			}
		}
		this.subscriber = s;
	}

	@ManyToOne
	@JoinColumn(name = "product_id",
	foreignKey = @ForeignKey(name = "PRODUCT_ID_FK23")
)	private SubscrProduct product;

	
	@JsonIgnore
	@OneToMany(mappedBy="subscription")
	private Set<SubscrDelivery> articleDeliveries;

	@JsonIgnore
	@OneToMany(mappedBy="subscription")
	private Set<SubscrIntervalDelivery> intervalDeliveries;

	@JsonProperty
	@Enumerated(EnumType.STRING)
	private PayIntervalType paymentType;

	@JsonProperty
	private Date payedUntil;

	@JsonProperty
	private Date lastInvoiceDate;
	
	@JsonProperty
	private boolean needsAttention;
	
	@JsonProperty
	private String memo;

	@PostLoad
	public void initDelivery() {
		if (getSubscriber()!= null) {
			setSubscriberId(getSubscriber().getId());
		}
		setProductId(product.getId());
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Date getPayedUntil() {
		return payedUntil;
	}
	public void setPayedUntil(Date payedUntil) {
		this.payedUntil = payedUntil;
	}
	public Date getLastInvoiceDate() {
		return lastInvoiceDate;
	}
	public void setLastInvoiceDate(Date lastInvoiceDate) {
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
	public Set<SubscrDelivery> getArticleDeliveries() {
		return articleDeliveries;
	}
	public void setArticleDeliveries(Set<SubscrDelivery> articleDeliveries) {
		this.articleDeliveries = articleDeliveries;
	}
	public Set<SubscrIntervalDelivery> getIntervalDeliveries() {
		return intervalDeliveries;
	}
	public void setIntervalDeliveries(Set<SubscrIntervalDelivery> intervalDeliveries) {
		this.intervalDeliveries = intervalDeliveries;
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
		Subscription other = (Subscription) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
