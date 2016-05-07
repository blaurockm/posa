package net.buchlese.bofc.core;

public class NumberGenerator {

	private static long number = 3169000;
	
	public static synchronized String createNumber(int pointid) {
		return String.valueOf(number++);
	}

}
