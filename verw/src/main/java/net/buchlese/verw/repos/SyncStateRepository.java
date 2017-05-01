package net.buchlese.verw.repos;

import java.util.List;

import net.buchlese.bofc.api.bofc.PosSyncState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "possyncstates", path = "possyncstate")
public interface SyncStateRepository extends JpaRepository<PosSyncState, Long>, 
QueryDslPredicateExecutor<PosSyncState> {

	List<PosSyncState> findByPointid (int pointid);
}
