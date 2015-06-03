package net.buchlese.bofc.core.Voucher;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class VoucherModel {

	private PosCashBalanceDAO dao;
	
	public VoucherModel(PosCashBalanceDAO posTxDao) {
		this.dao = posTxDao; 
	}

	/**
	 * gibt die aktuelle Woche zurück.
	 * @return
	 */
	public Container getDefaultContainer() {
		
		Container co = new IndexedContainer();
		
		co.addContainerProperty("Wochentag", String.class, null);
		co.addContainerProperty("Datum", LocalDate.class, null);
		co.addContainerProperty("Kasse Sulz", PosCashBalance.class, null);
		co.addContainerProperty("Kasse Dornhan",  PosCashBalance.class, null);

		// die Aktuelle Woche
		LocalDate now = LocalDate.now().minusDays(1);
		LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // den letzten Montag
		LocalDate sunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); // den nächsten Sonntag
		
		DateTimeFormatter abschlussidformat = DateTimeFormatter.ofPattern("yyyyMMdd");
		List<PosCashBalance> vouchers = dao.fetchAllAfter(monday.format(abschlussidformat), sunday.format(abschlussidformat));
		
		LocalDate loopDate = monday;
		while (loopDate.compareTo(sunday) <= 0) {
			final Item tag = co.getItem(co.addItem());
			final String abschlussid = loopDate.format(abschlussidformat);
			tag.getItemProperty("Datum").setValue(loopDate);
			tag.getItemProperty("Wochentag").setValue(loopDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY));
			tag.getItemProperty("Kasse Sulz").setValue(vouchers.stream().filter(x -> x.getPointid() == 2 && x.getAbschlussId().equals(abschlussid)).findFirst().orElse(null));
			tag.getItemProperty("Kasse Dornhan").setValue(vouchers.stream().filter(x -> x.getPointid() == 1 && x.getAbschlussId().equals(abschlussid)).findFirst().orElse(null));
			loopDate = loopDate.plusDays(1);
		}
		
		return co;
	}

}
