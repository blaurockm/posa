package net.buchlese.bofc.core;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import net.buchlese.bofc.api.bofc.SequenceGen;
import net.buchlese.bofc.jpa.JpaSequenceGenDAO;

public class NumberGenerator {

	private final JpaSequenceGenDAO dao;
	
	public NumberGenerator(JpaSequenceGenDAO dao) {
		this.dao = dao;
	}

	public int getNextCustomerNumber(int pointid) {
		Long num = getNextNumber("CUST");
		return (int) ((int) pointid * 100000 + num);
	}

	public String getNextInvoiceNumber(int pointid) {
		Long num = getNextNumber("INVOICE");
		String prefix = String.valueOf(pointid) + LocalDate.now().toString("yy") + "9";
		return prefix + StringUtils.leftPad(String.valueOf(num), 4, '0');
	}

	public long getNextNumber() {
		Long num = getNextNumber("DELIVNOTE");
		return num;
	}
	
	public void initSequence(String key, long start) {
		SequenceGen gen = getCurrentActiveSequence(key);
		if (gen != null) {
			return;
		}
		gen = new SequenceGen();
		gen.setSeqKey(key);
		gen.setStart(start);
		gen.setCurrent(start);
		gen.setValidFrom(java.time.LocalDate.now());
		dao.create(gen);
	}

	public synchronized Long getNextNumber(String key) {
		SequenceGen gen = getCurrentActiveSequence(key);
		if ( gen != null) {
			Long next = gen.getCurrent() + 1;
			gen.setCurrent(next);
			dao.update(gen);
			return next;
		}
		initSequence(key, 0);
		return 0L;
	}

	private SequenceGen getCurrentActiveSequence(String key) {
		return dao.findByKey(key);
	}
	
	
	
}
