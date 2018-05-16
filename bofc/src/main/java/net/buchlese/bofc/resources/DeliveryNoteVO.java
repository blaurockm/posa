package net.buchlese.bofc.resources;

import java.sql.Date;

public class DeliveryNoteVO {
	private String number;
	private Date date;
	private String name1;
	private Date deliveryFrom;
	private Date deliveryTill;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public Date getDeliveryFrom() {
		return deliveryFrom;
	}
	public void setDeliveryFrom(Date deliveryFrom) {
		this.deliveryFrom = deliveryFrom;
	}
	public Date getDeliveryTill() {
		return deliveryTill;
	}
	public void setDeliveryTill(Date deliveryTill) {
		this.deliveryTill = deliveryTill;
	}
}
