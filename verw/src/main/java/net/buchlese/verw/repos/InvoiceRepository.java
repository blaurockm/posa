package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.QPosInvoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "invoices", path = "invoice")
public interface InvoiceRepository extends JpaRepository<PosInvoice, Long>,
											QueryDslPredicateExecutor<PosInvoice>,
											QuerydslBinderCustomizer<QPosInvoice>{
	  @Override
	  @RestResource(exported = false)
	  void delete(Long id);

	  @Override
	  @RestResource(exported = false)
	  void delete(PosInvoice entity);

	  @Override
	  default public void customize(QuerydslBindings bindings, QPosInvoice inv) {
		  bindings.bind(inv.name1).first((path, value) -> path.likeIgnoreCase(value));
	  }
}
