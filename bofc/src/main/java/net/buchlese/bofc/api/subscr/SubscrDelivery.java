package net.buchlese.bofc.api.subscr;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscrDelivery {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private LocalDate deliveryDate;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private long subcriptionId;
	@JsonProperty
	private long subscriberId;
	@JsonProperty
	private long articleId;
	@JsonProperty
	private SubscrArticle article;
	@JsonProperty
	private long total;
	@JsonProperty
	private long shipmentCosts;
	@JsonProperty
	private boolean hasDeliveryNote;
	@JsonProperty
	private boolean hasInvoice;
	@JsonProperty
	private boolean payed;
	@JsonProperty
	private DateTime creationDate;
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
	public SubscrArticle getArticle() {
		return article;
	}
	public void setArticle(SubscrArticle article) {
		this.article = article;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getShipmentCosts() {
		return shipmentCosts;
	}
	public void setShipmentCosts(long shipmentCosts) {
		this.shipmentCosts = shipmentCosts;
	}
	public boolean isHasDeliveryNote() {
		return hasDeliveryNote;
	}
	public void setHasDeliveryNote(boolean hasDeliveryNote) {
		this.hasDeliveryNote = hasDeliveryNote;
	}
	public boolean isHasInvoice() {
		return hasInvoice;
	}
	public void setHasInvoice(boolean hasInvoice) {
		this.hasInvoice = hasInvoice;
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
	public void setSubcriptionId(long subcriptionId) {
		this.subcriptionId = subcriptionId;
	}
	public long getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public long getSubcriptionId() {
		return subcriptionId;
	}
	public void setSubscriberId(long subscriberId) {
		this.subscriberId = subscriberId;
	}
	
}
