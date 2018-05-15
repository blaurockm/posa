package net.buchlese.bofc.core;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrProduct;

public class CreationUtils {
	private CreationUtils() {
	}

	public static SubscrArticle createArticle(SubscrProduct prod) {
		SubscrArticle na = new SubscrArticle();
		na.setProduct(prod);
		if (prod.getHalfPercentage() <= 0.0001d) {
			na.setHalfPercentage(1d);
		} else {
			na.setHalfPercentage(prod.getHalfPercentage());
		}
		na.updateBrutto(prod.getBaseBrutto());
		na.setErschTag(new java.sql.Date(System.currentTimeMillis()));
		na.setIssueNo(prod.getCount() +1);
		na.setName(na.initializeName(prod.getNamePattern()));
		
		// Seiteneffekte auf das Produkt
		prod.setCount(na.getIssueNo());
		
		return na;
	}

	public static SubscrInterval createInterval(SubscrProduct prod) {
		SubscrInterval na = new SubscrInterval();
		na.setProduct(prod);
		if (prod.getIntervalType() == null) {
			na.setIntervalType(PayIntervalType.YEARLY);
		} else {
			na.setIntervalType(prod.getIntervalType());
		}
		if (prod.getHalfPercentage() <= 0.0001d) {
			na.setHalfPercentage(1d);
		} else {
			na.setHalfPercentage(prod.getHalfPercentage());
		}
		na.updateBrutto(prod.getBaseBrutto());
		if (prod.getLastInterval() != null) {
			na.setStartDate(java.sql.Date.valueOf(prod.getLastInterval().toLocalDate().plusDays(1)));
		} else {
			na.setStartDate(DateUtils.now());
		}
		na.updateEndDate();
		String pattern = prod.getIntervalPattern() != null ? prod.getIntervalPattern() : prod.getNamePattern();
		na.setName(na.initializeName(pattern));
		
		//Seiteneffekte auf das Produkt
		prod.setLastInterval(na.getEndDate());

		
		return na;
	}
	
}
