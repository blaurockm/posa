package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

public class SubscrProductUpdateHelper {

	private final SubscrDAO dao;

	public SubscrProductUpdateHelper(SubscrDAO dao) {
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
		SubscrProduct art = dao.getSubscrProduct(id);
		if ("abbrev".equals(field)) {
			art.setAbbrev(value);
			res.success = true;
		}
		if ("name".equals(field)) {
			art.setName(value);
			res.success = true;
		}
		if ("publisher".equals(field)) {
			art.setPublisher(value);
			res.success = true;
		}
		if ("namePattern".equals(field)) {
			art.setNamePattern(value);
			res.success = true;
		}
		if ("intervalPattern".equals(field)) {
			art.setIntervalPattern(value);
			res.success = true;
		}
		if ("issn".equals(field)) {
			art.setIssn(value);
			res.success = true;
		}
		if ("memo".equals(field)) {
			art.setMemo(value);
			res.success = true;
		}
		if ("url".equals(field)) {
			art.setUrl(value);
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
		if ("lastInterval".equals(field)) {
			art.setLastInterval(LocalDate.parse(value, DateTimeFormat.forPattern("dd.MM.yyyy") ));
			res.success = true;
		}
		if ("period".equals(field)) {
			art.setPeriod(Period.parse(value));
			res.success = true;
		}
		if ("payPerDelivery".equals(field)) {
			art.setPayPerDelivery(Boolean.valueOf(value));
			res.success = true;
		}
		if ("intervalType".equals(field)) {
			res.oldValue = String.valueOf(art.getIntervalType());
			art.setIntervalType(PayIntervalType.valueOf(value));
			res.success = true;
		}
		if ("count".equals(field)) {
			art.setCount(Integer.parseInt(value));
			res.success = true;
		}
		if ("quantity".equals(field)) {
			art.setQuantity(Integer.parseInt(value));
			res.success = true;
		}
		if ("baseBrutto".equals(field)) {
			art.setBaseBrutto(Long.parseLong(value));
			res.success = true;
		}
		if (art.getHalfPercentage() <= 0.0001d) {
			art.setHalfPercentage(1d);  // TODO: solange wir keine 100%igen 19%er haben
		}
		if ("halfPercentage".equals(field)) {
			art.setHalfPercentage(Double.parseDouble(value));
			res.success = true;
		}
		if (res.success) {
			dao.updateSubscrProduct(art);
		}
		return res;
	}
	
	
}
