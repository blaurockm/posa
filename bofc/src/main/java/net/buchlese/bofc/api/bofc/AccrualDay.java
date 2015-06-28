package net.buchlese.bofc.api.bofc;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccrualDay extends AccrualPeriod {
	@JsonProperty
	private DateTime day;
	
	public DateTime getDay() {
		return day;
	}
	public void setDay(DateTime day) {
		this.day = day;
	}

}
