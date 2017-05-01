package net.buchlese.verw.repos;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosArticleStockChange;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "posarticlestockchanges", path = "posstockchange")
public interface StockChangeRepository extends JpaRepository<PosArticleStockChange, Long>, 
QueryDslPredicateExecutor<PosArticleStockChange> {

	List<PosArticleStockChange> findByArtikelIdent(long ident);
}
