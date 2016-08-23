package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.SubscrInterval;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscrIntervalDAO extends AbstractDAO<SubscrInterval> {

	@Inject
	public JpaSubscrIntervalDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrInterval findById(Long id) {
        return get(id);
    }

    public void create(SubscrInterval person) {
        currentSession().save(person);
    }

    public void update(SubscrInterval person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrInterval> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
