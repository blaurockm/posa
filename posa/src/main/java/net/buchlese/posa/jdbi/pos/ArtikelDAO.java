package net.buchlese.posa.jdbi.pos;

import java.math.BigDecimal;
import java.util.List;

import net.buchlese.posa.api.pos.Artikel;
import net.buchlese.posa.api.pos.ArtikelBestandBuchung;
import net.buchlese.posa.api.pos.ArtikelEinkauf;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

public interface ArtikelDAO {

	@RegisterMapper(ArtikelMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel where isbn = :isbn")
	List<Artikel> fetch(@Bind("isbn") String isbn);

	@RegisterMapper(ArtikelMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel where artikelident = :ident")
	List<Artikel> fetchByArtikelident(@Bind("ident") Long ident);

//	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel where artikelident > :indent and LetztVKDatum > current_timestamp - :limd")
	@RegisterMapper(ArtikelMapper.class)
	@SqlQuery("select top 1000 *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel where artikelident > :ident ")
	List<Artikel> fetchAllArtikelAfter(@Bind("ident") Integer or,@Bind("limd") Integer limit);
	
	@RegisterMapper(ArtikelMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel where convert(bigint, zeitmarke) > :dat")
	List<Artikel> fetchAllChangedArtikelAfter(@Bind("dat") BigDecimal dat);
	
	@RegisterMapper(ArtikelEinkaufMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].[Artikel Einkauf] where artikelident = :ident")
	List<ArtikelEinkauf> fetchEinkauf(@Bind("ident") Long ident);

	@RegisterMapper(ArtikelEinkaufMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].[Artikel Einkauf] where artikelident = :ident")
	List<ArtikelEinkauf> fetchEinkaufByIdent(@Bind("ident") Long ident);

	@RegisterMapper(ArtikelBestandBuchungMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel_BestandBuchungen where artikelident = :ident")
	List<ArtikelBestandBuchung> fetchBestandsbuchungen(@Bind("ident") Long ident);

	@RegisterMapper(ArtikelBestandBuchungMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel_BestandBuchungen where datum > current_timestamp - :limd")
	List<ArtikelBestandBuchung> fetchBestandsbuchungenAfter(@Bind("limd") Integer limit);

	@RegisterMapper(ArtikelBestandBuchungMapper.class)
	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].Artikel_BestandBuchungen where convert(bigint, zeitmarke) > :dat")
	List<ArtikelBestandBuchung> fetchBestandsbuchungenAfter(@Bind("dat") BigDecimal dat);


//	@SqlQuery("select *, convert(bigint, zeitmarke) as MyZeitmarke from [dbo].KleinteilKopf where convert(bigint, zeitmarke) > :dat and rechnungnummer is not null")

}
