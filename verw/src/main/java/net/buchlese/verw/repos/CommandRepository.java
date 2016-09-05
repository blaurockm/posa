package net.buchlese.verw.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.buchlese.bofc.api.bofc.Command;
import net.buchlese.bofc.api.bofc.QCommand;

@RepositoryRestResource(path = "command")
public interface CommandRepository extends JpaRepository<Command, Long>, 
	QueryDslPredicateExecutor<Command>,
	QuerydslBinderCustomizer<QCommand>{

	@Override
	default public void customize(QuerydslBindings bindings, QCommand inv) {
		bindings.bind(inv.action).first((path, value) -> path.containsIgnoreCase(value));
	}

	public List<Command> findAllByPointidAndFetched(Integer pointid, boolean fetched);

}
