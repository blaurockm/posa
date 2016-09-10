package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import net.buchlese.posa.api.bofc.PosInvoice;
import net.buchlese.posa.api.bofc.PosIssueSlip;

public interface PosInvoiceDAO {

	@RegisterMapper(PosInvoiceMapper.class)
	@SqlQuery("select * from posinvoice where number = :nummer")
	List<PosInvoice> fetchInvoice(@Bind("nummer") String num);

	@RegisterMapper(PosIssueSlipMapper.class)
	@SqlQuery("select * from posissueslip where number = :nummer")
	List<PosIssueSlip> fetchIssueSlip(@Bind("nummer") String num);

	@RegisterMapper(PosInvoiceMapper.class)
	@SqlQuery("select * from posinvoice where invDate > :date")
	List<PosInvoice> fetchAllAfter(@Bind("date") java.util.Date num);

	@RegisterMapper(PosIssueSlipMapper.class)
	@SqlQuery("select * from posissueslip where invDate > :date")
	List<PosIssueSlip> fetchAllIssueSlipsAfter(@Bind("date") java.util.Date num);

	@SqlQuery("select max(actionum) from posinvoice where invDate > sysdate - 50")
	Integer getLastErfasst();

	@SqlQuery("select max(actionum) from posissueslip where invDate > sysdate - 50")
	Integer getLastErfasstLieferschein();

	@SqlBatch("insert into posinvoice (number, customer, debitor, amount, amountFull, amountHalf, amountNone, "
			+ "invDate, creationtime, printdate, name1, name2, name3,  street, city, actionum, payed, cancelled, complJson) " +
	" values (:number, :customerId, :debitorId, :amount, :amountFull, :amountHalf, :amountNone, "
	        + ":date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city, :actionum, :payed, :cancelled, :complJson)")
	@BatchChunkSize(500)
	void insertAll(@Valid @BindBean Iterator<PosInvoice> transactions);

	@SqlUpdate("update posinvoice set (customer, debitor, amount, amountFull, amountHalf, amountNone,  "
			+ "invDate, creationtime, printdate, name1, name2, name3,  street, city, actionum, payed, cancelled, complJson) " +
	"  = (:customerId, :debitorId, :amount, :amountFull, :amountHalf, :amountNone, "
	        + ":date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city, :actionum, :payed, :cancelled, :complJson) where id = :id")
	void updateInvoice(@Valid @BindBean PosInvoice inv);

	@SqlBatch("insert into posissueslip (number, customer, debitor, "
			+ "invDate, creationtime, printdate, name1, name2, name3,  street, city, actionum, payed, complJson) " +
	" values (:number, :customerId, :debitorId,  "
	        + ":date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city, :actionum, :payed, :complJson)")
	@BatchChunkSize(500)
	void insertAllIssueSlip(@Valid @BindBean Iterator<PosIssueSlip> transactions);

	@SqlUpdate("update posissueslip set (customer, debitor,  "
			+ "invDate, creationtime, printdate, name1, name2, name3,  street, city, actionum, payed, complJson) " +
	" = (:customerId, :debitorId, "
	        + ":date,:creationTime, :printTime, :name1, :name2, :name3, :street, :city, :actionum, :payed, :complJson) where id = :id")
	void updateIssueSlip(@Valid @BindBean PosIssueSlip inv);

}
