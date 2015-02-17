package net.buchlese.bofc.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.bofc.api.bofc.PosTicket;
import net.buchlese.bofc.api.bofc.PosTx;

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

	@SqlBatch("insert into posticket (belegnr, pointid, total, paymentmethod, cancelled, cancel, timest, tobecheckedagain) " +
	" values (:belegNr, :pointid, :total, :paymentMethod, :cancelled, :cancel, :timestamp, :toBeCheckedAgain)")
	@BatchChunkSize(700)
	void insertAll(@Valid @BindBean Iterator<PosTicket> tickets);


	/**
	 * Alle Belege die zu einem Abschluss gehören (zuordnung aufgrund des datums)
	 * @param vonDatum
	 * @param bisDatum
	 * @return
	 */
	@SqlQuery("select * from posticket where timest between :vonDatum and :bisDatum")
	List<PosTicket> fetch(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);

	/**
	 * Alle Tx die zu einem Abschluss gehören. Zuordnung aufgrund des Datums des Belegs
	 * @param vonDatum
	 * @param bisDatum
	 * @return
	 */
	@SqlQuery("select postx.* from postx join posticket on posticket.belegnr = postx.belegnr where postx.tobeignored = 0 and posticket.timest between :vonDatum and :bisDatum")
	List<PosTx> fetchTx(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum);

	@SqlUpdate("update posticket set total = :total, pointid = :pointid, paymentmethod = :paymentMethod, cancelled = :cancelled, tobecheckedagain = :toBeCheckedAgain, cancel = :cancel " +
	" where id = :id ")
	void update(@Valid @BindBean PosTicket checker);

	@SqlQuery("select * from posticket where belegnr = :belegnr")
	PosTicket fetch(@Bind("belegnr") long belegnr);

	@SqlUpdate("delete from posticket where timest between :vonDatum and :bisDatum and pointid = :pointid")
	void deleteAll(@Bind("vonDatum") DateTime vonDatum, @Bind("bisDatum") DateTime bisDatum, @Bind("pointid") Integer pointid);

}
