package net.buchlese.bofc.core.accounting;

import org.joda.time.DateTime;

/**
 * Helferklasse. Eine einfach Buchung auf ein Konto. 2 Stück davon ergeben eine Doppik
 * @author Markus Blaurock
 *
 */
public class Booking {
	private int account;
	private long betrag;
	private String text;
	private DateTime date;
	private String code;
	public int getAccount() {
		return account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
	public long getBetrag() {
		return betrag;
	}
	public void setBetrag(long betrag) {
		this.betrag = betrag;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public DateTime getDate() {
		return date;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
}