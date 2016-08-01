package net.buchlese.bofc.api.subscr;

import java.time.Period;

public enum PayIntervalType {
	EACHDELIVERY ("pro Lieferung", null),
	MONTHLY ("Monatlich", Period.ofMonths(1)),
	QUARTERLY ("Vierteljährlich", Period.ofMonths(3)),
	HALFYEARLY ("Halbjährlich", Period.ofMonths(6)),
	YEARLY ("Jährlich", Period.ofMonths(12));
	
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
