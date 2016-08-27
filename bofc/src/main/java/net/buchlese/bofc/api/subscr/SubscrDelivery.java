package net.buchlese.bofc.api.subscr;

import io.dropwizard.jackson.Jackson;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table( name = "subscrdelivery" )
public class SubscrDelivery implements Comparable<SubscrDelivery> {
	@Id
	@JsonProperty
	private long id;

	@JsonProperty
	private LocalDate deliveryDate;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private long subscriptionId;
	@JsonProperty
	private long subscriberId;
	@JsonProperty
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
	private DateTime creationDate;
	@JsonProperty
	private long shipmentCost;

	@JsonIgnore
	public String subscriberName;

	// not for transfer
//	@JsonIgnore
//	@ManyToOne
//	@JoinColumn(name = "article_id",
//	foreignKey = @ForeignKey(name = "ARTICLE_ID_FK")
//)
//	private SubscrArticle article;
//
//	@JsonIgnore
//	@ManyToOne
//	@JoinColumn(name = "subscriber_id",
//	foreignKey = @ForeignKey(name = "CUSTOMER_ID_FK")
//)
//	private Subscriber subscriber;
//
//	@JsonIgnore
//	@ManyToOne
//	@JoinColumn(name = "subscription_id",
//    			foreignKey = @ForeignKey(name = "SUBSCRIPTION_ID_FK")
//    )
//	private Subscription subscription;

	@JsonIgnore
	public String getSubscriberName() {
		return subscriberName;
	}
	
	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
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
	public DateTime getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(DateTime creationDate) {
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
