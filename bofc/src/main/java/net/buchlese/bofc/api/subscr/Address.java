package net.buchlese.bofc.api.subscr;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
	@NotEmpty
	@JsonProperty
	private long id;
	@JsonProperty
	private String name1;
	@JsonProperty
	private String name2;
	@JsonProperty
	private String name3;
	@JsonProperty
	private String street;
	@JsonProperty
	private String postalcode;
	@JsonProperty
	private String city;
	

}
