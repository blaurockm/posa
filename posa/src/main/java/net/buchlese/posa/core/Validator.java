package net.buchlese.posa.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;

public class Validator {

	private PosCashBalance balance;
	private final PosTicketDAO ticketDAO;
	
	public Validator(PosCashBalance balance, PosTicketDAO ticketDAO) {
		this.balance = balance;
		this.ticketDAO = ticketDAO;
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

		w.write("ArticleGroupBalance darf nicht NONE enthalten: ");
		if (balance.getArticleGroupBalance().containsKey(ArticleGroup.NONE.getKey())) {
			w.write(" leider doch.\n");
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
		List<PosTx> txs = ticketDAO.fetchTx(balance.getFirstCovered(), balance.getLastCovered());
		List<PosTicket> tickets = ticketDAO.fetch(balance.getFirstCovered(), balance.getLastCovered());
		for (PosTicket ticket : tickets) {
			w.write(" Check "); 
			w.write(String.valueOf(ticket.getBelegNr()));
			w.write(" (" + ticket.getPaymentMethod() + ")");
			long tito = ticket.getTotal();
			long txto = txs.stream().filter(t -> t.getBelegNr() == ticket.getBelegNr()).mapToLong(PosTx::getTotal).sum();
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
		if (balance.getArticleGroupBalance().containsKey(ArticleGroup.NONE.getKey())) {
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
