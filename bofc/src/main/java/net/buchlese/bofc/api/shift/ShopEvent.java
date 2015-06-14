package net.buchlese.bofc.api.shift;

import java.time.Instant;
import java.time.LocalDate;

public class ShopEvent {
	private long id;
	private Instant from;
	private Instant till;
	private boolean wholeDay;
	private boolean workFree;
	private Employee doneBy;
	private Shop doneWhere;
	private LocalDate date;
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
	public Shop getDoneWhere() {
		return doneWhere;
	}
	public void setDoneWhere(Shop doneWhere) {
		this.doneWhere = doneWhere;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
