package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.core.DateUtils;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDAO;

public class SubscrIntervalUpdateHelper {

	private final SubscrDAO dao;
	private final JpaSubscrIntervalDAO jpaSubscrIntervalDao;

	public SubscrIntervalUpdateHelper(SubscrDAO dao, JpaSubscrIntervalDAO jpaSubscrIntervalDao) {
		super();
		this.dao = dao;
		this.jpaSubscrIntervalDao = jpaSubscrIntervalDao;
	}
	
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		SubscrInterval art = dao.getSubscrInterval(id);
		if ("brutto".equals(field)) {
			art.updateBrutto(Long.parseLong(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("brutto_half".equals(field)) {
			art.updateBruttoHalf(Long.parseLong(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("brutto_full".equals(field)) {
			art.updateBruttoFull(Long.parseLong(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("halfPercentage".equals(field)) {
			art.updateHalfPercentage(Double.parseDouble(value));
			res.initializeMoneyFieldsFromArticle(art);
			res.success = true;
		}
		if ("name".equals(field)) {
			art.setName(art.initializeName(value));
			res.success = true;
		}
		if ("startDate".equals(field)) {
			art.setStartDate(DateUtils.parse(value ));
			SubscrProduct p = art.getProduct();
			art.setName(art.initializeName(p.getNamePattern()));
			res.success = true;
		}
		if ("endDate".equals(field)) {
			art.setEndDate(DateUtils.parse(value ));
			SubscrProduct p = art.getProduct();
			art.setName(art.initializeName(p.getNamePattern()));
			res.success = true;
		}
		if ("intervalType".equals(field)) {
			res.oldValue = String.valueOf(art.getIntervalType());
			art.setIntervalType(PayIntervalType.valueOf(value));
			SubscrProduct p = art.getProduct();
			art.updateEndDate();
			p.setLastInterval(art.getEndDate());
			art.setName(art.initializeName(p.getNamePattern()));
			dao.updateSubscrProduct(p);
			res.endDate = DateUtils.format(art.getEndDate());
			res.success = true;
		}
		if (res.success) {
			res.name = art.getName();
			jpaSubscrIntervalDao.update(art);
		}
		return res;
	}
	
	
}
