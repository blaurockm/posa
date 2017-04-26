package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import net.buchlese.posa.api.bofc.PosArticle;

@RegisterMapper(PosArticleMapper.class)
public interface PosArticleDAO {

// <column name="id" type="bigint" autoIncrement="true">
// <column name="ident" type="bigint">
// <column name="lastPurDate" type="DATETIME"/>
// <column name="lastSelDate" type="DATETIME"/>
// <column name="purchaseprice" type="bigint"/>
// <column name="sellingprice" type="bigint"/>
// <column name="artikelnummer" type="varchar(40)" />
// <column name="isbn" type="varchar(20)" />
// <column name="ean" type="varchar(40)" />
// <column name="matchcode" type="varchar(40)" />
// <column name="bezeichnung" type="varchar(80)" />
// <column name="author" type="varchar(50)" />
// <column name="publisher" type="varchar(50)" />
// <column name="stock" type="int"/>
// <column name="tax" type="varchar(2)" />
// <column name="grpidx" type="varchar(2)" />
	
	@SqlQuery("select max(ident) from posarticle")
	Integer getLastErfasst();
	
	@SqlUpdate("insert into posarticle (ident,lastPurDate,lastSelDate,purchaseprice, sellingprice, " + 
	            "artikelnummer, isbn, ean, matchcode, bezeichnung, author, publisher, stock, tax, grpidx) " +
		    " values (:artikelIdent, :lastPurchaseDate, :lastSellingDate, :purchasePrice, :sellingPrice, " + 
	            ":artikelnummer, :isbn, :ean, :matchcode, :bezeichnung, :author, :publisher, :availableStock, :tax, :wargrindex)")
    void insert(@BindBean PosArticle art);

	
	@SqlBatch("insert into posarticle (ident,lastPurDate,lastSelDate,purchaseprice, sellingprice, " + 
            "artikelnummer, isbn, ean, matchcode, bezeichnung, author, publisher, stock, tax, grpidx) " +
	    " values (:artikelIdent, :lastPurchaseDate, :lastSellingDate, :purchasePrice, :sellingPrice, " + 
            ":artikelnummer, :isbn, :ean, :matchcode, :bezeichnung, :author, :publisher, :availableStock, :tax, :wargrindex)")
	@BatchChunkSize(100)
	void insertAll(@Valid @BindBean Iterator<PosArticle> art);

	@SqlUpdate("update into posarticle (lastPurDate,lastSelDate,purchaseprice, sellingprice, " + 
            "artikelnummer, isbn, ean, matchcode, bezeichnung, author, publisher, stock, tax, grpidx) " +
	    " values (:lastPurchaseDate, :lastSellingDate, :purchasePrice, :sellingPrice, " + 
            ":artikelnummer, :isbn, :ean, :matchcode, :bezeichnung, :author, :publisher, :availableStock, :tax, :wargrindex)" +
		    " where  id = :id ")
	void updateArticle(@BindBean PosArticle art);

	@SqlQuery("select * from posarticle where ident >= :ident ")
	List<PosArticle> fetchArticle(@Bind("ident") Integer artikelident);

	@SqlQuery("select * from posarticle ")
	List<PosArticle> fetchArticles();


}
