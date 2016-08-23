package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.Subscription;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscriptionDAO extends AbstractDAO<Subscription> {

	@Inject
	public JpaSubscriptionDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public Subscription findById(Long id) {
        return get(id);
    }

    public void create(Subscription person) {
        currentSession().save(person);
    }

    public void update(Subscription person) {
        currentSession().saveOrUpdate(person);
    }

	public List<Subscription> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
