package net.buchlese.bofc.core;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
	private DateUtils() {
		
	}
	
	public static java.sql.Date minusOne(java.sql.Date x) {
		if (x == null) {
			x = now();
		}
		return java.sql.Date.valueOf(x.toLocalDate().minusDays(1));
	}
	
	public static java.sql.Date parse(String x) {
		try {
			return new java.sql.Date( new SimpleDateFormat("dd.MM.yyyy").parse(x).getTime());
		} catch (ParseException e) {
			return now();
		}
	}

	public static Date now() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	public static Date parseMonth(String x) {
		try {
			return new java.sql.Date( new SimpleDateFormat("MM/yyyy").parse(x).getTime());
		} catch (ParseException e) {
			return now();
		}
	}

	public static String format(Date date) {
		if ( date == null) {
			date = now();
		}
		return new SimpleDateFormat("dd.MM.yyyy").format(date);
	}

	public static Timestamp nowTime() {
		return new java.sql.Timestamp(System.currentTimeMillis());
	}
	
	
}

