package net.buchlese.verw.repos;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.QPosCashBalance;

@RepositoryRestResource(collectionResourceRel = "cashbalances", path = "cashbalance")
public interface BalanceRepository extends JpaRepository<PosCashBalance, Long>, 
QueryDslPredicateExecutor<PosCashBalance>,QuerydslBinderCustomizer<QPosCashBalance> {

	@Override
	@RestResource(exported = false)
	void delete(Long id);

	@Override
	@RestResource(exported = false)
	void delete(PosCashBalance entity);

	List<PosCashBalance> findByAbschlussId(String abschId);

	Page<PosCashBalance> findByPointidOrAbschlussIdLike(@Param("pointid") Optional<Integer> pointid, @Param("abschlussId") Optional<String> abschlussId, Pageable p);

	PosCashBalance findFirstByExportedAndPointidOrderByAbschlussIdAsc(@Param("exported")boolean b, @Param("pointid") int pointid);

	List<PosCashBalance> findAllByExportedAndPointidAndAbschlussIdLessThanEqualOrderByAbschlussIdAsc(@Param("exported")boolean b,  @Param("pointid") int pointid,  @Param("abschlussId") String maxAbschlussId);

	@Override
	default public void customize(QuerydslBindings bindings, QPosCashBalance bal) {
		bindings.bind(bal.abschlussId).first((path, value) -> path.likeIgnoreCase(value));
		bindings.bind(bal.firstCovered).all((path, value) -> {
			Iterator<? extends Timestamp> it = value.iterator();
			Timestamp first = it.next();
			if (it.hasNext()) {
				Timestamp second = it.next();
				return path.between(first, second);
			} else {
				return path.goe(first);
			}
		} );
	}

}
