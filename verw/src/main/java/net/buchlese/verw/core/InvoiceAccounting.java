package net.buchlese.verw.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.verw.core.accounting.Booking;
import net.buchlese.verw.core.accounting.LedgerEntry;

@Component
public class InvoiceAccounting {

	private static final int BASIS_ERLOES_19 = 4400;
	private static final int BASIS_ERLOES_7 = 4300;
	private static final int GUTSCHEIN_KONTO = 1371;

	public  List<LedgerEntry> convertInvoiceToLedger(PosInvoice inv) {
		if (inv == null || inv.getAmount() == null || inv.getAmount().longValue() == 0L) {
			return Collections.emptyList();
		}
		
		LedgerEntry entry = new LedgerEntry();
		entry.setSoll(true);
		entry.setNumber(inv.getNumber());
		entry.setDate(inv.getDate().toLocalDate());

		Booking soll = new Booking();
		soll.setAccount(getDebitAccount(inv)); // das debitorenkonto
		soll.setBetrag(inv.getAmount());
		soll.setText("Rg " + inv.getNumber() + "("+ inv.getCustomerId() + "," +inv.getName1()+")");
		soll.setDate(inv.getDate().toLocalDate());
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
	
	public  int getDebitAccount(PosInvoice inv) {
		if (inv.getDebitorId() > 0) {
			return inv.getDebitorId();
		}
		return 10000;
	}
	
}

