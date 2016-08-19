package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.ArticleGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "articleGroup")
public interface ArticleGroupRepository extends JpaRepository<ArticleGroup, String> {

}
