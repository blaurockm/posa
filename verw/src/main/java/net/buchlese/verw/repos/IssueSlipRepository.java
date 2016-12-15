package net.buchlese.verw.repos;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.QPosIssueSlip;

@RepositoryRestResource(collectionResourceRel = "issueslips", path = "issueslip")
public interface IssueSlipRepository extends JpaRepository<PosIssueSlip, Long>, 
	QueryDslPredicateExecutor<PosIssueSlip>,
	QuerydslBinderCustomizer<QPosIssueSlip>{

	@Override
	@RestResource(exported = false)
	void delete(Long id);

	@Override
	@RestResource(exported = false)
	void delete(PosIssueSlip entity);

	@Override
	default public void customize(QuerydslBindings bindings, QPosIssueSlip inv) {
		bindings.bind(inv.name1).first((path, value) -> path.containsIgnoreCase(value));
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

	List<PosIssueSlip> findAllByPrintedOrderByDateAscNumberAsc(boolean b);
	
	List<PosIssueSlip> findByDebitorIdAndPayed(int debitorId, boolean payed);

}
