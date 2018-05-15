package net.buchlese.bofc.jpa;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.bofc.PosIssueSlip;

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

//	@SqlQuery("select * from posissueslip where debitor = :debId and payed = 0 order by number asc")
	public List<PosIssueSlip> findIssueSlipsToAdd(int id) {
		Criteria c = criteria().add(Restrictions.eq("debitorId", id)).add(Restrictions.eq("payed", false)).addOrder(Order.asc("number"));
		return list(c);
	}

//	@SqlQuery("select * from posIssueSlip where debitor = :debitorId order by number desc")
	public List<PosIssueSlip> getSubscriberIssueSlips(int debId) {
		Criteria c = criteria().add(Restrictions.eq("debitorId", debId)).addOrder(Order.desc("number"));
		return list(c);
	}

}
