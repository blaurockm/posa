package net.buchlese.bofc.api.subscr;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "customer" )
public class Subscriber {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;
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

	@JsonIgnore
	@OneToMany(mappedBy = "subscriber", orphanRemoval = true)
	private Set<Subscription> subscriptions;
	
	
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

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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

	@XmlTransient
	public Set<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}


	public void addSubscription(Subscription sub) {
		if (subscriptions == null) {
			subscriptions = new HashSet<>();
		}
		subscriptions.add(sub);
	}

	public void removeSubscription(Subscription sub) {
		if (subscriptions == null) {
			subscriptions = new HashSet<>();
		}
		subscriptions.add(sub);
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
		Subscriber other = (Subscriber) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
