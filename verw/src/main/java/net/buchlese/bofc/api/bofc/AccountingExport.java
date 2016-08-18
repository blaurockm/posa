package net.buchlese.bofc.api.bofc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
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
@Table( name = "accountingexport")
public class AccountingExport {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty
	private Long id;
	@JsonProperty
	@Column(name="key_")
	private int key;
	@JsonProperty
	private int pointId;
	@JsonProperty
	private int refAccount;
	@JsonIgnore
	private String description;
	@JsonProperty
	private LocalDate execDate;
	@JsonProperty
	@Column(name="fromDate")
	private LocalDate from;
	@JsonProperty
	@Column(name="tillDate")
	private LocalDate till;

	@JsonProperty
	@OneToMany
	@JoinColumn(name = "EXPORT_ID")
	private Set<PosCashBalance> balances;

	@JsonProperty
	@OneToMany
	@JoinColumn(name = "EXPORT_ID")
	private Set<PosInvoice> invoices;

	
	public void addBalance(PosCashBalance bal) {
		if (getBalances() == null) {
			setBalances(new HashSet<PosCashBalance>());
		}
		getBalances().add(bal);
		bal.setExported(true);
		bal.setExportDate(LocalDateTime.now());
	}
	
	public void addBalances(Collection<PosCashBalance> coll) {
		coll.forEach(this::addBalance);
	}
	
	public LocalDate getExecDate() {
		return execDate;
	}
	public void setExecDate(LocalDate execDate) {
		this.execDate = execDate;
	}
	public LocalDate getFrom() {
		return from;
	}
	public void setFrom(LocalDate from) {
		this.from = from;
	}
	public LocalDate getTill() {
		return till;
	}
	public void setTill(LocalDate till) {
		this.till = till;
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
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<PosCashBalance> getBalances() {
		return balances;
	}
	public void setBalances(Set<PosCashBalance> balances) {
		this.balances = balances;
	}
	public Set<PosInvoice> getInvoices() {
		return invoices;
	}
	public void setInvoices(Set<PosInvoice> invoices) {
		this.invoices = invoices;
	}
	
}
