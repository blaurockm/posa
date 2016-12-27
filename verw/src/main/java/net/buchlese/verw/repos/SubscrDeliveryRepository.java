package net.buchlese.verw.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.buchlese.bofc.api.subscr.QSubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscription;

@RepositoryRestResource(path = "subscrdelivery")
public interface SubscrDeliveryRepository extends JpaRepository<SubscrDelivery, Long>,
QueryDslPredicateExecutor<SubscrDelivery>,QuerydslBinderCustomizer<QSubscrDelivery> {

	List<SubscrDelivery> findByDeliveryDate(java.sql.Date date);

	List<SubscrDelivery> findBySubscriptionAndPayed(Subscription sub, boolean payed);

	List<SubscrDelivery> findByPayed(boolean payed);

	List<SubscrDelivery> findBySlipped(boolean slipped);

    @Override
	default public void customize(QuerydslBindings bindings, QSubscrDelivery bal) {
		bindings.bind(bal.subscriber().name).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.article().name).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.article().issueNo).first((path, value) -> path.eq(value));
	}

}
