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

	@SqlQuery("select * from [dbo].KleinteilElemente where kopfnummer = :nr order by laufendenummer")
	@RegisterMapper(KleinteilElementMapper.class)
	List<KleinteilElement> fetchElemente(@Bind("nr") int nr);

	// die die neu sind und in den letzten 50 tagen erfasst wurden
	@SqlQuery("select * from [dbo].KleinteilKopf where Nummer > :nr and ErfassungsDatum > current_timestamp - 50")
	@RegisterMapper(KleinteilKopfMapper.class)
	List<KleinteilKopf> fetchAllAfter(@Bind("nr") Integer or);

	@SqlQuery("select text from [dbo].Textbausteine where nummer = :nr")
	String getTextbaustein(@Bind("nr") int textbaustein);

}
