package net.buchlese.verw.repos;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "posarticles", path = "posarticle")
public interface ArticleRepository extends JpaRepository<PosArticle, Long>, 
QueryDslPredicateExecutor<PosArticle> {

	List<PosArticle> findByArtikelIdent(Long ident);
}
