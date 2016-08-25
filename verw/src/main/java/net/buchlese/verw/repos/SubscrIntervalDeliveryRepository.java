package net.buchlese.verw.repos;

import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrintervaldelivery")
public interface SubscrIntervalDeliveryRepository extends JpaRepository<SubscrIntervalDelivery, Long> {

}
