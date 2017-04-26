package net.buchlese.posa.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import net.buchlese.posa.api.bofc.PosArticle;
import net.buchlese.posa.api.bofc.Tax;

public class PosArticleMapper implements ResultSetMapper<PosArticle> {

	public PosArticle map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosArticle cb = new PosArticle();
		// just to be shure
		cb.setId(rs.getLong("id"));
		cb.setArtikelIdent(rs.getLong("ident"));
		cb.setLastPurchaseDate(new DateTime(rs.getTimestamp("lastPurDate")));
		cb.setLastSellingDate(new DateTime(rs.getTimestamp("lastSelDate")));
		cb.setPurchasePrice(rs.getLong("purchaseprice"));
		cb.setSellingPrice(rs.getLong("sellingprice"));
		cb.setArtikelnummer(rs.getString("artikelnummer"));
		cb.setIsbn(rs.getString("isbn"));
		cb.setEan(rs.getString("ean"));
		cb.setMatchcode(rs.getString("matchcode"));
		cb.setBezeichnung(rs.getString("bezeichnung"));
		cb.setAuthor(rs.getString("author"));
		cb.setPublisher(rs.getString("publisher"));
		cb.setAvailableStock(rs.getInt("stock"));
		cb.setTax(Tax.fromDbKey(rs.getString("tax")));
		cb.setWargrindex(rs.getString("grpidx"));
		return cb;
	}

}
