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

	@SqlQuery("select [dbo].posakassvorg.*, [dbo].artikel.wargrindex from [dbo].posakassvorg join [dbo].artikel on [dbo].posakassvorg.artikelident = [dbo].artikel.artikelident where belegnr = :belegnr and kassennr = :kassennr ")
	List<KassenVorgang> fetchForBeleg(@BindBean KassenBeleg beleg);

	@SqlQuery("select [dbo].posakassvorg.*, [dbo].artikel.wargrindex from [dbo].posakassvorg join [dbo].artikel on [dbo].posakassvorg.artikelident = [dbo].artikel.artikelident  where datum> :datum order by datum asc ")
	List<KassenVorgang> fetchAllAfter(@Bind("datum") DateTime maxDatum);

	@SqlQuery("select [dbo].posakassvorg.*, [dbo].artikel.wargrindex from [dbo].posakassvorg join [dbo].artikel on [dbo].posakassvorg.artikelident = [dbo].artikel.artikelident  where datum between :vonDatum  and :bisDatum ")
	List<KassenVorgang> fetchAllBetween(@Bind("vonDatum") DateTime fromDate, @Bind("bisDatum") DateTime tillDate);

	@SqlQuery("select zahlungsbetrag from [dbo].kassenbelege where belegnr = :belegnr")
	BigDecimal fetchZahlbetrag(@Bind("belegnr") int belegNr);

	@SqlQuery("select [dbo].posakassvorg.*, [dbo].artikel.wargrindex from [dbo].posakassvorg join [dbo].artikel on [dbo].posakassvorg.artikelident = [dbo].artikel.artikelident  where belegnr = :belegnr and kassennr = :kassennr ")
	List<KassenVorgang> fetchForBeleg(@Bind("belegnr")int belegNr, @Bind("kassennr") int kassenNummer);

	@SqlQuery("select [dbo].posakassvorg.*, [dbo].artikel.wargrindex from [dbo].posakassvorg join [dbo].artikel on [dbo].posakassvorg.artikelident = [dbo].artikel.artikelident  where belegnr = :belegnr and lfdNummer = :belegidx ")
	KassenVorgang fetch(@Bind("belegnr") long belegNr, @Bind("belegidx") int belegIdx);
	
}
