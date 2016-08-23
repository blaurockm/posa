package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscrIntervalDeliveryDAO extends AbstractDAO<SubscrIntervalDelivery> {

	@Inject
	public JpaSubscrIntervalDeliveryDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrIntervalDelivery findById(Long id) {
        return get(id);
    }

    public void create(SubscrIntervalDelivery person) {
        currentSession().save(person);
    }

    public void update(SubscrIntervalDelivery person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrIntervalDelivery> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
