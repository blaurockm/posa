package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.ShipType;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.core.DateUtils;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaSubscriptionDAO;

public class SubscriptionUpdateHelper {

	private final JpaSubscriptionDAO jpaSubscriptionDao;

	public SubscriptionUpdateHelper(SubscrDAO dao, JpaSubscriptionDAO jpaSubscriptionDao) {
		super();
		this.jpaSubscriptionDao = jpaSubscriptionDao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		Subscription art = jpaSubscriptionDao.findById(id);
		if ("deliveryInfo1".equals(field)) {
			res.oldValue = art.getDeliveryInfo1();
			art.setDeliveryInfo1(value);
			res.success = true;
		}
		if ("deliveryInfo2".equals(field)) {
			res.oldValue = art.getDeliveryInfo2();
			art.setDeliveryInfo2(value);
			res.success = true;
		}
		if ("shipmentType".equals(field)) {
			res.oldValue = String.valueOf(art.getShipmentType());
			art.setShipmentType(ShipType.valueOf(value));
			res.success = true;
		}
		if ("quantity".equals(field)) {
			res.oldValue = String.valueOf(art.getQuantity());
			art.setQuantity(Integer.parseInt(value));
			res.success = true;
		}
		if ("paymentType".equals(field)) {
			res.oldValue = String.valueOf(art.getPaymentType());
			art.setPaymentType(PayIntervalType.valueOf(value));
			res.success = true;
		}
		if ("startDate".equals(field)) {
			res.oldValue = String.valueOf(art.getStartDate());
			art.setStartDate(DateUtils.parse(value));
			res.success = true;
		}
		if ("endDate".equals(field)) {
			res.oldValue = String.valueOf(art.getEndDate());
			art.setEndDate(DateUtils.parse(value));
			res.success = true;
		}
		if ("payedUntil".equals(field)) {
			res.oldValue = String.valueOf(art.getPayedUntil());
			art.setPayedUntil(DateUtils.parseMonth(value));
			res.success = true;
		}
		if ("needsAttention".equals(field)) {
			res.oldValue = String.valueOf(art.isNeedsAttention());
			art.setNeedsAttention(Boolean.valueOf(value));
			res.success = true;
		}
		if ("memo".equals(field)) {
			res.oldValue = art.getMemo();
			art.setMemo(value);
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
				res.oldValue = a.getName1();
				a.setName1(value);
				res.success = true;
			}
			if ("name2".equals(field)) {
				res.oldValue = a.getName2();
				a.setName2(value);
				res.success = true;
			}
			if ("name3".equals(field)) {
				res.oldValue = a.getName3();
				a.setName3(value);
				res.success = true;
			}
			if ("street".equals(field)) {
				res.oldValue = a.getStreet();
				a.setStreet(value);
				res.success = true;
			}
			if ("postalcode".equals(field)) {
				res.oldValue = a.getPostalcode();
				a.setPostalcode(value);
				res.success = true;
			}
			if ("city".equals(field)) {
				res.oldValue = a.getCity();
				a.setCity(value);
				res.success = true;
			}
		}

		if (res.success) {
			res.newValue = value;
			jpaSubscriptionDao.update(art);
		}
		return res;
	}
	
	
}
