package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.AccountingExport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "exports", path = "export")
public interface ExportRepository extends JpaRepository<AccountingExport, Long>,
QueryDslPredicateExecutor<AccountingExport> {

}
