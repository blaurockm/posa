package net.buchlese.verw.repos;

import net.buchlese.bofc.api.subscr.SubscrProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrproduct")
public interface SubscrProductRepository extends JpaRepository<SubscrProduct, Long> {

}
