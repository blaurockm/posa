package net.buchlese.posa.api.bofc;

public enum Tax {
	FULL("f"),
	HALF("h"),
	NONE("0"); // gutschein verkauft
	
	private String dbKey;
	Tax(String key) {
		this.dbKey = key;
	}
	
	public String getDbKey() {
		return dbKey;
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
