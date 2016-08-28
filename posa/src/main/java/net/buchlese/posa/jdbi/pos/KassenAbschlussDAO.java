package net.buchlese.posa.jdbi.pos;

import java.math.BigDecimal;
import java.util.List;

import net.buchlese.posa.api.pos.KassenAbschluss;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(KassenAbschlussMapper.class)
public interface KassenAbschlussDAO {

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].kasse_abschlussdaten where abschlussid = :date")
	KassenAbschluss fetchForDate(@Bind("date") String date);  // format yyyyMMdd

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].kasse_abschlussdaten where abschlussid > :datum and bisdatum > current_timestamp - :lim")
	List<KassenAbschluss> fetchAllAfter(@Bind("datum") String datum, @Bind("lim") Integer lim);

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].kasse_abschlussdaten where convert(bigint, zeitmarke) > :dat")
	List<KassenAbschluss> fetchAllChangedAfter(@Bind("dat") BigDecimal d);

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].kasse_abschlussdaten where bisDatum between :vondatum and :bisdatum")
	List<KassenAbschluss> fetchAllBetween(@Bind("vondatum") DateTime from, @Bind("bisdatum") DateTime till);

}
