package net.buchlese.bofc.api.bofc;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table( name = "accinvexport")
public class AccountingInvoiceExport {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;
	@JsonProperty
	private int pointId;
	@JsonProperty
	private int refAccount;
	@JsonProperty
	private int datasize;
	@JsonIgnore
	private String description;
	@JsonProperty
	private LocalDateTime execDate;
	@JsonProperty
	@Column(name="fromDate")
	private LocalDateTime from;
	@JsonProperty
	@Column(name="tillDate")
	private LocalDateTime till;
	
	@JsonIgnore
	@OneToMany
	@JoinColumn(name = "EXPORT_ID")
	private Set<PosInvoice> invoices;

	public LocalDateTime getExecDate() {
		return execDate;
	}
	public void setExecDate(LocalDateTime execDate) {
		this.execDate = execDate;
	}
	public int getPointId() {
		return pointId;
	}
	public void setPointId(int pointId) {
		this.pointId = pointId;
	}
	public int getRefAccount() {
		return refAccount;
	}
	public void setRefAccount(int refAccount) {
		this.refAccount = refAccount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Set<PosInvoice> getInvoices() {
		return invoices;
	}
	public void setInvoices(Set<PosInvoice> invoices) {
		this.invoices = invoices;
	}

	public LocalDateTime getFrom() {
		return from;
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public LocalDateTime getTill() {
		return till;
	}

	public void setTill(LocalDateTime till) {
		this.till = till;
	}

	public int getDatasize() {
		return datasize;
	}

	public void setDatasize(int datasize) {
		this.datasize = datasize;
	}
	
}
