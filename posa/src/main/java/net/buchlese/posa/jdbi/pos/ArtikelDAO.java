package net.buchlese.posa.jdbi.pos;

import java.util.List;

import net.buchlese.posa.api.pos.Artikel;
import net.buchlese.posa.api.pos.ArtikelBestandBuchung;
import net.buchlese.posa.api.pos.ArtikelEinkauf;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface ArtikelDAO {

	@RegisterMapper(ArtikelMapper.class)
	@SqlQuery("select * from [dbo].Artikel where isbn = :isbn")
	List<Artikel> fetch(@Bind("isbn") String isbn);

	
	@RegisterMapper(ArtikelEinkaufMapper.class)
	@SqlQuery("select * from [dbo].[Artikel Einkauf] where artikelident = :ident")
	List<ArtikelEinkauf> fetchEinkauf(@Bind("ident") String ident);

	@RegisterMapper(ArtikelBestandBuchungMapper.class)
	@SqlQuery("select * from [dbo].Artikel_BestandBuchungen where artikelident = :ident")
	List<ArtikelBestandBuchung> fetchBestandsbuchungen(@Bind("ident") String ident);

}
