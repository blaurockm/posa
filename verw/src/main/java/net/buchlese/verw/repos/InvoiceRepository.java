package net.buchlese.verw.repos;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.QPosInvoice;

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
		bindings.bind(inv.date).all((path, value) -> {
			Iterator<? extends LocalDate> it = value.iterator();
			LocalDate first = it.next();
			if (it.hasNext()) {
				LocalDate second = it.next();
				return path.between(first, second);
			} else {
				return path.after(first);
			}
		} );
		
	}

	PosInvoice findFirstByExportedAndPointidOrderByDateAsc(boolean b, Integer pointid);

	List<PosInvoice> findAllByExportedAndPointidAndDateLessThanEqualOrderByDateAsc(boolean b, Integer pointid, LocalDate maxTag);
	
	@Query("select debitorId from PosInvoice where customerId = :cust and pointid = :pi")
	Optional<Integer> mapDebitor(@Param("cust") Integer customerId, @Param("pi") Integer pointid);
}
