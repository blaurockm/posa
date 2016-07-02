package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

public class SubscrDeliveryUpdateHelper {

	private final SubscrDAO dao;

	public SubscrDeliveryUpdateHelper(SubscrDAO dao) {
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
		SubscrDelivery art = dao.getSubscrDelivery(id);
		if ("total".equals(field)) {
			SubscrArticle artH = dao.getSubscrArticle(art.getArticleId());
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
		if (res.success) {
			dao.updateDelivery(art);
		}
		return res;
	}
	
	
}
