package net.buchlese.bofc.api.subscr;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "publisher" )
public class Publisher {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;

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
	@Embedded
	private Address address;


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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
