package net.buchlese.bofc.api.subscr;

import org.hibernate.validator.constraints.NotEmpty;
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
	private SubscrArticle article;
	@JsonProperty
	private boolean hasDeliveryNote;
	@JsonProperty
	private boolean hasInvoice;
	@JsonProperty
	private boolean payed;
}
