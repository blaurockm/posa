package net.buchlese.posa.jdbi.pos;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import net.buchlese.posa.api.pos.KleinteilElement;
import net.buchlese.posa.api.pos.KleinteilKopf;

public interface KleinteilDAO {

	@SqlQuery("select * from [dbo].KleinteilKopf where rechnungNummer = :nr")
	@RegisterMapper(KleinteilKopfMapper.class)
	List<KleinteilKopf> fetch(@Bind("nr") String nr);

	@SqlQuery("select * from [dbo].KleinteilElemente where kopfnummer = :nr")
	@RegisterMapper(KleinteilElementMapper.class)
	List<KleinteilElement> fetchElemente(@Bind("nr") String nr);

	// die die neu sind und in den letzten 50 tagen erfasst wurden
	@SqlQuery("select * from [dbo].KleinteilKopf where Nummer > :nr and ErfassungsDatum > and erfassungsdatum > current_timestamp - 50")
	@RegisterMapper(KleinteilKopfMapper.class)
	List<KleinteilKopf> fetchAllAfter(@Bind("nr") Integer or);

}
