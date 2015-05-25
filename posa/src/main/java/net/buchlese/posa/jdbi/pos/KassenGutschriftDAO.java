package net.buchlese.posa.jdbi.pos;

import java.util.List;

import net.buchlese.posa.api.pos.KassenGutschrift;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(KassenGutschriftMapper.class)
public interface KassenGutschriftDAO {

	@SqlQuery("select * from [dbo].Kasse_gutschriften where GutschriftNr = :nr")
	List<KassenGutschrift> fetch(@Bind("nr") String nr);
	
}
