package net.buchlese.posa.api.bofc;

public enum PaymentMethod {
	CASH("c", "Bar", 1460),
	TELE("t", "EC", 1461),
	WHAT("w", "WTF?", 1370);
	
	private final String dbKey;
	private final String accTxt;
	private final int account;
	
	PaymentMethod(String key, String accTxt, int acc) {
		this.dbKey = key;
		this.accTxt = accTxt;
		this.account = acc;
	}
	
	public String getDbKey() {
		return dbKey;
	}

	public String getAccountText() {
		return accTxt;
	}

	public int getAccount() {
		return account;
	}

	public static PaymentMethod fromDbKey(String dbKey) {
		if (dbKey == null) return null;
		switch (dbKey) {
		case "c" : return CASH;
		case "t" : return TELE;
		default:return WHAT;
		}
	}

	public static PaymentMethod mappingFrom(int posKey) {
		switch (posKey) {
		case 0 : return CASH;
		case 3 : return TELE;
		case 7 : return CASH;  // in cash bezahlte rechnungen
		default:return WHAT;
		}
	}
}
