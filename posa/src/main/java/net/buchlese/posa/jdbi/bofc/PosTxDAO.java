package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.posa.api.bofc.PosTx;

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

	@SqlBatch("insert into postx (id, belegnr, belegidx, sellingprice, purchaseprice, articlegroupkey, articleid, articlekey, total, description, count, ean, isbn, matchcode,  tax, txtype, timest, tobeignored, tobecheckedagain) " +
	" values (:id, :belegNr, :belegIdx, :sellingPrice, :purchasePrice, :articleGroupKey, :articleId, :articleKey, :total, :description, :count, :ean, :isbn, :matchCode, :tax, :type, :timestamp, :toBeIgnored, :toBeCheckedAgain)")
	@BatchChunkSize(500)
	void insertAll(@Valid @BindBean Iterator<PosTx> transactions);

	@SqlQuery("select max(timest) from postx")
	DateTime getMaxDatum();

	@SqlQuery("select max(id) from postx")
	Integer getMaxId();

	@SqlQuery("select * from postx where tobeignored = 0 and timest between :vonDatum and :bisDatum")
	List<PosTx> fetch(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);

	@SqlQuery("select * from postx where tobecheckedAgain = 1 and timest > :datum")
	List<PosTx> fetchRevisitations(@Bind("datum") DateTime datum);

	@SqlUpdate("update postx set sellingprice = :sellingPrice, purchaseprice = :purchasePrice, articlegroupkey = :articleGroupKey, " +
	 " articleid = :articleId, articlekey = :articleKey, total = :total, description = :description, count = :count, " +
	 " ean = :ean, isbn = :isbn, matchcode = :matchCode,  tax = :tax, txtype = :type, " + 
	 " tobeignored = :toBeIgnored, tobecheckedagain = :toBeCheckedAgain where id = :id ")
	void update(@Valid @BindBean PosTx checker);

	@SqlQuery("select * from postx where belegnr = :belegNr and belegidx = :lfdNummer")
	PosTx fetch(@Bind("belegNr") int belegNr, @Bind("lfdNummer") int lfdNummer);

	@SqlUpdate("delete from postx where timest between :vonDatum and :bisDatum")
	void deleteTxBetween(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);
}
