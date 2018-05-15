package net.buchlese.bofc.jpa;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.Subscriber;

public class JpaSubscriberDAO extends AbstractDAO<Subscriber> {

	@Inject
	public JpaSubscriberDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public Subscriber findById(Long id) {
        return get(id);
    }

    public Long create(Subscriber person) {
        return (Long) currentSession().save(person);
    }

    public void update(Subscriber person) {
        currentSession().saveOrUpdate(person);
    }

	public List<Subscriber> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

	public List<Subscriber> findAll() {
		Criteria c = criteria();
		return list(c);
	}

//	@SqlQuery("select * from subscriber where name1 like :q or name2 like :q or to_char(customerId) like :q order by name1")
	public List<Subscriber> querySubscribers(String query) {
		Criteria c = criteria().add(Restrictions.like("name1", query)).addOrder(Order.asc("name1"));
		return list(c);
	}
	
}
