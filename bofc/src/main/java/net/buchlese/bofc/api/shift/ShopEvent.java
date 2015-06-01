package net.buchlese.bofc.api.shift;

import java.time.Instant;

public class ShopEvent {
	private long id;
	private Instant from;
	private Instant till;
	private boolean wholeDay;
	private boolean workFree;
	private Employee doneBy;
	private Store doneWhere;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Instant getFrom() {
		return from;
	}
	public void setFrom(Instant from) {
		this.from = from;
	}
	public Instant getTill() {
		return till;
	}
	public void setTill(Instant till) {
		this.till = till;
	}
	public boolean isWholeDay() {
		return wholeDay;
	}
	public void setWholeDay(boolean wholeDay) {
		this.wholeDay = wholeDay;
	}
	public boolean isWorkFree() {
		return workFree;
	}
	public void setWorkFree(boolean workFree) {
		this.workFree = workFree;
	}
	public Employee getDoneBy() {
		return doneBy;
	}
	public void setDoneBy(Employee doneBy) {
		this.doneBy = doneBy;
	}
	public Store getDoneWhere() {
		return doneWhere;
	}
	public void setDoneWhere(Store doneWhere) {
		this.doneWhere = doneWhere;
	}
}
