package net.buchlese.bofc.api.subscr;

import org.joda.time.Period;

public enum PayIntervalType {
	EACHDELIVERY ("pro Lieferung", null),
	MONTHLY ("Monatlich", Period.months(1)),
	QUARTERLY ("Vierteljährlich", Period.months(3)),
	HALFYEARLY ("Halbjährlich", Period.months(6)),
	YEARLY ("Jährlich", Period.months(12));
	
	private final String text;
	private final Period period;
	PayIntervalType(String d, Period p) {
		this.text = d;
		this.period = p;
	}
	
	public String getText() {
		return text;
	}
	
	public Period getPeriod() {
		return period;
	}
}
