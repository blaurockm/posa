package net.buchlese.posa.jdbi.pos;

import java.math.BigDecimal;
import java.util.List;

import net.buchlese.posa.api.pos.KleinteilElement;
import net.buchlese.posa.api.pos.KleinteilKopf;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface KleinteilDAO {

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where rechnungNummer = :nr")
	@RegisterMapper(KleinteilKopfMapper.class)
	List<KleinteilKopf> fetch(@Bind("nr") String nr);

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where lieferscheinnummer = :nr")
	@RegisterMapper(KleinteilKopfLieferscheinMapper.class)
	List<KleinteilKopf> fetchLieferschein(@Bind("nr") String nr);

	@SqlQuery("select * from [dbo].KleinteilElemente where kopfnummer = :nr order by laufendenummer")
	@RegisterMapper(KleinteilElementMapper.class)
	List<KleinteilElement> fetchElemente(@Bind("nr") int nr);

	// die die neu sind und in den letzten 50 tagen erfasst wurden
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where Nummer > :nr and rechnungnummer is not null and ErfassungsDatum > current_timestamp - :lim")
	@RegisterMapper(KleinteilKopfMapper.class)
	List<KleinteilKopf> fetchAllRechnungenAfter(@Bind("nr") Integer or, @Bind("lim") Integer lim);

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where convert(bigint, zeitmarke) > :dat and rechnungnummer is not null")
	@RegisterMapper(KleinteilKopfMapper.class)
	List<KleinteilKopf> fetchAllChangedRechnungenAfter(@Bind("dat") BigDecimal d);

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where Nummer > :nr and lieferscheinnummer is not null and ErfassungsDatum > current_timestamp - :lim")
	@RegisterMapper(KleinteilKopfLieferscheinMapper.class)
	List<KleinteilKopf> fetchAllLieferscheinAfter(@Bind("nr") Integer or, @Bind("lim") Integer lim);

	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where convert(bigint, zeitmarke) > :dat  and lieferscheinnummer is not null")
	@RegisterMapper(KleinteilKopfLieferscheinMapper.class)
	List<KleinteilKopf> fetchAllChangedLieferscheineAfter(@Bind("dat") BigDecimal d);

	@SqlQuery("select text from [dbo].Textbausteine where nummer = :nr")
	String getTextbaustein(@Bind("nr") int textbaustein);

}
