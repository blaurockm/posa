package net.buchlese.posa.jdbi.pos;

import java.math.BigDecimal;
import java.util.List;

import net.buchlese.posa.api.pos.KassenBeleg;
import net.buchlese.posa.api.pos.KassenVorgang;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(KassenVorgangMapper.class)
public interface KassenVorgangDAO {

	@SqlQuery("select * from [dbo].posakassvorg where belegnr = :belegnr and kassennr = :kassennr ")
	List<KassenVorgang> fetchForBeleg(@BindBean KassenBeleg beleg);

	@SqlQuery("select * from [dbo].posakassvorg where datum> :datum order by datum asc ")
	List<KassenVorgang> fetchAllAfter(@Bind("datum") DateTime maxDatum);

	@SqlQuery("select * from [dbo].posakassvorg where datum between :vonDatum  and :bisDatum ")
	List<KassenVorgang> fetchAllBetween(@Bind("vonDatum") DateTime fromDate, @Bind("bisDatum") DateTime tillDate);

	@SqlQuery("select top 10 * from [dbo].posakassvorg order by datum desc ")
	List<KassenVorgang> fetchLast();

	@SqlQuery("select zahlungsbetrag from [dbo].kassenbelege where belegnr = :belegnr")
	BigDecimal fetchZahlbetrag(@Bind("belegnr") int belegNr);

	@SqlQuery("select * from [dbo].posakassvorg where belegnr = :belegnr and kassennr = :kassennr ")
	List<KassenVorgang> fetchForBeleg(@Bind("belegnr")int belegNr, @Bind("kassennr") int kassenNummer);

	@SqlQuery("select * from [dbo].posakassvorg where belegnr = :belegnr and lfdNummer = :belegidx ")
	KassenVorgang fetch(@Bind("belegnr") long belegNr, @Bind("belegidx") int belegIdx);
	
}
