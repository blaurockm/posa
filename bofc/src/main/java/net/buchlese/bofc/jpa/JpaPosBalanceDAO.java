package net.buchlese.bofc.jpa;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.bofc.PosCashBalance;

public class JpaPosBalanceDAO extends AbstractDAO<PosCashBalance> {

	@Inject
	public JpaPosBalanceDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public PosCashBalance findById(Long id) {
        return get(id);
    }

    public void create(PosCashBalance person) {
        currentSession().save(person);
    }

    public void update(PosCashBalance person) {
        currentSession().saveOrUpdate(person);
    }

	public List<PosCashBalance> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
