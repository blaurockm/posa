package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.posa.api.bofc.PosCashBalance;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PosCashBalanceMapper.class)
public interface PosCashBalanceDAO {
	@SqlQuery("select * from poscashbalance where abschlussid >= :date ")
	List<PosCashBalance> fetchAllAfter(@Bind("date") String date);

	@SqlQuery("select * from poscashbalance where abschlussid >= :date and abschlussid <= :datetill")
	List<PosCashBalance> fetchAllBetween(@Bind("date") String date, @Bind("datetill") String datetill);

	@SqlQuery("select * from poscashbalance where exported = 0 and abschlussid >= '20140810'")
	List<PosCashBalance> fetchNotExported();

	@SqlQuery("select * from poscashbalance where abschlussid = :date")
	PosCashBalance fetchForDate(@Bind("date") String date);
	
	@SqlQuery("select max(creationtime) from poscashbalance")
	DateTime getMaxDatum();

	@SqlQuery("select max(abschlussid) from poscashbalance")
	String getMaxAbschlussId();

	@SqlUpdate("insert into poscashbalance (id, abschlussid, revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
	    " values (:id, :abschlussId, :revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)")
	void insert(@BindBean PosCashBalance cashBal);

	@SqlBatch("insert into poscashbalance (id, abschlussid, revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
		    " values (:id, :abschlussId, :revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)")
	@BatchChunkSize(100)
	void insertAll(@Valid @BindBean Iterator<PosCashBalance> cashBal);
	
	@SqlUpdate("update poscashbalance set (revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
		    " = (:revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)" +
		    " where  id = :id ")
	void update(@BindBean PosCashBalance cashBal);

	@SqlUpdate("delete from poscashbalance where abschlussid >= :datefrom and abschlussid <= :datetill")
	void deleteBalancesBetween(@Bind("datefrom") String datefrom, @Bind("datetill") String datetill);

	@SqlUpdate("update poscashbalance set (exported, exportdate) = (:exported, :exportDate) where  id = :id ")
	void markAsExported(@BindBean PosCashBalance bal);
}
