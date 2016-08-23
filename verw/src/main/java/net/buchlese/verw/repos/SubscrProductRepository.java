package net.buchlese.verw.repos;

import net.buchlese.bofc.api.subscr.QSubscrProduct;
import net.buchlese.bofc.api.subscr.SubscrProduct;
import net.buchlese.bofc.api.subscr.Subscriber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrproduct")
public interface SubscrProductRepository extends JpaRepository<SubscrProduct, Long>,
QueryDslPredicateExecutor<Subscriber>,QuerydslBinderCustomizer<QSubscrProduct> {

    @Override
	default public void customize(QuerydslBindings bindings, QSubscrProduct bal) {
		bindings.bind(bal.name).first((path, value) -> path.likeIgnoreCase(value));
	}

}
