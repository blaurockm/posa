package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.SubscrDelivery;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscrDeliveryDAO extends AbstractDAO<SubscrDelivery> {

	@Inject
	public JpaSubscrDeliveryDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrDelivery findById(Long id) {
        return get(id);
    }

    public void create(SubscrDelivery person) {
        currentSession().save(person);
    }

    public void update(SubscrDelivery person) {
        currentSession().saveOrUpdate(person);
    }

    public void delete(SubscrDelivery person) {
        currentSession().delete(person);
    }

	public List<SubscrDelivery> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
