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

	/**
	 * die indexnummer des letzten Gutscheins für diesen Beleg
	 * @param belegNr
	 * @return
	 */
	@SqlQuery("select max(lfdNummer) from [dbo].posakassvorg where belegnr = :belegnr and gesamt < 0 ")
	int fetchGutschForBeleg(@Bind("belegnr")int belegNr);

	@SqlQuery("select * from [dbo].posakassvorg where datum> :datum order by datum asc ")
	List<KassenVorgang> fetchAllAfter(@Bind("datum") DateTime maxDatum);

	@SqlQuery("select top 10 * from [dbo].posakassvorg order by datum desc ")
	List<KassenVorgang> fetchLast();

	@SqlQuery("select sum(zahlungsbetrag) from [dbo].kassenbelege where belegnr = :belegnr")
	BigDecimal fetchZahlbetrag(@Bind("belegnr") int belegNr);

	/**
	 * alle vorgänge dieses belegs ohne den mit dem geg. idx
	 * @param belegNr
	 * @param lfdNummer
	 * @return
	 */
	@SqlQuery("select sum(gesamt) from [dbo].posakassvorg where belegnr = :belegnr and lfdnummer != :idx")
	BigDecimal fetchBelegSumOhne(@Bind("belegnr")int belegNr, @Bind("idx") int lfdNummer);

	@SqlQuery("select * from [dbo].posakassvorg where belegnr = :belegnr and lfdNummer = :belegidx ")
	KassenVorgang fetch(@Bind("belegnr") long belegNr, @Bind("belegidx") int belegIdx);
	
}
