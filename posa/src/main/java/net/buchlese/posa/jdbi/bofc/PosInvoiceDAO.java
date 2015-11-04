package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.posa.api.bofc.PosInvoice;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PosInvoiceMapper.class)
public interface PosInvoiceDAO {

	@SqlQuery("select * from posinvoice where number = :nummer")
	List<PosInvoice> fetch(@Bind("nummber") String num);

	@SqlQuery("select max(creationtime) from posinvoice")
	DateTime getLastErfasst();

	@SqlBatch("insert into posinvoice (number, customer, debitor, amount, amountFull, amountHalf, amountNone, invDate, creationtime, printdate, name1, name2, name3,  street, city) " +
	" values (:number, :customerId, :debitorId, :amount, :amountFull, :amountHalf, :amountNone, :creationTime,:creationTime, :printTime, :name1, :name2, :name3, :street, :city)")
	@BatchChunkSize(500)
	void insertAll(@Valid @BindBean Iterator<PosInvoice> transactions);

}
