package net.buchlese.bofc.jpa;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.bofc.PosTicket;

public class JpaPosTicketDAO extends AbstractDAO<PosTicket> {

	public JpaPosTicketDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public PosTicket findById(Long id) {
        return get(id);
    }

    public void create(PosTicket person) {
        currentSession().save(person);
    }

//	@SqlQuery("select * from posticket where belegnr = :belegnr")
	public List<PosTicket> findByBelegNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("belegNr", belegnr ));
		return list(c);
	}

}
