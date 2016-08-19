package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.AccountingInvoiceExport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "invexports", path = "invexport")
public interface InvoiceExportRepository extends JpaRepository<AccountingInvoiceExport, Long>,
QueryDslPredicateExecutor<AccountingInvoiceExport> {

	// use service from controlle for delete (!)
	@Override
	@RestResource(exported = false)
	void delete(Long id);

	@Override
	@RestResource(exported = false)
	void delete(AccountingInvoiceExport entity);

}
