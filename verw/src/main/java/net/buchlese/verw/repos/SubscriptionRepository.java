package net.buchlese.verw.repos;

import net.buchlese.bofc.api.subscr.QSubscription;
import net.buchlese.bofc.api.subscr.Subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscription")
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>,
QueryDslPredicateExecutor<Subscription>,QuerydslBinderCustomizer<QSubscription> {

    @Override
	default public void customize(QuerydslBindings bindings, QSubscription bal) {
		bindings.bind(bal.subscriber().name).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.product().name).first((path, value) -> path.likeIgnoreCase(value));
	}

}
