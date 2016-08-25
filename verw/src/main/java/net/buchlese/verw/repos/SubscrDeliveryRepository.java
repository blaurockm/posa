package net.buchlese.verw.repos;

import java.time.LocalDate;
import java.util.List;

import net.buchlese.bofc.api.subscr.QSubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrdelivery")
public interface SubscrDeliveryRepository extends JpaRepository<SubscrDelivery, Long>,
QueryDslPredicateExecutor<SubscrDelivery>,QuerydslBinderCustomizer<QSubscrDelivery> {

	List<SubscrDelivery> findByDeliveryDate(LocalDate date);

    @Override
	default public void customize(QuerydslBindings bindings, QSubscrDelivery bal) {
		bindings.bind(bal.subscriber().name).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.article().name).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.article().issueNo).first((path, value) -> path.eq(value));
	}

}