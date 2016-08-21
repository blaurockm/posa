package net.buchlese.verw.repos;

import net.buchlese.bofc.api.subscr.Subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscription")
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
