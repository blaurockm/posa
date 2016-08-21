package net.buchlese.verw.repos;

import java.time.LocalDate;
import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrdelivery")
public interface SubscrDeliveryRepository extends JpaRepository<SubscrDelivery, Long> {

	List<SubscrDelivery> findByDeliveryDate(LocalDate date);

}
