package net.buchlese.verw.core;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.buchlese.bofc.api.bofc.AccountingBalanceExport;
import net.buchlese.bofc.api.bofc.AccountingInvoiceExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.verw.core.accounting.LedgerEntry;

/**
 * exportiert die LedgerEntries im Format passend für Lexware.
 * 
 * @author Markus Blaurock
 *
 */
@Component
public class AccountingExportFile {
	
	@Autowired BalanceAccounting balanceAccounting;
	@Autowired InvoiceAccounting invoiceAccounting;
	private static final Logger log = LoggerFactory.getLogger(AccountingExportFile.class);
	
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

	public void createFile(AccountingBalanceExport ae, Writer writer) throws IOException {
		writer.write(accountingExportHeader());
		for (PosCashBalance bal :  ae.getBalances()) {
			try {
				List<LedgerEntry> e = balanceAccounting.convertBalanceToLedger(bal);
				writer.write(accountingExport(e));
			} catch (Exception e) {
				writer.write("\n\n\nproblem creating cashBalance " + e.toString() + "\n\n\n\n");
				log.error("problem writing cashBalance to exportFile", e);
			}
		}
	}

	public void createFile(AccountingInvoiceExport ae, Writer writer) throws IOException {
		writer.write(accountingExportHeader());
		for (PosInvoice inv :  ae.getInvoices()) {
			try {
				List<LedgerEntry> e = invoiceAccounting.convertInvoiceToLedger(inv);
				writer.write(accountingExport(e));
			} catch (Exception e) {
				writer.write("\n\n\nproblem creating cashBalance " + e.toString() + "\n\n\n\n");
			}
		}
	}

	
	public String accountingExportHeader() {
		return "Belegdatum;Belegnummer;Buchungstext;Buchungsbetrag;Sollkonto;Habenkonto;Kostenstelle\r\n";
	}

	public String accountingExport(List<LedgerEntry> entries) {
		StringBuilder sb = new StringBuilder();
		entries.forEach(e -> sb.append(convertBookingsToCSV(e)));
		return sb.toString();
	}

	/**
	 * erzeugt aus einer List mit Buchungen einen CSV-String.
	 * Wenn es mehr als 2 Buchungen sind, wird eine Split-Buchung erzeugt, aonsten eine normale Soll-Haben Buchung
	 * 
	 * @param bookings
	 * @param soll
	 * @return String in CSV-Format
	 */
	private String convertBookingsToCSV(LedgerEntry entry) {
		String dateLong = entry.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")); // oder das aus dem entry
		String number = entry.getNumber();
		
		StringBuilder sb = new StringBuilder();
		if (entry.getBookings().size() > 2) {
			// wir haben ein split-booking
			sb.append(dateLong).append(";");
			sb.append(number).append(";");
			sb.append(entry.getBookings().get(0).getText()).append(";");
			sb.append(new BigDecimal(entry.getBookings().get(0).getBetrag()).movePointLeft(2).toPlainString()).append(";");
			if (entry.isSoll() == false) {
				sb.append("0;"); // ein haben split booking
			}
			sb.append(entry.getBookings().get(0).getAccount());
			if (entry.isSoll() == true) {
				sb.append(";0;"); // ein entry.isSoll() split booking
			}
			sb.append("\r\n");
			for (int i = 1; i < entry.getBookings().size(); i++) {
				if (entry.getBookings().get(i).getBetrag() == 0) {
					continue;
				}
				sb.append(";");
				sb.append(";");
				sb.append(entry.getBookings().get(i).getText()).append(";");
				sb.append(new BigDecimal(entry.getBookings().get(i).getBetrag()).movePointLeft(2).toPlainString()).append(";");
				if (entry.isSoll() == true) {
					sb.append(";");
				}
				sb.append(entry.getBookings().get(i).getAccount()).append(";");
				if (entry.isSoll() == false) {
					sb.append(";");
				}
				if (entry.getBookings().get(i).getCode() != null) {
					sb.append(entry.getBookings().get(i).getCode());
				}
				sb.append("\r\n");
			}
		} else {
			// es ist ein einfaches booking;
			sb.append(dateLong).append(";");
			sb.append(number).append(";");
			sb.append(entry.getBookings().get(0).getText()).append(";");
			sb.append(new BigDecimal(entry.getBookings().get(0).getBetrag()).movePointLeft(2).toPlainString()).append(";");
			if (entry.isSoll() == true) {
				sb.append(entry.getBookings().get(0).getAccount()).append(";");
				sb.append(entry.getBookings().get(1).getAccount()).append(";");
			} else {
				sb.append(entry.getBookings().get(1).getAccount()).append(";");
				sb.append(entry.getBookings().get(0).getAccount()).append(";");
			}
			if (entry.getBookings().get(1).getCode() != null) {
				sb.append(entry.getBookings().get(1).getCode());
			}
			sb.append("\r\n");
		}
		
		return sb.toString();
	}
	
}
