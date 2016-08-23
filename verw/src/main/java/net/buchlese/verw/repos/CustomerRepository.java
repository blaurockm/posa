package net.buchlese.verw.repos;

import java.util.List;

import net.buchlese.bofc.api.subscr.QSubscriber;
import net.buchlese.bofc.api.subscr.Subscriber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "customer")
public interface CustomerRepository extends JpaRepository<Subscriber, Long>,
QueryDslPredicateExecutor<Subscriber>,QuerydslBinderCustomizer<QSubscriber> {

    List<Subscriber> findByName(@Param("name") String firstName);

    List<Subscriber> findByDebitorId(int custId);

    @Override
	default public void customize(QuerydslBindings bindings, QSubscriber bal) {
		bindings.bind(bal.name).first((path, value) -> path.likeIgnoreCase(value));
	}

}
