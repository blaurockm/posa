package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.Subscriber;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscriberDAO extends AbstractDAO<Subscriber> {

	@Inject
	public JpaSubscriberDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public Subscriber findById(Long id) {
        return get(id);
    }

    public void create(Subscriber person) {
        currentSession().save(person);
    }

    public void update(Subscriber person) {
        currentSession().saveOrUpdate(person);
    }

	public List<Subscriber> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
