package net.buchlese.bofc.api.subscr;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "subscrdelivery" )
public class SubscrDelivery implements Comparable<SubscrDelivery> {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@JsonProperty
	private long id;

//	@DateTimeFormat(pattern="dd.MM.yyyy")
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
	private long articleId;
	@JsonProperty
	private String articleName;
	@JsonProperty
	private long total;
	@JsonProperty
	private long totalFull;
	@JsonProperty
	private long totalHalf;
	@JsonProperty
	private boolean payed;
	@JsonProperty
	private boolean slipped;
	@JsonProperty
	private String invoiceNumber;
	@JsonProperty
	private String slipNumber;
	@JsonProperty
	private LocalDateTime creationDate;
	@JsonProperty
	private long shipmentCost;

	@JsonIgnore
	public String subscriberName;

	@ManyToOne
	@JoinColumn(name = "article_id",
	foreignKey = @ForeignKey(name = "ARTICLE_ID_FK")
)
	private SubscrArticle article;

	@ManyToOne
	@JoinColumn(name = "subscriber_id",
	foreignKey = @ForeignKey(name = "CUSTOMER_ID_FK")
)
	private Subscriber subscriber;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "subscription_id",
    			foreignKey = @ForeignKey(name = "SUBSCRIPTION_ID_FK")
    )
	private Subscription subscription;

	@JsonIgnore
	public String getSubscriberName() {
		return subscriberName;
	}
	
	@PostLoad
	public void initDelivery() {
		setSubscriberId(subscriber.getId());
		setSubscriptionId(subscription.getId());
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
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String article) {
		this.articleName = article;
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
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
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
	public int compareTo(SubscrDelivery o) {
		return getDeliveryDate().compareTo(o.getDeliveryDate());
	}

	public long getShipmentCost() {
		return shipmentCost;
	}

	public void setShipmentCost(long shipmentCost) {
		this.shipmentCost = shipmentCost;
	}

	public boolean isSlipped() {
		return slipped;
	}

	public void setSlipped(boolean slipped) {
		this.slipped = slipped;
	}

	public String getSlipNumber() {
		return slipNumber;
	}

	public void setSlipNumber(String slipNumber) {
		this.slipNumber = slipNumber;
	}


	public SubscrArticle getArticle() {
		return article;
	}


	public void setArticle(SubscrArticle article) {
		this.article = article;
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


	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
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
		SubscrDelivery other = (SubscrDelivery) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
