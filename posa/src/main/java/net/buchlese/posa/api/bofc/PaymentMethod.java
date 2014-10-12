package net.buchlese.posa.api.bofc;

public enum PaymentMethod {
	CASH("c"),
	TELE("t"),
	WHAT("w");
	
	private final String dbKey;
	PaymentMethod(String key) {
		this.dbKey = key;
	}
	
	public String getDbKey() {
		return dbKey;
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
