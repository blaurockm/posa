package net.buchlese.bofc.api.shift;

import java.time.DayOfWeek;
import java.time.Instant;

public class Schedule {
	private long id;
	private DayOfWeek day;
	private Instant workFrom;
	private Instant workTill;
	private double workHours;
	private Store workPlace;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public DayOfWeek getDay() {
		return day;
	}
	public void setDay(DayOfWeek day) {
		this.day = day;
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
	public Store getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(Store workPlace) {
		this.workPlace = workPlace;
	}
	
}
