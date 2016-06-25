package net.buchlese.bofc.resources.helper;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;

public class IssueSlipUpdateHelper {

	private final PosInvoiceDAO dao;

	public IssueSlipUpdateHelper(PosInvoiceDAO dao) {
		super();
		this.dao = dao;
	}
	
	public UpdateResult update(String pk, String fieldname, String value) {
		UpdateResult res = new UpdateResult();
		String field = fieldname;
		if (fieldname.contains(".")) {
			field = fieldname.substring(fieldname.indexOf(".")+1);
		}
		List<PosIssueSlip> arts = dao.fetchIssueSlip(pk);
		if (arts.isEmpty()) {
			return res;
		}
		PosIssueSlip art = arts.get(0);
		if ("includeOnInvoice".equals(field)) {
			art.setIncludeOnInvoice(Boolean.valueOf(value));
			res.success = true;
		}
		if (res.success) {
			dao.updateIssueSlip(art);
		}
		return res;
	}
	
	
}
