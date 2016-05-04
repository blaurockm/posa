package net.buchlese.bofc.api.subscr;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscriber {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private int debitorId;
	@JsonProperty
	private int customerId;
	@JsonProperty
	private Address invoiceAddress;

}
