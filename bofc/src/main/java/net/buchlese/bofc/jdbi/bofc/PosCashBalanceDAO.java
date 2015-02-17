package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosCashBalance;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.google.common.base.Optional;

@RegisterMapper(PosCashBalanceMapper.class)
public interface PosCashBalanceDAO {
	@SqlQuery("select * from poscashbalance where abschlussid >= :date and abschlussid <= :datetill")
	List<PosCashBalance> fetchAllAfter(@Bind("date") String date, @Bind("datetill") Optional<String> datetill);

	@SqlQuery("select * from poscashbalance where exported = 0 and abschlussid >= '20140810'")
	List<PosCashBalance> fetchNotExported();

	@SqlQuery("select * from poscashbalance where abschlussid = :date")
	PosCashBalance fetchForDate(@Bind("date") String date);
	
	@SqlQuery("select id from poscashbalance where abschlussid = :abschlussid and pointid = :pointId")
	Long getIdOfExistingBalance(@Bind("abschlussid") String abschlussid,@Bind("pointId") Integer pointId);

	@SqlUpdate("insert into poscashbalance (pointid, abschlussid, revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
	    " values (:pointid, :abschlussId, :revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)")
	void insert(@BindBean PosCashBalance cashBal);

	@SqlUpdate("update poscashbalance set (revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
		    " = (:revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)" +
		    " where  id = :id ")
	void update(@BindBean PosCashBalance cashBal);

}
