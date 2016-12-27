package net.buchlese.verw.reports;

import java.util.Arrays;
import java.util.Comparator;

import net.buchlese.bofc.api.bofc.AccountingInvoiceExport;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.verw.reports.obj.ReportInvoiceExport;

import org.springframework.stereotype.Component;

@Component
public class ReportInvoiceExportCreator {

	public ReportInvoiceExport createReport(AccountingInvoiceExport export) {
		ReportInvoiceExport rep = new ReportInvoiceExport();
		rep.setExecDate(export.getExecDate().toLocalDateTime());
		rep.setPosname(decodePos(export.getPointId()));
		rep.setDescription("Rechnungsausgangsjournal für " + rep.getPosname());
		rep.setExportId(export.getId());
		PosInvoice[] invs = export.getInvoices().toArray(new PosInvoice[]{});
		Arrays.sort(invs, Comparator.comparing(PosInvoice::getDate).thenComparing(PosInvoice::getNumber));
		rep.setFirstInvoice(invs[0]);
		rep.setLastInvoice(invs[invs.length-1]);
		rep.setInvoices(invs);
		
		long taxSumFull = 0;
		long taxSumHalf = 0;
		long taxSumNone = 0;
		long sum = 0;
		for (PosInvoice inv : invs) {
			if (inv.getAmountFull() != null) {
				taxSumFull += inv.getAmountFull();
			}
			if (inv.getAmountHalf() != null) {
				taxSumHalf += inv.getAmountHalf();
			}
			if (inv.getAmountNone() != null) {
				taxSumNone += inv.getAmountNone();
			}
			sum += inv.getAmount();
		}
		rep.setTaxFullSum(taxSumFull);
		rep.setTaxHalfSum(taxSumHalf);
		rep.setTaxNoneSum(taxSumNone);
		rep.setInvoicesSum(sum);
		
		return rep;
	}
	

	private String decodePos(int pointId) {
		switch (pointId) {
		case 1: return "Dornhan";
		case 2: return "Sulz";
		case 3: return "Schramberg";
		default: return "neuer Laden";
		}
	}
	
}
