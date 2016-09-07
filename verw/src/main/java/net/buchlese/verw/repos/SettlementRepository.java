package net.buchlese.verw.repos;

import java.time.LocalDate;
import java.util.Iterator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.buchlese.bofc.api.bofc.QSettlement;
import net.buchlese.bofc.api.bofc.Settlement;

@RepositoryRestResource(path = "settlement")
public interface SettlementRepository extends JpaRepository<Settlement, Long>,
QueryDslPredicateExecutor<Settlement>,
QuerydslBinderCustomizer<QSettlement>{
	

	@Override
	default public void customize(QuerydslBindings bindings, QSettlement inv) {
		bindings.bind(inv.name1).first((path, value) -> path.containsIgnoreCase(value));
		bindings.bind(inv.date).all((path, value) -> {
			Iterator<? extends LocalDate> it = value.iterator();
			LocalDate first = it.next();
			if (it.hasNext()) {
				LocalDate second = it.next();
				return path.between(first, second);
			} else {
				return path.goe(first);
			}
		} );
		
	}

}
