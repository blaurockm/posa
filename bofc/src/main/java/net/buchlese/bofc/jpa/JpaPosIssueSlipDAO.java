package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.bofc.PosIssueSlip;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaPosIssueSlipDAO extends AbstractDAO<PosIssueSlip> {

	@Inject
	public JpaPosIssueSlipDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public PosIssueSlip findById(Long id) {
        return get(id);
    }

    public void create(PosIssueSlip person) {
        currentSession().save(person);
    }

    public void update(PosIssueSlip person) {
        currentSession().update(person);
    }

	public List<PosIssueSlip> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
