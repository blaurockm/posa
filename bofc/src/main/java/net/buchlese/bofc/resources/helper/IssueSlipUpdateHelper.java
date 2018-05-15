package net.buchlese.bofc.resources.helper;

import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.jpa.JpaPosIssueSlipDAO;

public class IssueSlipUpdateHelper {

	private final JpaPosIssueSlipDAO dao;

	public IssueSlipUpdateHelper(JpaPosIssueSlipDAO dao) {
		super();
		this.dao = dao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		PosIssueSlip art = dao.findById(Long.valueOf(pk));
		if (art == null) {
			return res;
		}
		if ("includeOnInvoice".equals(field)) {
			art.setIncludeOnInvoice(Boolean.valueOf(value));
			res.success = true;
		}
		if (res.success) {
			dao.update(art);
		}
		return res;
	}
	
	
}
