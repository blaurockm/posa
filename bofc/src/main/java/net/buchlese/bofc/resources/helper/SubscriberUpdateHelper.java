package net.buchlese.bofc.resources.helper;

import org.apache.commons.lang3.StringUtils;

import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.ShipType;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaSubscriberDAO;

public class SubscriberUpdateHelper {

	private final SubscrDAO dao;
	private final JpaSubscriberDAO jpaSubscriberDao;

	public SubscriberUpdateHelper(SubscrDAO dao, JpaSubscriberDAO jpaSubscriberDao) {
		super();
		this.dao = dao;
		this.jpaSubscriberDao = jpaSubscriberDao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		Subscriber art = dao.getSubscriber(id);
		if ("name".equals(field)) {
			art.setName(value);
			res.success = true;
		}
		if ("telephone".equals(field)) {
			art.setTelephone(value);
			res.success = true;
		}
		if ("email".equals(field)) {
			art.setEmail(value);
			res.success = true;
		}
		if ("debitorId".equals(field)) {
			art.setDebitorId(Integer.parseInt(value));
			res.success = true;
		}
		if ("pointId".equals(field)) {
			art.setPointid(Integer.parseInt(value));
			res.success = true;
		}
		if ("shipmentType".equals(field)) {
			art.setShipmentType(ShipType.valueOf(value));
			res.success = true;
		}
		if ("collectiveInvoice".equals(field)) {
			art.setCollectiveInvoice(Boolean.valueOf(value));
			res.success = true;
		}
		if ("needsDeliveryNote".equals(field)) {
			art.setNeedsDeliveryNote(Boolean.valueOf(value));
			res.success = true;
		}

		if (field.contains(".") && field.startsWith("invoiceAddress")) {
			field = field.substring(field.indexOf(".")+1);
			Address a = art.getInvoiceAddress();
			if (a == null) {
				a = new Address();
				art.setInvoiceAddress(a);
			}
			if ("name1".equals(field)) {
				a.setName1(StringUtils.left(value,40));
				res.success = true;
			}
			if ("name2".equals(field)) {
				a.setName2(StringUtils.left(value,40));
				res.success = true;
			}
			if ("name3".equals(field)) {
				a.setName3(StringUtils.left(value,40));
				res.success = true;
			}
			if ("street".equals(field)) {
				a.setStreet(StringUtils.left(value,40));
				res.success = true;
			}
			if ("postalcode".equals(field)) {
				a.setPostalcode(StringUtils.left(value,5));
				res.success = true;
			}
			if ("city".equals(field)) {
				a.setCity(StringUtils.left(value,50));
				res.success = true;
			}
		}
		if (res.success) {
			jpaSubscriberDao.update(art);
		}
		return res;
	}
	
	
}
