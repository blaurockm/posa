package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.posa.api.bofc.PosTicket;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PosTicketMapper.class)
public interface PosTicketDAO {

	@SqlQuery("select * from posticket")
	List<PosTicket> fetchAll();

	@SqlBatch("insert into posticket (id, belegnr, total, paymentmethod, cancelled, timest, tobecheckedagain) " +
	" values (:id, :belegNr, :total, :paymentMethod, :cancelled, :timestamp, :toBeCheckedAgain)")
	@BatchChunkSize(700)
	void insertAll(@Valid @BindBean Iterator<PosTicket> tickets);

	@SqlQuery("select max(timest) from posticket")
	DateTime getMaxTimestamp();

	@SqlQuery("select max(id) from posticket")
	Integer getMaxId();

	@SqlQuery("select * from posticket where cancelled = 0 and timest between :vonDatum and :bisDatum")
	List<PosTicket> fetch(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);

	@SqlQuery("select * from posticket where tobecheckedagain = 1 and timest > :datum")
	List<PosTicket> fetchRevisitations(@Bind("datum") DateTime datum);

	@SqlUpdate("update posticket set total = :total, paymentmethod = :paymentMethod, cancelled = :cancelled, tobecheckedagain = :toBeCheckedAgain " +
	" where id = :id ")
	void update(@Valid @BindBean PosTicket checker);

	@SqlQuery("select * from posticket where belegnr = :belegnr")
	PosTicket fetch(@Bind("belegnr") long belegnr);

}
