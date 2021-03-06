package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDeliveryDAO;

public class SubscrIntervalDeliveryUpdateHelper {

	private final SubscrDAO dao;
	private final JpaSubscrIntervalDeliveryDAO jpaSubscrIntervalDeliveryDao;

	public SubscrIntervalDeliveryUpdateHelper(SubscrDAO dao, JpaSubscrIntervalDeliveryDAO jpaSubscrIntervalDeliveryDao) {
		super();
		this.dao = dao;
		this.jpaSubscrIntervalDeliveryDao = jpaSubscrIntervalDeliveryDao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		SubscrIntervalDelivery art = dao.getSubscrIntervalDelivery(id);
		if ("total".equals(field)) {
			SubscrInterval artH = art.getInterval();
			art.updateBrutto(Long.parseLong(value), artH.getHalfPercentage());
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("totalHalf".equals(field)) {
			art.updateBruttoHalf(Long.parseLong(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("totalFull".equals(field)) {
			art.updateBruttoFull(Long.parseLong(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("shipmentCost".equals(field)) {
			art.setShipmentCost(Long.parseLong(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("payed".equals(field)) {
			art.setPayed(Boolean.parseBoolean(value));
			res.success = true;
		}
		if (res.success) {
			jpaSubscrIntervalDeliveryDao.update(art);
		}
		return res;
	}
	
	
}
