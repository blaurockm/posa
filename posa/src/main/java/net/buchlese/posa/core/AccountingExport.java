package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.PaymentMethod;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.Tax;

public class AccountingExport {

	public static int kassenKonto = 1600;
	public static String kostenStelle = "Dornhan";
	
//	Belegdatum;Buchungstext;Buchungsbetrag;Sollkonto;Habenkonto;Kostenstelle
//	04.10.2014;Einnahmen 04.10.;289.07;1600;0;
//	;Warenausgang 19% 04.10.;34.58;;4400;Dornhan
//	;Warenausgang 7% 04.10.;271.48;;4300;Dornhan
//	;Gutscheine 04.10.;2.91;;1371;
//	;Gutscheine Sulz 04.10.;-19.9;;1373;
//	04.10.2014;Entnahme Transit;284.69;0;1600;
//	;Telecash 04.10.;59.69;1461;;
//	;Barentnahme 04.10.;225.0;1460;;
//	04.10.2014;Eingel Gutscheine 04.10.;2.91;1371;1600;

	public static String accountingExport(PosCashBalance bal) {
		StringBuilder sb = new StringBuilder();
		// den Datumstring...
		
		String dateShort = bal.getLastCovered().toString("dd.MM.");
		
		
		// zuerst die Einnahmen
		List<Booking> einnahmen = new ArrayList<>();
		long ges = 0;
		
		Booking soll = new Booking();
		soll.setAccount(kassenKonto);
		soll.setDate(bal.getLastCovered());
		soll.setText("Einnahmen " + dateShort);
		soll.setBetrag(bal.getGoodsOut());
		soll.setCode(null);
		einnahmen.add(soll);
		
		// jetzt die einzelnen MWSt-Sätze
		for (Map.Entry<Tax, Long> entry : bal.getTaxBalance().entrySet()) {
			Booking taxEntry = new Booking();
			taxEntry.setAccount(entry.getKey().getAccount());
			taxEntry.setBetrag(entry.getValue());
			taxEntry.setText("Warenausgang " + entry.getKey().getAccountText() + " " + dateShort);
			taxEntry.setCode(kostenStelle);
			ges += entry.getValue();
			einnahmen.add(taxEntry);
		}
		
		// jetzt die Gutschein werte
		for (Map.Entry<String, Long> entry : bal.getNewCoupon().entrySet()) {
			Booking couponEntry = new Booking();
			ArticleGroup grp = ArticleGroup.getArticleGroups().get(entry.getKey());
			if (grp == null || grp.getAccount() == null) {
				continue;
			}
			couponEntry.setAccount(grp.getAccount());
			couponEntry.setBetrag(entry.getValue());
			couponEntry.setText(grp.getText() + " " + dateShort);
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
			ges += entry.getValue();
			einnahmen.add(grpEntry);
		}

		soll.setBetrag(ges);
		sb.append(convertBookingsToCSV(einnahmen, true));

		// jetzt die Ausgaben
		List<Booking> ausgaben = new ArrayList<>();
		ges = 0;
		
		soll = new Booking();
		soll.setAccount(kassenKonto);
		soll.setDate(bal.getLastCovered());
		soll.setText("Kassenausgang " + dateShort);
		soll.setBetrag(bal.getGoodsOut());
		soll.setCode(null);
		ausgaben.add(soll);
		for (Map.Entry<PaymentMethod, Long> entry : bal.getPaymentMethodBalance().entrySet()) {
			Booking couponEntry = new Booking();
			couponEntry.setAccount(entry.getKey().getAccount());
			couponEntry.setBetrag(entry.getValue());
			couponEntry.setText(entry.getKey().getAccountText() + " " + dateShort);
			ges += entry.getValue();
			ausgaben.add(couponEntry);
		}
		// jetzt die Gutschein werte
		for (Map.Entry<String, Long> entry : bal.getOldCoupon().entrySet()) {
			Booking couponEntry = new Booking();
			ArticleGroup grp = ArticleGroup.getArticleGroups().get(entry.getKey()); 
			if (grp == null || grp.getAccount() == null) {
				continue;
			}
			couponEntry.setAccount(grp.getAccount());
			couponEntry.setBetrag(entry.getValue());
			couponEntry.setText(grp.getText() + " " + dateShort);
			ges += entry.getValue();
			ausgaben.add(couponEntry);
		}

		soll.setBetrag(ges);
		sb.append(convertBookingsToCSV(ausgaben, false));

		return sb.toString();
	}
	
	
	private static String convertBookingsToCSV(List<Booking> bookings, boolean soll) {
		String dateLong = bookings.get(0).getDate().toString("dd.MM.yyyy");
		StringBuilder sb = new StringBuilder();
		if (bookings.size() > 2) {
			// wir haben ein split-booking
			sb.append(dateLong).append(";").append(bookings.get(0).getText()).append(";");
			sb.append(new BigDecimal(bookings.get(0).getBetrag()).movePointLeft(2).toPlainString()).append(";");
			if (soll == false) {
				sb.append("0;"); // ein haben split booking
			}
			sb.append(bookings.get(0).getAccount());
			if (soll == true) {
				sb.append(";0;"); // ein soll split booking
			}
			sb.append("\n");
			for (int i = 1; i < bookings.size(); i++) {
				sb.append(";").append(bookings.get(i).getText()).append(";");
				sb.append(new BigDecimal(bookings.get(i).getBetrag()).movePointLeft(2).toPlainString()).append(";");
				if (soll == true) {
					sb.append(";");
				}
				sb.append(bookings.get(i).getAccount()).append(";");
				if (soll == false) {
					sb.append(";");
				}
				sb.append(bookings.get(i).getCode()).append("\n");
			}
		} else {
			// es ist ein einfaches booking;
			sb.append(dateLong).append(";").append(bookings.get(0).getText()).append(";");
			sb.append(new BigDecimal(bookings.get(0).getBetrag()).movePointLeft(2).toPlainString()).append(";");
			if (soll == true) {
				sb.append(bookings.get(0).getAccount()).append(";");
				sb.append(bookings.get(1).getAccount()).append(";");
			} else {
				sb.append(bookings.get(1).getAccount()).append(";");
				sb.append(bookings.get(0).getAccount()).append(";");
			}
			sb.append(bookings.get(1).getCode()).append("\n");
		}
		
		return sb.toString();
	}


	private static class Booking {
		private int account;
		private long betrag;
		private String text;
		private DateTime date;
		private String code;
		public int getAccount() {
			return account;
		}
		public void setAccount(int account) {
			this.account = account;
		}
		public long getBetrag() {
			return betrag;
		}
		public void setBetrag(long betrag) {
			this.betrag = betrag;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public DateTime getDate() {
			return date;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public void setDate(DateTime date) {
			this.date = date;
		}
	}
	
	
	
}
