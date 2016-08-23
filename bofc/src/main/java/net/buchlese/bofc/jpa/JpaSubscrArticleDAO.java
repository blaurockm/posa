package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.SubscrArticle;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscrArticleDAO extends AbstractDAO<SubscrArticle> {

	@Inject
	public JpaSubscrArticleDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrArticle findById(Long id) {
        return get(id);
    }

    public void create(SubscrArticle person) {
        currentSession().save(person);
    }

    public void update(SubscrArticle person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrArticle> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
