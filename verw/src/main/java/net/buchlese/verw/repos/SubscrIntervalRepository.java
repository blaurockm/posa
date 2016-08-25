package net.buchlese.verw.repos;

import java.util.List;

import net.buchlese.bofc.api.subscr.SubscrInterval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscrinterval")
public interface SubscrIntervalRepository extends JpaRepository<SubscrInterval, Long> {

	List<SubscrInterval> findTop5ByOrderByEndDateDesc();
}
