package net.buchlese.bofc.jpa;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.resources.DeliveryNoteVO;

public class JpaSubscrDeliveryDAO extends AbstractDAO<SubscrDelivery> {

	@Inject
	public JpaSubscrDeliveryDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SubscrDelivery findById(Long id) {
        return get(id);
    }

    public void create(SubscrDelivery person) {
        currentSession().save(person);
    }

    public void update(SubscrDelivery person) {
        currentSession().saveOrUpdate(person);
    }

    public void delete(SubscrDelivery person) {
        currentSession().delete(person);
    }

	public List<SubscrDelivery> findByIdNr(long belegnr) {
		Criteria c = criteria().add(Restrictions.eq("id", belegnr ));
		return list(c);
	}
	public List<SubscrDelivery> findAll() {
		Criteria c = criteria();
		return list(c);
	}

	public List<SubscrDelivery> getDeliveries(Date now) {
		Criteria c = criteria().add(Restrictions.eq("deliveryDate", now )).addOrder(Order.asc("subscriber"));
		return list(c);
	}

	public List<SubscrDelivery> getDeliveriesSlipflag(boolean slipped) {
		Criteria c = criteria().add(Restrictions.eq("slipped", slipped));
		return list(c);
	}

	public List<SubscrDelivery> getDeliveriesPayflag(boolean payed) {
		Criteria c = criteria().add(Restrictions.eq("payed", payed));
		return list(c);
	}
	
	public List<SubscrDelivery> getDeliveriesForSubscriberSlipflag(Subscriber subscriber, Date now, boolean slipped) {
		Criteria c = criteria().add(Restrictions.eq("slipped", slipped))
				.add(Restrictions.eq("deliveryDate", now))
				.add(Restrictions.eq("subscriber", subscriber));
		return list(c);
	}

	public List<SubscrDelivery> getDeliveriesForNote(String noteNum) {
		Criteria c = criteria().add(Restrictions.eq("slipped", true))
				.add(Restrictions.eq("slipNumber", noteNum));
		return list(c);
	}
	

	public List<SubscrDelivery> getDeliveriesForSubscription(Subscription id) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id));
		return list(c);
	}

	public List<SubscrDelivery> getDeliveriesForSubscription(Subscription id, LocalDate from, LocalDate till) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id));
		return list(c);
	}

	public List<SubscrDelivery> getDeliveriesForSubscriptionPayflag(Subscription id, boolean payed) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id)).add(Restrictions.eq("payed", payed));
		return list(c);
	}

	public List<SubscrDelivery> getDeliveriesForSubscriptionSlipflag(Subscription id, boolean slipped) {
		Criteria c = criteria().add(Restrictions.eq("subscription", id)).add(Restrictions.eq("slipped", slipped));
		return list(c);
	}

//	@SqlQuery("select * from subscrDelivery where subscriptionId = :subid and deliveryDate = (select max(deliveryDate) from subscrDelivery where subscriptionId = :subid)")
	public SubscrDelivery getLastDeliveryForSubscription(Subscription id) {
		Date pj = (Date) criteria().setProjection( Projections.projectionList()
				          .add( Projections.max("deliveryDate"))).add(Restrictions.eq("subscription", id)).uniqueResult();

		Criteria c = criteria().add(Restrictions.eq("subscription", id)).add(Restrictions.eq("deliveryDate", pj));
		return (SubscrDelivery) c.uniqueResult();
	}

//	@SqlBatch("update subscrDelivery set invoiceNumber = :invNum, payed = true where id = :id")
	public void recordDetailsOnInvoice(Collection<SubscrDelivery> deliveryIds, String invNumber) {
		for (SubscrDelivery d : deliveryIds) {
			d.setInvoiceNumber(invNumber);
			d.setPayed(true);
		}
	}

//	@SqlBatch("update subscrDelivery set invoiceNumber = null, payed = false where id = :id")
	public void resetDetailsOfInvoice(Collection<SubscrDelivery> deliveryIds) {
		for (SubscrDelivery d : deliveryIds) {
			d.setInvoiceNumber(null);
			d.setPayed(false);
			currentSession().saveOrUpdate(d);
		}
	}

//	@SqlBatch("update subscrDelivery set slipNumber = :invNum, slipped = true where id = :id")
	public void recordDetailsOnSlip(Collection<SubscrDelivery> deliveries, String invNumber) {
		for (SubscrDelivery d : deliveries) {
			d.setSlipNumber(invNumber);
			d.setSlipped(true);
			currentSession().saveOrUpdate(d);
		}
	}

//	@SqlBatch("update subscrDelivery set slipNumber = null, slipped = false where id = :id")
	public void resetDetailsOfSlip(Collection<SubscrDelivery> deliveryIds) {
		for (SubscrDelivery d : deliveryIds) {
			d.setSlipNumber(null);
			d.setSlipped(false);
			currentSession().saveOrUpdate(d);
		}
	}

	public List<DeliveryNoteVO> getDeliveryNotes(int i, int j) {
		Criteria c = criteria().add(Restrictions.eq("slipped", true))
				.add(Restrictions.isNotNull("slipNumber")).addOrder(Order.desc("slipNumber"));
		
		List<DeliveryNoteVO> res = new ArrayList<>();
		ScrollableResults it = c.scroll(ScrollMode.FORWARD_ONLY);
		String oldnum = null;
		int count = 0;
		while(it.next()) {
			SubscrDelivery s = (SubscrDelivery)it.get(0);
			if (oldnum == null || oldnum.equals(s.getSlipNumber()) == false) {
				oldnum = s.getSlipNumber();
				DeliveryNoteVO dn = new DeliveryNoteVO();
				dn.setNumber(s.getSlipNumber());
				dn.setDate(s.getDeliveryDate());
				dn.setName1(s.getSubscriber().getName());
				dn.setDeliveryFrom(s.getDeliveryDate());
				dn.setDeliveryTill(s.getDeliveryDate());
				res.add(dn);
				if (++count > j) {
					break;
				}
			}
		}
		it.close();
		return res;
	}

}
