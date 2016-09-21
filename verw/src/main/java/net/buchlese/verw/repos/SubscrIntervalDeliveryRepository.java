package net.buchlese.verw.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.buchlese.bofc.api.subscr.QSubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscription;

@RepositoryRestResource(path = "subscrintervaldelivery")
public interface SubscrIntervalDeliveryRepository extends JpaRepository<SubscrIntervalDelivery, Long>,
QuerydslBinderCustomizer<QSubscrIntervalDelivery> {

	List<SubscrIntervalDelivery> findBySubscriptionAndPayed(Subscription sub, boolean b);

    @Override
	default public void customize(QuerydslBindings bindings, QSubscrIntervalDelivery bal) {
		bindings.bind(bal.subscriber().name).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.interval().name).first((path, value) -> path.likeIgnoreCase(value));
	}

}
