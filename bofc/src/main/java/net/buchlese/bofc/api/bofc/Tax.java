package net.buchlese.bofc.api.bofc;

public enum Tax {
	FULL("f", "19%", 4400),
	HALF("h", "7%", 4300),
	NONE("0", "0%", 4200); // gutschein verkauft
	
	private String dbKey;
	private String accText;
	private int account;
	Tax(String key, String accTxt, int acc) {
		this.dbKey = key;
		this.account = acc;
		this.accText = accTxt;
	}
	
	public String getDbKey() {
		return dbKey;
	}

	public String getAccountText() {
		return accText;
	}

	public int getAccount() {
		return account;
	}

	public static Tax fromDbKey(String dbKey) {
		if (dbKey == null) return null;
		switch (dbKey) {
		case "f" : return FULL;
		case "h" : return HALF;
		case "0" : return NONE;
		default:return null;
		}
	}

	public static Tax mappingFrom(Character mwst) {
		if (mwst == null) {
			return null;
		}
		switch (mwst) {
		case '1' : return FULL;
		case '2' : return HALF;
		case '3' : return NONE; // es wurde was verkauft mit 0%
		default : return null;
		}
	}
	
}
