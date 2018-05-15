package net.buchlese.bofc.view.subscr;

import java.util.List;
import java.util.Set;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriberDetailView extends AbstractBofcView{

	private final Subscriber sub;
	private Set<Subscription> subs;
	private List<PosInvoice> invs;
	private List<PosIssueSlip> slips;

	public SubscriberDetailView(SubscrDAO dao, Subscriber s) {
		super("subscriberdetail.ftl");
		this.sub = s;
		this.subs = s.getSubscriptions();
		this.subs.forEach(i -> i.getDeliveryInfo1());
		this.invs = dao.getSubscriberInvoices((int) sub.getDebitorId());
		this.slips = dao.getSubscriberIssueSlips(sub.getId().intValue());
	}


	public Set<Subscription> getSubscriptions() {
		return subs;
	}

	public Subscriber getSub() {
		return sub;
	}

	public List<PosIssueSlip> getIssueSlips() {
		return slips;
	}
	
	public List<PosInvoice> getInvoices() {
//		org.hibernate.Session s = sessFact.openSession();
//		ManagedSessionContext.bind(s);
//		JpaPosInvoiceDAO jpaDao = new JpaPosInvoiceDAO(sessFact);
//		List<PosInvoice> invs = new ArrayList<PosInvoice>();
//		if (sub.getDebitorId() > 0) {
//			invs.addAll(jpaDao.findByDebitorNumber(sub.getDebitorId()));
//		}
//		s.close();
		return invs;
	}

	public String product(Subscription s) {
		return s.getProduct().getName();
	}


}
