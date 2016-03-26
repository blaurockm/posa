package net.buchlese.bofc.core.accounting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.buchlese.bofc.api.bofc.ArticleGroup;
import net.buchlese.bofc.api.bofc.PaymentMethod;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.Tax;

/**
 * erzeugt aus einer CashBalance einträge für die Buchhaltung.
 * 
 * @author Markus Blaurock
 *
 */
public class BalanceAccounting {

	private static final int KASSDIFF_KONTO = 7400;
	private static final int GUTSCHEIN_KONTO = 1371;

	public static List<LedgerEntry> convertBalanceToLedger(PosCashBalance bal) {
		List<LedgerEntry> res = new ArrayList<LedgerEntry>();
		// den Datumstring...
		String dateShort = bal.getLastCovered().toString("dd.MM.");
		String entryNum = String.valueOf(bal.getPointid()) + "-" + bal.getLastCovered().toString("yyyyMMdd") +".";
		
		
		// zuerst die Einnahmen
		LedgerEntry einnahmen = createEinnahmenEntry(bal, dateShort);
		einnahmen.setNumber(entryNum+"E");
		res.add(einnahmen);
	
		// jetzt die Ausgaben
		LedgerEntry ausgaben = createAusgabenEntry(bal, dateShort);
		ausgaben.setNumber(entryNum+"A");
		res.add(ausgaben);
	
		// und jetzt die Kassendifferenz.
		if (bal.getCashDiff() != 0) {
			// natürlich nur, wenn es eine gibt..
			LedgerEntry kassdiff = createKassDiffEntry(bal, dateShort);
			kassdiff.setNumber(entryNum+"D");
			res.add(kassdiff);
		}
		
		return res;
	}

	private static LedgerEntry createKassDiffEntry(PosCashBalance bal, String dateShort) {
		LedgerEntry kassdiff = new LedgerEntry();
		kassdiff.setSoll(true);
		Booking soll = new Booking();
		soll.setAccount(getKassenkonto(bal));
		soll.setDate(bal.getLastCovered());
		soll.setText("Kassendifferenz " + dateShort);
		soll.setBetrag(bal.getCashDiff());
		soll.setCode(null);
		kassdiff.add(soll);
		Booking haben = new Booking();
		haben.setAccount(KASSDIFF_KONTO);		
		haben.setCode(null);
		kassdiff.add(haben);
		return kassdiff;
	}

	private static LedgerEntry createAusgabenEntry(PosCashBalance bal,	String dateShort) {
		LedgerEntry ausgaben = new LedgerEntry();
		ausgaben.setSoll(false);
		long ges = 0;
		
		Booking soll = new Booking();
		soll.setAccount(getKassenkonto(bal));
		soll.setDate(bal.getLastCovered());
		soll.setText("Kassenausgang " + dateShort);
		soll.setBetrag(bal.getGoodsOut());
		soll.setCode(null);
		ausgaben.add(soll);
		if (bal.getPaymentMethodBalance().get(PaymentMethod.TELE) != null) {
			Booking couponEntry = new Booking();
			couponEntry.setAccount(PaymentMethod.TELE.getAccount());
			couponEntry.setBetrag(bal.getPaymentMethodBalance().get(PaymentMethod.TELE));
			couponEntry.setText(PaymentMethod.TELE.getAccountText() + " " + dateShort);
			ges += bal.getPaymentMethodBalance().get(PaymentMethod.TELE);
			ausgaben.add(couponEntry);
		}
		Booking cashAbsorpEntry = new Booking();
		cashAbsorpEntry.setAccount(PaymentMethod.CASH.getAccount()+bal.getPointid());
		cashAbsorpEntry.setBetrag(bal.getAbsorption());
		cashAbsorpEntry.setText(PaymentMethod.CASH.getAccountText() + " " + dateShort);
		ges += bal.getAbsorption();
		ausgaben.add(cashAbsorpEntry);
		// jetzt die Gutschein werte
		for (Map.Entry<String, Long> entry : bal.getOldCoupon().entrySet()) {
			Booking couponEntry = new Booking();
			ArticleGroup grp = ArticleGroup.getArticleGroups().get(entry.getKey());
			couponEntry.setBetrag(-entry.getValue());
			if (grp != null && grp.getAccount() != null) {
				couponEntry.setAccount(grp.getAccount());
				couponEntry.setText("Eingelöst " + grp.getText() + " " + dateShort);
			} else {
				couponEntry.setAccount(GUTSCHEIN_KONTO);
				couponEntry.setText("Eingelöst Gutsch " + dateShort);
			}
			ges += -entry.getValue();
			ausgaben.add(couponEntry);
		}
	
		soll.setBetrag(ges);
		return ausgaben;
	}

	private static LedgerEntry createEinnahmenEntry(PosCashBalance bal,	String dateShort) {
		LedgerEntry einnahmen = new LedgerEntry(); 
		einnahmen.setSoll(true);
		long ges = 0;
		
		Booking soll = new Booking();
		soll.setAccount(getKassenkonto(bal));
		soll.setDate(bal.getLastCovered());
		soll.setText("Einnahmen " + dateShort);
		soll.setBetrag(bal.getGoodsOut());
		soll.setCode(null);
		einnahmen.add(soll);
		
		// jetzt die einzelnen MWSt-Sätze
		for (Map.Entry<Tax, Long> entry : bal.getTaxBalance().entrySet()) {
			if (entry.getKey().equals(Tax.NONE)) {
				// 0% wollen wir aufteilen
				continue;
			}
			Booking taxEntry = new Booking();
			taxEntry.setAccount(entry.getKey().getAccount() + bal.getPointid());
			taxEntry.setBetrag(entry.getValue());
			taxEntry.setText("Warenausgang " + entry.getKey().getAccountText() + " " + dateShort);
			taxEntry.setCode(getKostenStelle(bal));
			ges += entry.getValue();
			einnahmen.add(taxEntry);
		}
		
		// jetzt die Gutschein werte
		for (Map.Entry<String, Long> entry : bal.getNewCoupon().entrySet()) {
			Booking couponEntry = new Booking();
			ArticleGroup grp = ArticleGroup.getArticleGroups().get(entry.getKey());
			couponEntry.setBetrag(entry.getValue());
			if (grp != null && grp.getAccount() != null) {
				couponEntry.setAccount(grp.getAccount());
				couponEntry.setText(grp.getText() + " " + dateShort);
			} else {
				couponEntry.setAccount(GUTSCHEIN_KONTO);
				couponEntry.setText("Gutsch " + dateShort);
			}
			couponEntry.setCode(null);
			ges += entry.getValue();
			einnahmen.add(couponEntry);
		}
	
		// jetzt die Artikelgruppen mit eigener Buchungseinheit
		for (Map.Entry<String, Long> entry : bal.getArticleGroupBalance().entrySet()) {
			Booking grpEntry = new Booking();
			ArticleGroup grp = ArticleGroup.getArticleGroups().get(entry.getKey()); 
			if (grp == null || grp.getAccount() == null) {
				continue;
			}
			grpEntry.setAccount(grp.getAccount());
			grpEntry.setBetrag(entry.getValue());
			grpEntry.setText(grp.getText() + " " + dateShort);
			grpEntry.setCode(null);
			ges += entry.getValue();
			einnahmen.add(grpEntry);
		}
	
		soll.setBetrag(ges);
		return einnahmen;
	}

	private static int getKassenkonto(int pointid) {
		switch(pointid) {
		case 1 : return 1600;
		case 2 : return 1610;
		case 3 : return 1620;
		default: return 1370;
		}
	}

	private static int getKassenkonto(PosCashBalance bal) {
		return getKassenkonto(bal.getPointid());
	}

	private static String getKostenStelle(PosCashBalance bal) {
		return "";
	}

}
