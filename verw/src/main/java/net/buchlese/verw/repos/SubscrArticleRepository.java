package net.buchlese.verw.repos;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrArticle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrarticle")
public interface SubscrArticleRepository extends JpaRepository<SubscrArticle, Long> {

	List<SubscrArticle> findTop5ByOrderByErschTagDesc();

}
