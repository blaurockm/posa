package net.buchlese.bofc.api.subscr;

import io.dropwizard.jackson.Jackson;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table( name = "customer" )
public class Subscriber {
	@Id
	@NotNull
	@JsonProperty
	private long id;
	@JsonProperty
	private int pointid;

	@JsonProperty
	private int debitorId;
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
	private Address invoiceAddress;
	@JsonProperty
	private boolean collectiveInvoice;
	@JsonProperty
	private boolean needsDeliveryNote;
	@JsonProperty
	@Enumerated(EnumType.STRING)
	private ShipType shipmentType;
	// sich selber als json-object ausgeben
	@JsonIgnore
	public String getComplJson() throws JsonProcessingException {
		ObjectMapper om = Jackson.newObjectMapper();
		return om.writeValueAsString(this);
	}

	@JsonIgnore
	@OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Subscription> subscriptions;
	
	
	@JsonIgnore
	public String getName1() {
		if (getInvoiceAddress() != null) {
			return getInvoiceAddress().getName1();
		}
		return null;
	}

	@JsonIgnore
	public String getName2() {
		if (getInvoiceAddress() != null) {
			return getInvoiceAddress().getName2();
		}
		return null;
	}

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
	public int getDebitorId() {
		return debitorId;
	}
	public void setDebitorId(int debitorId) {
		this.debitorId = debitorId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public Address getInvoiceAddress() {
		return invoiceAddress;
	}
	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isCollectiveInvoice() {
		return collectiveInvoice;
	}
	public void setCollectiveInvoice(boolean collectiveInvoice) {
		this.collectiveInvoice = collectiveInvoice;
	}
	public boolean isNeedsDeliveryNote() {
		return needsDeliveryNote;
	}
	public void setNeedsDeliveryNote(boolean needsDeliveryNote) {
		this.needsDeliveryNote = needsDeliveryNote;
	}
	public ShipType getShipmentType() {
		return shipmentType;
	}
	public void setShipmentType(ShipType shipmentType) {
		this.shipmentType = shipmentType;
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

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

}
