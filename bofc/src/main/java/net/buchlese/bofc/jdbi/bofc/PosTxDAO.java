package net.buchlese.bofc.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.bofc.api.bofc.PosTx;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PosTxMapper.class)
public interface PosTxDAO {

	@SqlBatch("insert into postx (belegnr, belegidx, pointid, sellingprice, purchaseprice, articlegroupkey, articleid, articlekey, total, description, count, ean, isbn, matchcode,  tax, txtype, timest, tobeignored, tobecheckedagain) " +
	" values (:belegNr, :belegIdx, :pointid, :sellingPrice, :purchasePrice, :articleGroupKey, :articleId, :articleKey, :total, :description, :count, :ean, :isbn, :matchCode, :tax, :type, :timestamp, :toBeIgnored, :toBeCheckedAgain)")
	@BatchChunkSize(500)
	void insertAll(@Valid @BindBean Iterator<PosTx> transactions);


	@SqlQuery("select * from postx where tobeignored = 0 and timest between :vonDatum and :bisDatum")
	List<PosTx> fetch(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);

	/**
	 * Alle Tx die zu einem Abschluss gehören. Zuordnung aufgrund des Datums des Belegs
	 * @param vonDatum
	 * @param bisDatum
	 * @return
	 */
	@SqlQuery("select postx.* from postx join posticket on posticket.belegnr = postx.belegnr where postx.tobeignored = 0 and posticket.timest between :vonDatum and :bisDatum")
	List<PosTx> fetchTx(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);


	@SqlUpdate("update postx set sellingprice = :sellingPrice, purchaseprice = :purchasePrice, articlegroupkey = :articleGroupKey, " +
	 " articleid = :articleId, articlekey = :articleKey, total = :total, description = :description, count = :count, " +
	 " ean = :ean, isbn = :isbn, matchcode = :matchCode,  tax = :tax, txtype = :type, pointid = :pointid, " + 
	 " tobeignored = :toBeIgnored, tobecheckedagain = :toBeCheckedAgain where id = :id ")
	void update(@Valid @BindBean PosTx checker);

	@SqlQuery("select * from postx where belegnr = :belegNr and belegidx = :lfdNummer")
	PosTx fetch(@Bind("belegNr") int belegNr, @Bind("lfdNummer") int lfdNummer);

	@SqlUpdate("delete from postx where timest between :vonDatum and :bisDatum and pointid = :pointid")
	void deleteAll(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum, @Bind("pointid") Integer pointid);

}
