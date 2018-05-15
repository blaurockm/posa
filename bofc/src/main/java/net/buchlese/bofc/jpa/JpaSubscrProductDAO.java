package net.buchlese.bofc.jpa;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.time.LocalDate;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.SubscrProduct;

public class JpaSubscrProductDAO extends AbstractDAO<SubscrProduct> {

	@Inject
	public JpaSubscrProductDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrProduct findById(Long id) {
        return get(id);
    }

    public Long create(SubscrProduct person) {
        return (Long) currentSession().save(person);
    }

    public void update(SubscrProduct person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrProduct> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}
	public List<SubscrProduct> findAll() {
		Criteria c = criteria().addOrder(Order.asc("name"));
		return list(c);
	}

//	@SqlQuery("select * from subscrProduct where name like :q or to_char(id) like :q order by name")
	public List<SubscrProduct> querySubscrProducts(String query) {
		Criteria c = criteria().add(Restrictions.like("name", query)).addOrder(Order.asc("name"));
		return list(c);
	}

//		@SqlQuery("select * from subscrProduct where nextDelivery between :from and :till order by name")
	public List<SubscrProduct> getProductsForTimespan(LocalDate from, LocalDate till) {
		Criteria c = criteria().add(Restrictions.ge("nextDelivery", from)).add(Restrictions.le("nextDelivery", till)).addOrder(Order.asc("name"));
		return list(c);
	}

}
