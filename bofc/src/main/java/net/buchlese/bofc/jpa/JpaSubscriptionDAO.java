package net.buchlese.bofc.jpa;

import java.sql.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.Subscription;

public class JpaSubscriptionDAO extends AbstractDAO<Subscription> {

	@Inject
	public JpaSubscriptionDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public Subscription findById(Long id) {
        return get(id);
    }

    public Long create(Subscription person) {
        return (Long) currentSession().save(person);
    }

    public void update(Subscription person) {
        currentSession().saveOrUpdate(person);
    }

	public List<Subscription> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

	public List<Subscription> findAll() {
		Criteria c = criteria();
		return list(c);
	}

//	@SqlQuery("select * from subscription where payedUntil is null or payedUntil < :till")
	public List<Subscription> getSubscriptionsForTimespan(Date from, Date till) {
		Criteria c = criteria().add(Restrictions.or(Restrictions.isNull("payedUntil"), Restrictions.le("payedUntil", till)));
		return list(c);
	}
	
}
