package net.buchlese.bofc.api.bofc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccrualMonth extends AccrualPeriod {
	@JsonProperty
	private int month;
	@JsonProperty
	private int year;
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	
}
