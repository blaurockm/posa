package net.buchlese.bofc.core.accounting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;

public class InvoiceAccounting {

	private static final int BASIS_ERLOES_19 = 4400;
	private static final int BASIS_ERLOES_7 = 4300;
	private static final int GUTSCHEIN_KONTO = 1371;

	public static List<LedgerEntry> convertBalanceToLedger(PosInvoice inv) {
		if (inv == null || inv.getAmount() == null || inv.getAmount().longValue() == 0L) {
			return Collections.emptyList();
		}
		
		LedgerEntry entry = new LedgerEntry();
		entry.setSoll(true);
		entry.setNumber(inv.getNumber());
		entry.setDate(inv.getDate());

		Booking soll = new Booking();
		soll.setAccount(getDebitAccount(inv)); // das debitorenkonto
		soll.setBetrag(inv.getAmount());
		soll.setText("Rg " + inv.getNumber() + "("+inv.getName1()+")");
		soll.setDate(inv.getDate().toDateTimeAtStartOfDay());
		entry.add(soll);
		if (inv.getAmountNone() != null && inv.getAmountNone().longValue() != 0L) {
			Booking haben = new Booking();
			haben.setBetrag(inv.getAmountNone().longValue());
			haben.setAccount(GUTSCHEIN_KONTO); // Erlöse ohne Steuer gehen meist aufs Gutscheinkonto
			haben.setText("Gutscheine");
			entry.add(haben);
		}
		if (inv.getAmountHalf() != null && inv.getAmountHalf().longValue() != 0L) {
			Booking haben = new Booking();
			haben.setBetrag(inv.getAmountHalf().longValue());
			haben.setAccount(BASIS_ERLOES_7 + inv.getPointid());
			haben.setText("Erlös 7%");
			entry.add(haben);
		}
		if (inv.getAmountFull() != null && inv.getAmountFull().longValue() != 0L) {
			Booking haben = new Booking();
			haben.setBetrag(inv.getAmountFull().longValue());
			haben.setAccount(BASIS_ERLOES_19 + inv.getPointid());
			haben.setText("Erlös 19%");
			entry.add(haben);
		}
		
		return Arrays.asList(entry);
	}
	
	public static int getDebitAccount(PosInvoice inv) {
		if (inv.getDebitorId() > 0) {
			return inv.getDebitorId();
		}
		return 10000;
	}
	
}

