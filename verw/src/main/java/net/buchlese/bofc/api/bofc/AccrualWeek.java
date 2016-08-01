package net.buchlese.bofc.api.bofc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccrualWeek extends AccrualPeriod {
	@JsonProperty
	private int week;
	@JsonProperty
	private int year;
	
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

}
