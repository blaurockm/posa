package net.buchlese.verw.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.buchlese.bofc.api.bofc.AccountingBalanceExport;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosTicket;
import net.buchlese.bofc.api.bofc.PosTx;
import net.buchlese.bofc.api.bofc.Tax;
import net.buchlese.bofc.api.bofc.TxType;
import net.buchlese.verw.reports.obj.ReportBalanceExport;
import net.buchlese.verw.reports.obj.ReportBalanceExport.ExtraPay;

import org.springframework.stereotype.Component;

@Component
public class ReportBalanceExportCreator {

	public ReportBalanceExport createReport(AccountingBalanceExport export) {
		ReportBalanceExport rep = new ReportBalanceExport();
		rep.setExecDate(export.getExecDate().toLocalDateTime());
		rep.setPosname(decodePos(export.getPointId()));
		rep.setDescription("Kassenberichtsexport für " + rep.getPosname());
		rep.setExportId(export.getId());
		PosCashBalance[] bals = export.getBalances().toArray(new PosCashBalance[]{});
		Arrays.sort(bals, Comparator.comparing(PosCashBalance::getFirstCovered));
		rep.setBalances(bals);
		rep.setFirstBalance(bals[0]);
		rep.setLastBalance(bals[bals.length-1]);
		rep.setCashStart(bals[0].getCashStart());
		rep.setCashEnd(bals[bals.length-1].getCashEnd());
		
		rep.setAbsorptionSum(Arrays.stream(bals).mapToLong(PosCashBalance::getAbsorption).sum());
		rep.setCashDiffSum(Arrays.stream(bals).mapToLong(x -> x.getCashDiff() != null ? x.getCashDiff() : Long.valueOf(0L)).sum());
		rep.setTelecashSum(Arrays.stream(bals).mapToLong(x -> x.getPaymentMethodBalance().get(PaymentMethod.TELE) != null ? x.getPaymentMethodBalance().get(PaymentMethod.TELE).longValue() : 0L).sum());
		rep.setRevenueSum(Arrays.stream(bals).mapToLong(PosCashBalance::getRevenue).sum());
		rep.setCashInSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCashInSum).sum());
		rep.setCashOutSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCashOutSum).sum());
		rep.setInvoicesPayedSum(Arrays.stream(bals).mapToLong(PosCashBalance::getPayedInvoicesSum).sum());
		rep.setCouponInSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCouponTradeIn).sum());
		rep.setCouponOutSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCouponTradeOut).sum());
		List<ExtraPay> ip = new ArrayList<>();
		List<ExtraPay> cop = new ArrayList<>();
		List<ExtraPay> cip = new ArrayList<>();
		long taxSumFull = 0;
		long taxSumHalf = 0;
		long taxSumNone = 0;
		for (PosCashBalance bal : bals) {
			if (bal.getTaxBalance().containsKey(Tax.FULL)) {
				taxSumFull += bal.getTaxBalance().get(Tax.FULL);
			}
			if (bal.getTaxBalance().containsKey(Tax.HALF)) {
				taxSumHalf += bal.getTaxBalance().get(Tax.HALF);
			}
			if (bal.getTaxBalance().containsKey(Tax.NONE)) {
				taxSumNone += bal.getTaxBalance().get(Tax.NONE);
			}
			for (PosTicket ticket : bal.getTickets()) {
				for (PosTx tx : ticket.getTxs()) {
					if (tx.getType().equals(TxType.DEBITPAY)) {
						ip.add(convertTx(tx));
					}
					if (tx.getType().equals(TxType.CASHIN)) {
						cip.add(convertTx(tx));
					}
					if (tx.getType().equals(TxType.CASHOUT)) {
						cop.add(convertTx(tx));
					}
				}
			}
			// optimized, dont need them for report
			bal.setTickets(null); // hopefully this doesnt get saved, its transient
		}
		rep.setInvoicePays(ip);
		rep.setCashInPays(cip);
		rep.setCashOutPays(cop);
		rep.setTaxFullSum(taxSumFull);
		rep.setTaxHalfSum(taxSumHalf);
		rep.setTaxNoneSum(taxSumNone);
		return rep;
	}
	
	private ExtraPay convertTx(PosTx tx) {
		ExtraPay p = new ExtraPay();
		p.setAmount(tx.getTotal());
		p.setText(tx.getMatchCode());
		p.setPayDate(tx.getTimestampAsLocalDate());
		return p;
	}
	

	private String decodePos(int pointId) {
		switch (pointId) {
		case 1: return "Dornhan";
		case 2: return "Sulz";
		case 3: return "Schramberg";
		default: return "neuer Laden";
		}
	}

	public long getTelecashForBalance(PosCashBalance bal) {
		if (bal.getPaymentMethodBalance() != null && bal.getPaymentMethodBalance().get(PaymentMethod.TELE) != null) {
			return bal.getPaymentMethodBalance().get(PaymentMethod.TELE).longValue();
		}
		return 0L;
	}
	
	
}
