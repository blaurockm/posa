package net.buchlese.bofc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import net.buchlese.bofc.api.bofc.AccrualDay;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;

public class DayBalance {
	private final PosCashBalanceDAO balanceDao;
	private final Map<Integer, PosCashBalance> cache;
	
	public DayBalance(PosCashBalanceDAO balanceDao) {
		this.balanceDao = balanceDao;
		this.cache = new HashMap<>();
	}

	
	/**
	 * 
	 * @param dateTime wenn = null, dann die aus dem Speicher
	 * @return
	 */
	public AccrualDay computeBalance(String date) {
		AccrualDay ad = new AccrualDay();

		if (date == null) {
			ad.setDay(new DateTime());
			BalanceComputer.computeBalance(ad, new ArrayList<>(cache.values()));
			
			return ad;
		} else {
			ad.setDay(DateTime.parse(date, DateTimeFormat.forPattern("yyyyMMdd")));
			List<PosCashBalance> l = balanceDao.fetchForDate(date);
			BalanceComputer.computeBalance(ad, l);
			
			return ad;
		}
	}

	public void accept(PosCashBalance cashBal) {
		cache.put(cashBal.getPointid(), cashBal);
	}

}
