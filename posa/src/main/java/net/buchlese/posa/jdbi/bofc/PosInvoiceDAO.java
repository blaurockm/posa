package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import net.buchlese.posa.api.bofc.PosInvoice;

@RegisterMapper(PosInvoiceMapper.class)
public interface PosInvoiceDAO {

	@SqlQuery("select * from posinvoice where number = :nummer")
	List<PosInvoice> fetch(@Bind("nummer") String num);

	@SqlQuery("select * from posinvoice where invDate > :date")
	List<PosInvoice> fetchAllAfter(@Bind("date") java.util.Date num);

	@SqlQuery("select max(actionum) from posinvoice")
	Integer getLastErfasst();

	@SqlBatch("insert into posinvoice (number, customer, debitor, amount, amountFull, amountHalf, amountNone, invDate, creationtime, printdate, name1, name2, name3,  street, city, actionum, payed) " +
	" values (:number, :customerId, :debitorId, :amount, :amountFull, :amountHalf, :amountNone, :date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city, :actionum, :payed)")
	@BatchChunkSize(500)
	void insertAll(@Valid @BindBean Iterator<PosInvoice> transactions);

}
