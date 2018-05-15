package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaSubscrDeliveryDAO;

public class SubscrDeliveryUpdateHelper {

	private final SubscrDAO dao;
	private final JpaSubscrDeliveryDAO jpaSubscrDeliveryDao;

	public SubscrDeliveryUpdateHelper(SubscrDAO dao, JpaSubscrDeliveryDAO jpaSubscrDeliveryDao) {
		super();
		this.dao = dao;
		this.jpaSubscrDeliveryDao = jpaSubscrDeliveryDao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		SubscrDelivery art = dao.getSubscrDelivery(id);
		if ("total".equals(field)) {
			SubscrArticle artH = art.getArticle();
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
			art.setPayed(Boolean.valueOf(value));
			res.success = true;
		}
		if ("slipped".equals(field)) {
			art.setSlipped(Boolean.valueOf(value));
			res.success = true;
		}
		if (res.success) {
			jpaSubscrDeliveryDao.update(art);
		}
		return res;
	}
	
	
}
