package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.subscr.SubscrProduct;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaSubscrProductDAO extends AbstractDAO<SubscrProduct> {

	@Inject
	public JpaSubscrProductDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrProduct findById(Long id) {
        return get(id);
    }

    public void create(SubscrProduct person) {
        currentSession().save(person);
    }

    public void update(SubscrProduct person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrProduct> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
