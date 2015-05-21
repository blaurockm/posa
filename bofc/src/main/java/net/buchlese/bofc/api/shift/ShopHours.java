package net.buchlese.bofc.api.shift;

import java.time.DayOfWeek;
import java.time.Instant;

public class ShopHours {
	private long id;
	private DayOfWeek weekDay;
	private Instant openFrom;
	private Instant openTill;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public DayOfWeek getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(DayOfWeek weekDay) {
		this.weekDay = weekDay;
	}
	public Instant getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(Instant openFrom) {
		this.openFrom = openFrom;
	}
	public Instant getOpenTill() {
		return openTill;
	}
	public void setOpenTill(Instant openTill) {
		this.openTill = openTill;
	}
	
}
