package net.buchlese.bofc.view.subscr;

import java.util.ArrayList;
import java.util.List;

import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.view.AbstractBofcView;

public class SubscriberDetailView extends AbstractBofcView{

	private final Subscriber sub;
	private List<Subscription> subs;
	private List<Subscription> invalidSubs;
	private List<PosInvoice> invs;
	private List<PosIssueSlip> slips;

	public SubscriberDetailView(SubscrDAO dao, Subscriber s) {
		super("subscriberdetail.ftl");
		this.sub = s;
		this.subs = new ArrayList<>();
		this.invalidSubs = new ArrayList<>();
		for (Subscription su : s.getSubscriptions()) {
			if (su.isValid()) {
				this.subs.add(su);
			} else {
				this.invalidSubs.add(su);
			}
		}
		this.invs = dao.getSubscriberInvoices((int) sub.getDebitorId());
		this.slips = dao.getSubscriberIssueSlips(sub.getId().intValue());
	}


	public List<Subscription> getSubscriptions() {
		return subs;
	}

	public List<Subscription> getInvalidSubscriptions() {
		return invalidSubs;
	}

	public Subscriber getSub() {
		return sub;
	}

	public List<PosIssueSlip> getIssueSlips() {
		return slips;
	}
	
	public List<PosInvoice> getInvoices() {
		return invs;
	}

	public String product(Subscription s) {
		return s.getProduct().getName();
	}


}
