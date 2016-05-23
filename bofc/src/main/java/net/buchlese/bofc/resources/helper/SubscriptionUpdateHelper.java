package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.ShipType;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class SubscriptionUpdateHelper {

	private final SubscrDAO dao;

	public SubscriptionUpdateHelper(SubscrDAO dao) {
		super();
		this.dao = dao;
	}
	
	
	
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		Subscription art = dao.getSubscription(id);
		if ("deliveryInfo1".equals(field)) {
			art.setDeliveryInfo1(value);
			res.success = true;
		}
		if ("deliveryInfo2".equals(field)) {
			art.setDeliveryInfo2(value);
			res.success = true;
		}
		if ("shipmentType".equals(field)) {
			art.setShipmentType(ShipType.valueOf(value));
			res.success = true;
		}
		if ("quantity".equals(field)) {
			art.setQuantity(Integer.parseInt(value));
			res.success = true;
		}
		if ("paymentType".equals(field)) {
			art.setPaymentType(PayIntervalType.valueOf(value));
			res.success = true;
		}
		if ("startDate".equals(field)) {
			art.setStartDate(LocalDate.parse(value, DateTimeFormat.forPattern("dd.MM.yyyy") ));
			res.success = true;
		}
		if ("endDate".equals(field)) {
			art.setEndDate(LocalDate.parse(value, DateTimeFormat.forPattern("dd.MM.yyyy") ));
			res.success = true;
		}
		if ("payedUntil".equals(field)) {
			art.setPayedUntil(LocalDate.parse(value, DateTimeFormat.forPattern("MM/yyyy") ));
			res.success = true;
		}
		if (field.contains(".") && field.startsWith("deliveryAddress")) {
			field = field.substring(field.indexOf(".")+1);
			Address a = art.getDeliveryAddress();
			if (a == null) {
				a = new Address();
				art.setDeliveryAddress(a);
			}
			if ("name1".equals(field)) {
				a.setName1(value);
				res.success = true;
			}
			if ("name2".equals(field)) {
				a.setName2(value);
				res.success = true;
			}
			if ("name3".equals(field)) {
				a.setName3(value);
				res.success = true;
			}
			if ("street".equals(field)) {
				a.setStreet(value);
				res.success = true;
			}
			if ("postalcode".equals(field)) {
				a.setPostalcode(value);
				res.success = true;
			}
			if ("city".equals(field)) {
				a.setCity(value);
				res.success = true;
			}
		}

		if (res.success) {
			dao.updateSubscription(art);
		}
		return res;
	}
	
	
}
