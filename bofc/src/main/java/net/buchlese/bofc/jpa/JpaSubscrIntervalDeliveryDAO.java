package net.buchlese.bofc.jpa;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscription;

public class JpaSubscrIntervalDeliveryDAO extends AbstractDAO<SubscrIntervalDelivery> {

	@Inject
	public JpaSubscrIntervalDeliveryDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrIntervalDelivery findById(Long id) {
        return get(id);
    }

    public void create(SubscrIntervalDelivery person) {
        currentSession().save(person);
    }

    public void update(SubscrIntervalDelivery person) {
        currentSession().saveOrUpdate(person);
    }

	public List<SubscrIntervalDelivery> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}

	public void delete(SubscrIntervalDelivery del) {
        currentSession().delete(del);
	}
	public List<SubscrIntervalDelivery> findAll() {
		Criteria c = criteria();
		return list(c);
	}

	public List<SubscrIntervalDelivery> getIntervalDeliveriesUnrecorded() {
		Criteria c = criteria().add(Restrictions.eq("payed", false));
		return list(c);
	}
	
	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(Subscription id) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id));
		return list(c);
	}

	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscription(Subscription id, LocalDate from, LocalDate till) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id));
		return list(c);
	}

	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionPayflag(Subscription id, boolean payed) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id)).add(Restrictions.eq("payed", payed));
		return list(c);
	}

	public List<SubscrIntervalDelivery> getIntervalDeliveriesForSubscriptionSlipflag(Subscription id, boolean slipped) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id)).add(Restrictions.eq("slipped", slipped));
		return list(c);
	}

//	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and deliveryDate = (select max(deliveryDate) from subscrDelivery where subscriptionId = :subid)")
	public SubscrIntervalDelivery getLastIntervalDeliveryForSubscription(Subscription id) {
		Date pj = (Date) criteria().setProjection( Projections.projectionList()
				          .add( Projections.max("deliveryDate"))).add(Restrictions.eq("subscription", id)).uniqueResult();

		Criteria c = criteria().add(Restrictions.eq("subscription", id)).add(Restrictions.eq("deliveryDate", pj));
		List<SubscrIntervalDelivery> res = list(c);
		if (res.isEmpty()) {
			return null;
		}
		return res.get(0);
	}

	
//	@SqlBatch("update subscrIntervalDelivery set invoiceNumber = null, payed = false where id = :id")
	public void resetIntervalDetailsOfInvoice(Set<SubscrIntervalDelivery> deliveryIds) {
		for (SubscrIntervalDelivery d : deliveryIds) {
			d.setInvoiceNumber(null);
			d.setPayed(false);
			currentSession().saveOrUpdate(d);
		}
	}

	public void recordIntervalDetailsOnInvoice(Set<SubscrIntervalDelivery> deliveryIds, String invNumber) {
		for (SubscrIntervalDelivery d : deliveryIds) {
			d.setInvoiceNumber(invNumber);
			d.setPayed(true);
			currentSession().saveOrUpdate(d);
		}
	}

}
