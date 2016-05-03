package net.buchlese.bofc.api.subscr;

import java.util.List;

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
	private Subscriber subscriber;
	@JsonProperty
	private SubscrProduct product;
	@JsonProperty
	private int quantity;
	@JsonProperty
	private List<SubscrDelivery> deliveries;
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
}
