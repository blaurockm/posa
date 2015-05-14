package net.buchlese.posa.jdbi.pos;

import java.util.List;

import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.api.pos.KassenVorgang;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(KassenBelegMapper.class)
public interface KassenBelegDAO {
	
	@SqlQuery("select * from [dbo].kassenbelege where datum > :datum order by datum asc")
	List<KassenBeleg> fetchAllAfter(@Bind("datum") DateTime maxDatum);

	@SqlQuery("select top 10 * from [dbo].kassenbelege order by datum desc ")
	List<KassenBeleg> fetchLast();

	@SqlQuery("select * from [dbo].kassenbelege where datum between :vonDatum  and :bisDatum and zahlungsbetrag is not null")
	List<KassenBeleg> fetchAllBetween(@Bind("vonDatum") DateTime fromDate, @Bind("bisDatum") DateTime tillDate);

	@SqlQuery("select * from [dbo].posakassvorg where belegnr = :belegnr and kassennr = :kassennr ")
	List<KassenVorgang> fetchForBeleg(@BindBean KassenBeleg beleg);

	@SqlQuery("select * from [dbo].kassenbelege where belegnr = :belegnr")
	KassenBeleg fetch(@Bind("belegnr") Long nr);

}
