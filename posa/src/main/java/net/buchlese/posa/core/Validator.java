package net.buchlese.posa.core;

import java.util.Map;

import net.buchlese.posa.api.bofc.PosCashBalance;

public class Validator {

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
