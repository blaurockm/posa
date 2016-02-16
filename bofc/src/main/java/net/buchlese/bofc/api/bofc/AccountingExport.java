package net.buchlese.bofc.api.bofc;

import java.util.List;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountingExport {
	@JsonProperty
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
	private LocalDate from;
	@JsonProperty
	private LocalDate till;
	@JsonProperty
	private List<PosCashBalance> balances;
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
	public List<PosCashBalance> getBalances() {
		return balances;
	}
	public void setBalances(List<PosCashBalance> balances) {
		this.balances = balances;
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
	
}
