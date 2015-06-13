package net.buchlese.posa.jdbi.bofc;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.bofc.Tax;
import net.buchlese.posa.api.bofc.TxType;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class PosTxMapper implements ResultSetMapper<PosTx> {

	public PosTx map(int arg0, ResultSet rs, StatementContext ctx) throws SQLException {
		PosTx tx = new PosTx();
		tx.setId(rs.getLong("id"));
		tx.setSellingPrice(rs.getLong("sellingPrice"));
		tx.setPurchasePrice(rs.getLong("purchasePrice"));
		tx.setRebate(rs.getDouble("rebate"));
		tx.setBelegNr(rs.getLong("belegnr"));
		tx.setBelegIdx(rs.getInt("belegidx"));
		tx.setTotal(rs.getLong("total"));
		tx.setDescription(rs.getString("description"));
		tx.setCount(rs.getInt("count"));
		tx.setEan(rs.getString("ean"));
		tx.setIsbn(rs.getString("isbn"));
		tx.setMatchCode(rs.getString("matchCode"));
		tx.setArticleGroupKey(rs.getString("articleGroupKey"));
		tx.setArticleId(rs.getInt("articleId"));
		tx.setArticleKey(rs.getString("articlekey"));
		tx.setTax(Tax.fromDbKey(rs.getString("tax")));
		tx.setType(TxType.fromDbKey(rs.getString("txtype")));
		tx.setTimestamp(new DateTime(rs.getTimestamp("timest")));
		return tx;
	}

}
