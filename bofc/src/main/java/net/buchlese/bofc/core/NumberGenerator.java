package net.buchlese.bofc.core;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.util.LongMapper;

public class NumberGenerator {

	private final DBI database;
	private static long number = 3169000;
	
	public NumberGenerator(DBI posDBI) {
		this.database = posDBI;
		Handle h = database.open();
		h.execute("create sequence if not exists CUSTOMERNO start with 100");
		h.execute("create sequence if not exists INVOICENO start with 0");
		h.execute("create sequence if not exists INDEPENDENTNO start with 555");
		h.close();
	}

	public static synchronized String createNumber(int pointid) {
		return String.valueOf(number++);
	}

	public int getNextCustomerNumber(int pointid) {
		Handle h = database.open();
		Query<Map<String, Object>> q = h.createQuery("call nextval('CUSTOMERNO')");
		Query<Long> q2 = q.map(LongMapper.FIRST);
		long num = q2.first();
		h.close();
		return pointid * 100000 + (int) num;
	}

	public String getNextInvoiceNumber(int pointid) {
		Handle h = database.open();
		Query<Map<String, Object>> q = h.createQuery("call nextval('INVOICENO')");
		Query<Long> q2 = q.map(LongMapper.FIRST);
		long num = q2.first();
		h.close();
		String prefix = String.valueOf(pointid) + LocalDate.now().toString("yy") + "9";
		return prefix + StringUtils.leftPad(String.valueOf(num), 4, '0');
	}

	public long getNextNumber() {
		Handle h = database.open();
		Query<Map<String, Object>> q = h.createQuery("call nextval('INDEPENDENTNO')");
		Query<Long> q2 = q.map(LongMapper.FIRST);
		long num = q2.first();
		h.close();
		return num;
	}

}
