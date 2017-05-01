package net.buchlese.posa.jdbi.bofc;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import net.buchlese.posa.api.bofc.PosArticle;
import net.buchlese.posa.api.bofc.PosArticleStockChange;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

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

	@SqlUpdate("update posarticle set (lastPurDate,lastSelDate,purchaseprice, sellingprice, " + 
            "artikelnummer, isbn, ean, matchcode, bezeichnung, author, publisher, stock, tax, grpidx) " +
	    " = (:lastPurchaseDate, :lastSellingDate, :purchasePrice, :sellingPrice, " + 
            ":artikelnummer, :isbn, :ean, :matchcode, :bezeichnung, :author, :publisher, :availableStock, :tax, :wargrindex)" +
		    " where  id = :id ")
	void updateArticle(@BindBean PosArticle art);

	@SqlQuery("select * from posarticle where ident = :ident ")
	@RegisterMapper(PosArticleMapper.class)
	List<PosArticle> fetchArticle(@Bind("ident") Integer artikelident);

	@SqlQuery("select * from posarticle ")
	@RegisterMapper(PosArticleMapper.class)
	List<PosArticle> fetchArticles();


	@SqlQuery("select changeDate from posstockchange")
	DateTime getLastStockChange();
//	
//	private Long id;
//	private int pointid;
//	private long artikelIdent;
//	private DateTime changeDate;
//	private Integer stockChange;
//	private Integer stockBefore;
//	private Integer stockAfter;
//	private String comment;

	@SqlUpdate("insert into posstockchange (ident, changeDate, stockChange, stockBefore, stockAfter, comment) " +
	    " values (:artikelIdent, :changeDate, :stockChange, :stockBefore, :stockAfter, :comment) ")
	void insertStockChange(@BindBean PosArticleStockChange art);

	@SqlBatch("insert into posstockchange (ident, changeDate, stockChange, stockBefore, stockAfter, comment) " +
		    " values (:artikelIdent, :changeDate, :stockChange, :stockBefore, :stockAfter, :comment) ")
	@BatchChunkSize(100)
    void insertStockChangeAll(@Valid @BindBean Iterator<PosArticleStockChange> art);

	@SqlUpdate("update posstockchange set (stockChange, stockBefore, stockAfter, comment) " + 
	    " = (:stockChange, :stockChange, :stockBefore, :stockAfter, :comment )" + 
		    " where  id = :id ")
	void updateStockChange(@BindBean PosArticleStockChange art);

	@SqlQuery("select * from posstockchange where ident = :ident and changeDate = :chdat ")
	@RegisterMapper(PosArticleStockChangeMapper.class)
	List<PosArticleStockChange> fetchStockChange(@Bind("ident") Integer artikelident, @Bind("chdat") DateTime chdate);

	@SqlQuery("select * from posstockchange where ident = :ident ")
	@RegisterMapper(PosArticleStockChangeMapper.class)
	List<PosArticleStockChange> fetchStockChange(@Bind("ident") Integer artikelident);

	@SqlQuery("select * from posstockchange ")
	@RegisterMapper(PosArticleStockChangeMapper.class)
	List<PosArticleStockChange> fetchStockChanges();
	
	
}
