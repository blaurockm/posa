package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.core.DateUtils;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaSubscrArticleDAO;

public class SubscrArticleUpdateHelper {

	private final SubscrDAO dao;
	private final JpaSubscrArticleDAO jpaSubscrArticleDao;

	public SubscrArticleUpdateHelper(SubscrDAO dao, JpaSubscrArticleDAO jpaSubscrArticleDao) {
		super();
		this.dao = dao;
		this.jpaSubscrArticleDao = jpaSubscrArticleDao;
	}
	
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		long id = Long.parseLong(pk);
		SubscrArticle art = dao.getSubscrArticle(id);
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
		if ("erschTag".equals(field)) {
			art.setErschTag(DateUtils.parse(value ));
			SubscrProduct p = art.getProduct();
			art.setName(art.initializeName(p.getNamePattern()));
			res.name = art.getName();
			res.success = true;
		}
		if ("issueNo".equals(field)) {
			int count = Integer.parseInt(value);
			art.setIssueNo(count);
			SubscrProduct p = art.getProduct();
			p.setCount(count);
			art.setName(art.initializeName(p.getNamePattern()));
			dao.updateSubscrProduct(p);
			res.name = art.getName();
			res.success = true;
		}
		if (res.success) {
			dao.updateArticle(art);
			jpaSubscrArticleDao.update(art);
		}
		return res;
	}
	
	
}
