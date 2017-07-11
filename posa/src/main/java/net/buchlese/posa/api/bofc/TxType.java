package net.buchlese.posa.api.bofc;

public enum TxType {
	SELL("w+"),  // good sold
	CASHOUT("c-"),   // cash given away
	CASHIN("c+"),   // cash received
	DEBITPAY("i+"),   // invoice payed by debitor
	TRADEIN("t-"),  // gutschein o.ä. angenommen
	TRADEOUT("t+"); // gutschein verkauft
	
	private String dbKey;
	TxType(String key) {
		this.dbKey = key;
	}
	
	public String getDbKey() {
		return dbKey;
	}

	public static TxType fromDbKey(String dbKey) {
		if (dbKey == null) return null;
		switch (dbKey) {
		case "w+" : return SELL;
		case "c-" : return CASHOUT;
		case "c+" : return CASHIN;
		case "i+" : return DEBITPAY;
		case "t-" : return TRADEIN;
		case "t+" : return TRADEOUT;
		default:return null;
		}
	}

	public static TxType mappingFrom(Character vorgangsart) {
		if (vorgangsart == null) return null;
		switch (vorgangsart) {
		case '1' : return SELL; // es wurde was verkauft
		case '7' : return CASHOUT; // es wurde was ausbezahlt 
		case '8' : return CASHIN; // es wurde was einbezahlt
		case '6' : return CASHIN; // es wurde was einbezahlt, als anzahlung
		case '0' : return DEBITPAY; // es wurde eine (Kassen-)Rechnung bezahlt
		case '4' : return TRADEIN; // es fand ein Tausch statt (gutscheine) bezahlt m. Gutschein
		case '9' : return TRADEOUT; // es fand ein Tausch statt (gutschein) verkaufter gutschein
		default : return null;
		}
	}

}
