package net.buchlese.bofc.jdbi.bofc;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosCashBalance;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.google.common.base.Optional;

@RegisterMapper(PosCashBalanceMapper.class)
public interface PosCashBalanceDAO {
	public static String IDFORMAT = "yyyyMMdd";

	@SqlQuery("select * from poscashbalance where abschlussid >= :date and abschlussid <= :datetill")
	List<PosCashBalance> fetchAllAfter(@Bind("date") String date, @Bind("datetill") String datetill);

	@SqlQuery("select * from poscashbalance where pointid = :pointId and abschlussid >= :date and abschlussid <= :datetill order by abschlussid")
	List<PosCashBalance> fetch(@Bind("pointId") Integer pointId, @Bind("date") String date, @Bind("datetill") Optional<String> datetill);

	@SqlQuery("select * from poscashbalance where abschlussid = :date")
	List<PosCashBalance> fetchForDate(@Bind("date") String date);

	@SqlQuery("select * from poscashbalance where id = :id")
	PosCashBalance fetchForId(@Bind("id") String id);

	@SqlQuery("select id from poscashbalance where abschlussid = :abschlussid and pointid = :pointId")
	Long getIdOfExistingBalance(@Bind("abschlussid") String abschlussid,@Bind("pointId") Integer pointId);

	@GetGeneratedKeys
	@SqlUpdate("insert into poscashbalance (pointid, abschlussid, revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
	    " values (:pointid, :abschlussId, :revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)")
	long insert(@BindBean PosCashBalance cashBal);

	@SqlUpdate("update poscashbalance set (revenue, creationtime, balancesheet, origsheet, exported, exportdate, firstCovered, lastCovered) " +
		    " = (:revenue, :creationtime, :balanceSheet, :origAbschluss, :exported, :exportDate, :firstCovered, :lastCovered)" +
		    " where  id = :id ")
	void update(@BindBean PosCashBalance cashBal);

	@SqlQuery("select sum(revenue) from poscashbalance where firstCovered between :firstDay and :lastDay")
	Long getProfitOfTimespan(@Bind("firstDay") DateTime firstDay, @Bind("lastDay") DateTime lastDay);

}
