package net.buchlese.bofc.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.bofc.api.bofc.PosInvoice;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.google.common.base.Optional;

@RegisterMapper(PosInvoiceMapper.class)
public interface PosInvoiceDAO {

	@SqlQuery("select * from posinvoice where number = :nummer")
	List<PosInvoice> fetch(@Bind("nummer") String num);

	@SqlQuery("select * from posinvoice where pointid = :kasse and invDate >= :from and invDate <= :till")
	List<PosInvoice> fetch(@Bind("kasse") Integer kasse, @Bind("from") java.util.Date from, @Bind("till") Optional<java.util.Date> till);

	@SqlQuery("select * from posinvoice where invDate > :date")
	List<PosInvoice> fetchAllAfter(@Bind("date") java.util.Date num);

	@SqlQuery("select max(creationtime) from posinvoice")
	DateTime getLastErfasst();

	@SqlBatch("insert into posinvoice (number, customer, pointid, debitor, amount, amountFull, amountHalf, amountNone, invDate, creationtime, printdate, name1, name2, name3,  street, city) " +
	" values (:number, :customerId, :pointid, :debitorId, :amount, :amountFull, :amountHalf, :amountNone, :date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city)")
	@BatchChunkSize(500)
	void insertAll(@Valid @BindBean Iterator<PosInvoice> transactions);

	@SqlUpdate("insert into posinvoice (number, customer, pointid, debitor, amount, amountFull, amountHalf, amountNone, invDate, creationtime, printdate, name1, name2, name3,  street, city) " +
	" values (:number, :customerId, :pointid, :debitorId, :amount, :amountFull, :amountHalf, :amountNone, :date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city)")
	void insert(@Valid @BindBean PosInvoice inv);

}
