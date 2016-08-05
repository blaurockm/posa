package net.buchlese.verw.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import net.buchlese.bofc.api.subscr.Subscriber;

@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface CustomerRepository extends JpaRepository<Subscriber, Long> {

    List<Subscriber> findByName(@Param("name") String firstName);

    List<Subscriber> findByDebitorId(int custId);
}
