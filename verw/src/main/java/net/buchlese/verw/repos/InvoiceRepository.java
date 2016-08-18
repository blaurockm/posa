package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.PosInvoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "invoices", path = "invoice")
public interface InvoiceRepository extends JpaRepository<PosInvoice, Long>,
											QueryDslPredicateExecutor<PosInvoice> {
	  @Override
	  @RestResource(exported = false)
	  void delete(Long id);

	  @Override
	  @RestResource(exported = false)
	  void delete(PosInvoice entity);

}
