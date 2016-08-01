package net.buchlese.bofc.api.bofc;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccrualDay extends AccrualPeriod {
	@JsonProperty
	private LocalDateTime day;
	
	public LocalDateTime getDay() {
		return day;
	}
	public void setDay(LocalDateTime day) {
		this.day = day;
	}

}
