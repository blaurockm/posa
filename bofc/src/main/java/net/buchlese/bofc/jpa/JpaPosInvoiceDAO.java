package net.buchlese.bofc.jpa;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;

import javax.inject.Inject;

import net.buchlese.bofc.api.bofc.PosInvoice;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class JpaPosInvoiceDAO extends AbstractDAO<PosInvoice> {

	@Inject
	public JpaPosInvoiceDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public PosInvoice findById(Long id) {
        return get(id);
    }

    public void create(PosInvoice person) {
        currentSession().save(person);
    }

	public List<PosInvoice> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

}
