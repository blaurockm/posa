package net.buchlese.bofc.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosTicket;
import net.buchlese.bofc.api.bofc.PosTx;

public class Validator {
	private PosCashBalance balance;
	
	public Validator(PosCashBalance balance) {
		this.balance = balance;
	}

	public void validateDetails(OutputStream output) throws IOException {
		Writer w = new BufferedWriter(new OutputStreamWriter(output));
		
		w.write("GoodsOut muss der Summe der TaxBalance entsprechen :");
		long taxBalanceSum = balance.getTaxBalance().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		if (balance.getRevenue() == null ||  balance.getGoodsOut() == null || balance.getGoodsOut() != taxBalanceSum) {
			w.write(" nein\n");
		} else {
			w.write(" ok\n");
		}

		w.write("GoodsOut muss der Summe der ArticleGroupBalance entsprechen : ");
		long articleGrpSum = balance.getArticleGroupBalance().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		if ( balance.getGoodsOut() != articleGrpSum) {
			w.write(" nein\n");
		} else {
			w.write(" ok\n");
		}

		w.write("Revenue muss der Summe der PaymentMethodBalance entsprechen : ");
		long paymBalanceSum = balance.getPaymentMethodBalance().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		if ( balance.getRevenue() != paymBalanceSum) {
			w.write(" nein\n");
		} else {
			w.write(" ok\n");
		}

		w.write("Einzelcheck der Belege...\n ");
		List<PosTicket> tickets = balance.getTickets();
		for (PosTicket ticket : tickets) {
			w.write(" Check "); 
			w.write(String.valueOf(ticket.getBelegNr()));
			w.write(" (" + ticket.getPaymentMethod() + ")");
			long tito = ticket.getTotal();
			long txto = ticket.getTxs().stream().mapToLong(PosTx::getTotal).sum();
			if (tito != txto) {
				w.write(" nein, ti=" + tito + " <-> tx=" + txto + "\n");
			} else {
				w.write(" ok\n");
			}
		}
		
		w.write("Summe der TxTypes muss der Umsatz entsprechen : ");
		long debitPay = balance.getPayedInvoices().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		long cashIn = balance.getCashIn().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		long cashOut = balance.getCashOut().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		
		
		long rev = cashIn + cashOut + balance.getCouponTradeIn() +
				   balance.getCouponTradeOut() + balance.getGoodsOut() + debitPay;
		
		if ( balance.getRevenue() != rev) {
			w.write(" nein\n");
		} else {
			w.write(" ok\n");
		}
		
		w.flush();
	}

	public static boolean validBalance(PosCashBalance balance) {
		
		// GoodsOut muss der Summe der TaxBalance entsprechen
		long taxBalanceSum = balance.getTaxBalance().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		if (balance.getRevenue() == null ||  balance.getGoodsOut() == null || balance.getGoodsOut() != taxBalanceSum) {
			return false;
		}

		// GoodsOut muss der Summe der ArticleGroupBalance entsprechen
		long articleGrpSum = balance.getArticleGroupBalance().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		if ( balance.getGoodsOut() != articleGrpSum) {
			return false;
		}

		// Revenue muss der Summe der PaymentMethodBalance entsprechen
		long paymBalanceSum = balance.getPaymentMethodBalance().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		if ( balance.getRevenue() != paymBalanceSum) {
			return false;
		}

		// Summe der TxTypes muss der Umsatz entsprechen
		long debitPay = balance.getPayedInvoices().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		long cashIn = balance.getCashIn().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		long cashOut = balance.getCashOut().entrySet().stream().mapToLong(Map.Entry::getValue).sum();
		
		
		long rev = cashIn + cashOut + balance.getCouponTradeIn() +
				   balance.getCouponTradeOut() + balance.getGoodsOut() + debitPay;
		
		if ( balance.getRevenue() != rev) {
			return false;
		}
		
		
		return true;
	}

}
