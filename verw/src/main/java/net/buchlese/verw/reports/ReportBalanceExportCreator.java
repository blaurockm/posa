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
import net.buchlese.bofc.api.bofc.TxType;
import net.buchlese.verw.reports.obj.ReportBalanceExport;
import net.buchlese.verw.reports.obj.ReportBalanceExport.InvoicePay;

import org.springframework.stereotype.Component;

@Component
public class ReportBalanceExportCreator {

	public ReportBalanceExport createReport(AccountingBalanceExport export) {
		ReportBalanceExport rep = new ReportBalanceExport();
		rep.setExecDate(export.getExecDate());
		rep.setDescription(export.getDescription());
		PosCashBalance[] bals = export.getBalances().toArray(new PosCashBalance[]{});
		Arrays.sort(bals, Comparator.comparing(PosCashBalance::getFirstCovered));
		rep.setBalances(bals);
		rep.setFirstBalance(bals[0]);
		rep.setLastBalance(bals[bals.length-1]);
		rep.setCashStart(bals[0].getCashStart());
		rep.setCashEnd(bals[bals.length-1].getCashEnd());
		
		rep.setAbsorptionSum(Arrays.stream(bals).mapToLong(PosCashBalance::getAbsorption).sum());
		rep.setTelecashSum(Arrays.stream(bals).mapToLong(x -> x.getPaymentMethodBalance().get(PaymentMethod.TELE) != null ? x.getPaymentMethodBalance().get(PaymentMethod.TELE).longValue() : 0L).sum());
		rep.setRevenueSum(Arrays.stream(bals).mapToLong(PosCashBalance::getRevenue).sum());
		rep.setCashInSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCashInSum).sum());
		rep.setCashOutSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCashOutSum).sum());
		rep.setInvoicesPayedSum(Arrays.stream(bals).mapToLong(PosCashBalance::getPayedInvoicesSum).sum());
		rep.setCouponInSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCouponTradeIn).sum());
		rep.setCouponOutSum(Arrays.stream(bals).mapToLong(PosCashBalance::getCouponTradeOut).sum());
		List<InvoicePay> ip = new ArrayList<>();
		for (PosCashBalance bal : bals) {
			if (bal.getPayedInvoicesSum()> 0) {
				for (PosTicket ticket : bal.getTickets()) {
					for (PosTx tx : ticket.getTxs()) {
						if (tx.getType().equals(TxType.DEBITPAY)) {
							InvoicePay p = new InvoicePay();
							p.setAmount(tx.getTotal());
							p.setInvNum(tx.getMatchCode());
							p.setPayDate(tx.getTimestamp().toLocalDate());
							ip.add(p);
						}
					}
				}
			}
			// optimized, dont need them for report
			bal.setTickets(null); // hopefully this doesnt get saved, its transient
		}
		rep.setInvoicePays(ip);
		return rep;
	}

	public long getTelecashForBalance(PosCashBalance bal) {
		if (bal.getPaymentMethodBalance() != null && bal.getPaymentMethodBalance().get(PaymentMethod.TELE) != null) {
			return bal.getPaymentMethodBalance().get(PaymentMethod.TELE).longValue();
		}
		return 0L;
	}
	
	
}
