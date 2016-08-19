package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.AccountingBalanceExport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "balexports", path = "balexport")
public interface BalanceExportRepository extends JpaRepository<AccountingBalanceExport, Long>,
QueryDslPredicateExecutor<AccountingBalanceExport> {

	// use service from controlle for delete (!)
	@Override
	@RestResource(exported = false)
	void delete(Long id);

	@Override
	@RestResource(exported = false)
	void delete(AccountingBalanceExport entity);

}
