package net.buchlese.bofc.api.bofc;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
@Table( name = "accbalexport")
public class AccountingBalanceExport {
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
	private Set<PosCashBalance> balances;
	
	public void addBalance(PosCashBalance bal) {
		if (getBalances() == null) {
			setBalances(new HashSet<PosCashBalance>());
		}
		getBalances().add(bal);
		bal.export(this);
	}
	
	public void setBalances(Collection<PosCashBalance> coll) {
		if (coll == null || coll.isEmpty()) {
			return;
		}
		List<PosCashBalance> invSorted = coll.stream().sorted(Comparator.comparing(PosCashBalance::getFirstCovered)).collect(Collectors.toList());
		PosCashBalance first = invSorted.get(0);
		PosCashBalance last = invSorted.get(invSorted.size()-1);
		setFrom(first.getFirstCovered());
		setTill(last.getLastCovered());
		setDatasize(coll.size());
		coll.forEach(this::addBalance);
	}

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
	public Set<PosCashBalance> getBalances() {
		return balances;
	}
	public void setBalances(Set<PosCashBalance> balances) {
		this.balances = balances;
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
