package net.buchlese.bofc.jpa;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrProduct;

public class JpaSubscrIntervalDAO extends AbstractDAO<SubscrInterval> {

	@Inject
	public JpaSubscrIntervalDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrInterval findById(Long id) {
        return get(id);
    }

    public Long create(SubscrInterval person) {
        return (Long) currentSession().save(person);
    }

    public void update(SubscrInterval person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrInterval> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}
	public List<SubscrInterval> findAll() {
		Criteria c = criteria();
		return list(c);
	}

	public List<SubscrInterval> getIntervalsOfProduct(SubscrProduct prodid) {
		Criteria c = criteria().add(Restrictions.eq("product", prodid )).addOrder( Order.asc("id") );
		return list(c);
	}

	public SubscrInterval getNewestIntervalOfProduct(SubscrProduct prodid) {
		Long pj = (Long) criteria().setProjection( Projections.projectionList()
				.add( Projections.max("id"))).add(Restrictions.eq("product", prodid)).uniqueResult();
		if (pj == null) {
			return null;
		}
		return get(pj);
	}

}
