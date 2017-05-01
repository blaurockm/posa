package net.buchlese.posa.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.bofc.PosArticleStockChange;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PosArticleStockChangeMapper implements ResultSetMapper<PosArticleStockChange> {

	public PosArticleStockChange map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosArticleStockChange cb = new PosArticleStockChange();
		// just to be shure
		cb.setId(rs.getLong("id"));
		cb.setArtikelIdent(rs.getLong("ident"));
		cb.setChangeDate(new DateTime(rs.getTimestamp("changeDate")));
		cb.setStockChange(rs.getInt("stockChange"));
		cb.setStockAfter(rs.getInt("stockAfter"));
		cb.setStockBefore(rs.getInt("stockBefore"));
		return cb;
	}

}
