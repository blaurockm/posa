package net.buchlese.posa.jdbi.bofc;

import java.util.List;

import net.buchlese.posa.api.bofc.PosCashBalance;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(PosCashBalanceMapper.class)
public interface PosCashBalanceDAO {
	@SqlQuery("select * from poscashbalance where abschlussid >= :date")
	List<PosCashBalance> fetchAllAfter(@Bind("date") String date);

	@SqlQuery("select * from poscashbalance where exported = 0 and abschlussid >= '20140810'")
	List<PosCashBalance> fetchNotExported();

	@SqlQuery("select * from poscashbalance where abschlussid = :date")
	PosCashBalance fetchForDate(@Bind("date") String date);
	
	@SqlQuery("select max(creationtime) from poscashbalance")
	DateTime getMaxDatum();

	@SqlUpdate("insert into poscashbalance (id, abschlussid, revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
	    " values (:id, :abschlussId, :revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)")
	void insert(@BindBean PosCashBalance cashBal);

}
