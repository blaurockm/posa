package net.buchlese.posa.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

import org.joda.time.DateTime;

public abstract class AbstractSynchronizer {

	protected boolean updMoney(Consumer<Long> c, Long val1, BigDecimal val2) {
		if (val2 == null && val1 != null) {
			c.accept(0l);
			return true;
		}
		if (val2 == null) {
			// wir haben keinen Wert und 0 steht schon drin, also nix machen...
			return false;
		}
		// wir runden hier, obwohl das eigentlich nicht nötig sein sollte, aber float in der DB ist unberechenbar
		long v = val2.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValue();
		if (val1 == null || val1.longValue() != v) {
			c.accept(v);
			return true;
		}
		return false;
	}

	protected boolean updStr(Consumer<String> l, String val1, String val2) {
		if (val1 == null && val2 != null ) {
			l.accept(val2);
			return true;
		}
		if (val1 != null && val2 == null ) {
			l.accept(val2);
			return true;
		}
		if (val1 != null && val2 != null && val1.equals(val2) == false) {
			l.accept(val2);
			return true;
		}
		return false;
	}

	protected boolean updInt(Consumer<Integer> c, Integer val1, BigDecimal val2) {
		if (val2 == null && val1 != null) {
			c.accept(0);
			return true;
		}
		if (val2 == null) {
			return false;
		}
		int v = val2.intValue();
		if (val1 == null || val1.intValue() != v) {
			c.accept(v);
			return true;
		}
		return false;
	}

	protected boolean updLong(Consumer<Long> c, Long val1, BigDecimal val2) {
		if (val2 == null && val1 != null) {
			c.accept(0L);
			return true;
		}
		if (val2 == null) {
			return false;
		}
		long v = val2.longValue();
		if (val1 == null || val1.longValue() != v) {
			c.accept(v);
			return true;
		}
		return false;
	}

	protected boolean updBool(Consumer<Boolean> c, Boolean val1, Boolean val2) {
		if (val1 == null && val2 != null ) {
			c.accept(val2);
			return true;
		}
		if (val1 != null && val2 == null ) {
			c.accept(Boolean.FALSE);
			return true;
		}
		if (val1 != null && val2 != null && val1.equals(val2) == false) {
			c.accept(val2);
			return true;
		}
		return false;
	}

	protected <R> boolean updEnum(Consumer<R> c, R val1, R val2) {
		if (val1 == null && val2 != null ) {
			c.accept(val2);
			return true;
		}
		if (val1 != null && val2 == null ) {
			c.accept(val2);
			return true;
		}
		if (val1 != null && val2 != null && val1.equals(val2) == false) {
			c.accept(val2);
			return true;
		}
		return false;
	}

	protected boolean updDate(Consumer<DateTime> c, DateTime val1, DateTime val2) {
		if (val1 == null && val2 != null ) {
			c.accept(val2);
			return true;
		}
		if (val1 != null && val2 == null ) {
			c.accept(val2);
			return true;
		}
		if (val1 != null && val2 != null && val1.equals(val2) == false) {
			c.accept(val2);
			return true;
		}
		return false;
		
	}

}
