package net.buchlese.bofc.jpa;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrProduct;

public class JpaSubscrArticleDAO extends AbstractDAO<SubscrArticle> {

	@Inject
	public JpaSubscrArticleDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public SubscrArticle findById(Long id) {
		return get(id);
	}

	public Long create(SubscrArticle person) {
		return (Long) currentSession().save(person);
	}

	public void update(SubscrArticle person) {
		currentSession().saveOrUpdate(person);
	}

	public List<SubscrArticle> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}
	public List<SubscrArticle> findAll() {
		Criteria c = criteria();
		return list(c);
	}

	public SubscrArticle getNewestArticleOfProduct(SubscrProduct prod) {
		Long pj = (Long) criteria().setProjection( Projections.projectionList()
				.add( Projections.max("id"))).add(Restrictions.eq("product", prod)).uniqueResult();

		return get(pj);
	}

}
