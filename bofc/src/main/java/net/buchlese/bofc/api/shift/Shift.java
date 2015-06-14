package net.buchlese.bofc.api.shift;

import java.time.Instant;
import java.time.LocalDate;

public class Shift {
	private long id;
	private Employee employee;
	private Shop store;
	private LocalDate date;
	private Instant workFrom;
	private Instant workTill;
	private double workHours;
	private boolean settled;
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Shop getStore() {
		return store;
	}
	public void setStore(Shop store) {
		this.store = store;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Instant getWorkFrom() {
		return workFrom;
	}
	public void setWorkFrom(Instant workFrom) {
		this.workFrom = workFrom;
	}
	public Instant getWorkTill() {
		return workTill;
	}
	public void setWorkTill(Instant workTill) {
		this.workTill = workTill;
	}
	public double getWorkHours() {
		return workHours;
	}
	public void setWorkHours(double workHours) {
		this.workHours = workHours;
	}
	public boolean isSettled() {
		return settled;
	}
	public void setSettled(boolean settled) {
		this.settled = settled;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
}
