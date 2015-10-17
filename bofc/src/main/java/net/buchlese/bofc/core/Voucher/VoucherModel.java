package net.buchlese.bofc.core.Voucher;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;

import com.google.inject.Inject;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class VoucherModel {

	@Inject private PosCashBalanceDAO dao;

	private List<PosCashBalance> vouchers;

	/**
	 * gibt einen Container für die gerade gewählten Belege zurück
	 * @return
	 */
	public Container getContainer() {
		if (vouchers != null) {
			return createContainer(vouchers);
		}
		return createContainer(Collections.emptyList());
	}

	public String getSum(int pointid) {

		long rev = vouchers.stream().filter(x -> x.getPointid() == pointid).mapToLong(x -> x.getRevenue()).sum();
		long abs = vouchers.stream().filter(x -> x.getPointid() == pointid).mapToLong(x -> x.getAbsorption()).sum();

        NumberFormat currFormat = DecimalFormat.getCurrencyInstance();
		return currFormat.format(rev /100d ) + " / " +	currFormat.format(abs /100d );
	}
	
	
	/**
	 * gibt die aktuelle Woche zurück.
	 * @return
	 */
	public void selectCurrentWeek() {
		// die Aktuelle Woche
		LocalDate now = LocalDate.now().minusDays(1);
		LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // den letzten Montag
		LocalDate sunday = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); // den nächsten Sonntag

		DateTimeFormatter abschlussidformat = DateTimeFormatter.ofPattern("yyyyMMdd");
		vouchers = dao.fetchAllAfter(monday.format(abschlussidformat), sunday.format(abschlussidformat));
	}

	@SuppressWarnings("unchecked")
	private Container createContainer(List<PosCashBalance> vs) {
		Container co = new IndexedContainer();

		co.addContainerProperty("Wochentag", String.class, null);
		co.addContainerProperty("Datum", LocalDate.class, null);
		co.addContainerProperty("Kasse Sulz", PosCashBalance.class, null);
		co.addContainerProperty("Kasse Dornhan",  PosCashBalance.class, null);

		Optional<LocalDate> firstDay = vs.stream().map(x -> x.getFirstCovered()).min(org.joda.time.DateTimeComparator.getInstance()).map(x -> LocalDate.of(x.getYear(),x.getMonthOfYear(), x.getDayOfMonth()));
		Optional<LocalDate> lastDay = vs.stream().map(x -> x.getLastCovered()).max(org.joda.time.DateTimeComparator.getInstance()).map(x -> LocalDate.of(x.getYear(),x.getMonthOfYear(), x.getDayOfMonth()));

		DateTimeFormatter abschlussidformat = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (firstDay.isPresent() && lastDay.isPresent()) {
			LocalDate loopDate = firstDay.get();
			while (loopDate.compareTo(lastDay.get()) <= 0) {
				final Item tag = co.getItem(co.addItem());
				final String abschlussid = loopDate.format(abschlussidformat);
				tag.getItemProperty("Datum").setValue(loopDate);
				tag.getItemProperty("Wochentag").setValue(loopDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY));
				tag.getItemProperty("Kasse Sulz").setValue(vs.stream().filter(x -> x.getPointid() == 2 && x.getAbschlussId().equals(abschlussid)).findFirst().orElse(null));
				tag.getItemProperty("Kasse Dornhan").setValue(vs.stream().filter(x -> x.getPointid() == 1 && x.getAbschlussId().equals(abschlussid)).findFirst().orElse(null));
				loopDate = loopDate.plusDays(1);
			}
		}		
		return co;
	}

}
