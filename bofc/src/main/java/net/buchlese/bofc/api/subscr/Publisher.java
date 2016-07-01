package net.buchlese.bofc.api.subscr;

import io.dropwizard.jackson.Jackson;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Publisher {
	@NotEmpty
	@JsonProperty
	private long id;

	@JsonProperty
	private int creditorId;
	@JsonProperty
	private int customerId;
	@JsonProperty
	private String name;
	@JsonProperty
	private String telephone;
	@JsonProperty
	private String email;
	@JsonProperty
	private Address address;

	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
	}

	@JsonIgnore
	public String getName1() {
		if (getAddress() != null) {
			return getAddress().getName1();
		}
		return null;
	}

	@JsonIgnore
	public String getName2() {
		if (getAddress() != null) {
			return getAddress().getName2();
		}
		return null;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public int getCreditorId() {
		return creditorId;
	}

	public void setCreditorId(int creditorId) {
		this.creditorId = creditorId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
