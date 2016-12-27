package net.buchlese.bofc.api.subscr;


import javax.persistence.CascadeType;
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

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;


@Entity
@Table( name = "subscrdelivery" )
public class SubscrDelivery implements Comparable<SubscrDelivery> {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	@JsonProperty
	private Long id;

//	@DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonProperty
	private java.sql.Date deliveryDate;
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
	private java.sql.Timestamp creationDate;
	@JsonProperty
	private long shipmentCost;


	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "settdetail_id",
	foreignKey = @ForeignKey(name = "SETTDETAIL1_ID_FK")
)
	private InvoiceAgrDetail settDetail;
	
	@ManyToOne(fetch=FetchType.LAZY)
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public java.sql.Date getDeliveryDate() {
		return deliveryDate;
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
	public java.sql.Timestamp getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(java.sql.Timestamp creationDate) {
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
		SubscrDelivery other = (SubscrDelivery) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public InvoiceAgrDetail getSettDetail() {
		return settDetail;
	}

	public void setSettDetail(InvoiceAgrDetail settDetail) {
		if (settDetail != null) {
			settDetail.addSubscrDelivery(this);
		} else {
			if (this.settDetail != null) {
				this.settDetail.removeSubscrDelivery(this);
			}
		}
		this.settDetail = settDetail;
		this.payed = settDetail != null;
	}

	public void setDeliveryDate(java.sql.Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	
}
