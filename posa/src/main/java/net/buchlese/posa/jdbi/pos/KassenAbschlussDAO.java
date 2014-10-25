package net.buchlese.posa.jdbi.pos;

import java.util.List;

import net.buchlese.posa.api.pos.KassenAbschluss;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(KassenAbschlussMapper.class)
public interface KassenAbschlussDAO {

	@SqlQuery("select * from [dbo].kasse_abschlussdaten where abschlussid = :date")
	KassenAbschluss fetchForDate(@Bind("date") String date);  // format yyyyMMdd

	@SqlQuery("select * from [dbo].kasse_abschlussdaten where abschlussid > :datum")
	List<KassenAbschluss> fetchAllAfter(@Bind("datum") String datum);

	// TODO: should be "letzeÄnderung" instead of "bisdatum" but there is a fucking umlaut
	@SqlQuery("select * from [dbo].kasse_abschlussdaten where bisDatum > :datum")
	List<KassenAbschluss> fetchAllModified(@Bind("datum") DateTime datum);

}
