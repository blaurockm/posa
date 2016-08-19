package net.buchlese.verw.core.accounting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LedgerEntry {
	private List<Booking> bookings = new ArrayList<Booking>();
	private boolean soll = false;
	private String number; // belegnummer;
	private LocalDate date; // belegdatum;
	
	public List<Booking> getBookings() {
		return bookings;
	}
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	public boolean isSoll() {
		return soll;
	}
	public void setSoll(boolean soll) {
		this.soll = soll;
	}
	
	public void add(Booking b) {
		if (date == null) {
			this.date = b.getDate();
		}
		this.bookings.add(b);
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}