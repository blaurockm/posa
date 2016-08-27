package net.buchlese.posa.jdbi.pos;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.pos.KleinteilKopf;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class KleinteilKopfMapper implements ResultSetMapper<KleinteilKopf> {

	public KleinteilKopf map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		KleinteilKopf vorg = new KleinteilKopf();
		vorg.setId(rs.getInt("Nummer"));
		vorg.setRechnungsNummer(rs.getString("RechnungNummer"));
		vorg.setKundenNummer(rs.getInt("KundenNummer"));
		vorg.setName1(rs.getString("name1"));
		vorg.setName2(rs.getString("name2"));
		vorg.setName3(rs.getString("name3"));
		vorg.setStrasse(rs.getString("strasse"));
		vorg.setOrt(rs.getString("PLZ") + " " + rs.getString("ort"));
		vorg.setRabatt(rs.getBigDecimal("rabatt"));
		vorg.setSkonto(rs.getBigDecimal("skonto"));
		vorg.setBrutto(rs.getBigDecimal("rechnungsBetrag"));
		vorg.setMwst7(rs.getBigDecimal("RechnungMwst_7"));
		vorg.setMwst19(rs.getBigDecimal("RechnungMwst_16"));
		vorg.setBrutto0(rs.getBigDecimal("RechnungBetragMwst_0"));
		vorg.setBrutto7(rs.getBigDecimal("RechnungBetragMwst_7"));
		vorg.setBrutto19(rs.getBigDecimal("RechnungBetragMwst_16"));
		vorg.setArt(rs.getInt("art"));
		vorg.setErfassungsDatum(new DateTime(rs.getTimestamp("ErfassungsDatum")));
		vorg.setRechnungsDatum(new DateTime(rs.getTimestamp("RechungsDatum")));
		vorg.setDruckDatum(new DateTime(rs.getTimestamp("Gedruckt_Am")));
		vorg.setBezahlt(rs.getBoolean("bezahlt"));
		vorg.setZeitmarke(rs.getBigDecimal("MyZeitmarke"));
		return vorg;
	}

}
