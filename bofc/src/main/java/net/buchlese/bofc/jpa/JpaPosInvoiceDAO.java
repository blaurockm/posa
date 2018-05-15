package net.buchlese.bofc.jpa;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscription;

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

    public void update(PosInvoice person) {
        currentSession().update(person);
    }

	public List<PosInvoice> findByNumber(String belegnr) {
		Criteria c = criteria().add(Restrictions.eq("number", belegnr ));
		return list(c);
	}

//	@SqlQuery("select * from posInvoice where debitor = :debitorId order by number desc")
	public List<PosInvoice> findByDebitorNumber(int debitor) {
		Criteria c = criteria().add(Restrictions.eq("debitorId", debitor)).addOrder( Order.desc("number"));
		return list(c);
	}
	
//	@SqlQuery("select * from posInvoice where pointid = :pointid and invDate > :date order by number asc")
	public List<PosInvoice> getSubscrInvoices(int id, Date  num) {
		Criteria c = criteria().add(Restrictions.eq("pointid", id)).add(Restrictions.gt("date", num)).addOrder(Order.asc("number"));
		return list(c);
	}

//	@SqlQuery("select invoiceNumber from subscrDelivery where subscriptionId = :subid and invoiceNumber is not null")
	public List<PosInvoice> getInvoicesForDetails(Subscription id) {
		@SuppressWarnings("rawtypes")
		List invNum =  currentSession().createCriteria(SubscrDelivery.class).
				setProjection(Projections.property("invoiceNumber")).
				add(Restrictions.eq("subscription", id)).
				add(Restrictions.isNotNull("invoiceNumber")).list();
		if (invNum.isEmpty()) {
			return new ArrayList<>();
		}					
		Criteria c = criteria().add(Restrictions.in("number", invNum));
		return list(c);
	}

	public List<PosInvoice> getInvoicesForIntervalDetails(Subscription id) {
		@SuppressWarnings("rawtypes")
		List invNum =  currentSession().createCriteria(SubscrIntervalDelivery.class).
				setProjection(Projections.property("invoiceNumber")).
				add(Restrictions.eq("subscription", id)).
				add(Restrictions.isNotNull("invoiceNumber")).list();
		if (invNum.isEmpty()) {
			return Collections.emptyList();
		}					
		Criteria c = criteria().add(Restrictions.in("number", invNum));
		return list(c);
	}


}
