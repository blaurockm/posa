package net.buchlese.bofc.jpa.listeners;

import java.io.IOException;

import io.dropwizard.jackson.Jackson;
import net.buchlese.bofc.api.bofc.PosCashBalance;

import org.hibernate.event.internal.DefaultPostLoadEventListener;
import org.hibernate.event.spi.PostLoadEvent;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PostLoadBalanceListener extends DefaultPostLoadEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onPostLoad(PostLoadEvent event) {
		if (event.getEntity() instanceof PosCashBalance) {
			PosCashBalance orig = (PosCashBalance) event.getEntity();
			String balanceSheet = orig.getBalanceSheet();
			if (balanceSheet != null) {
				ObjectMapper om = Jackson.newObjectMapper();
				PosCashBalance bal;
				try {
					bal = om.readValue(balanceSheet, PosCashBalance.class);
					orig.setArticleGroupBalance(bal.getArticleGroupBalance());
					orig.setCashIn(bal.getCashIn());
					orig.setCashOut(bal.getCashOut());
					orig.setCreatedInvoices(bal.getCreatedInvoices());
					orig.setTickets(bal.getTickets());
					orig.setPaymentMethodBalance(bal.getPaymentMethodBalance());
					orig.setPayedInvoices(bal.getPayedInvoices());
					orig.setTaxBalance(bal.getTaxBalance());
					orig.setOldCoupon(bal.getOldCoupon());
					orig.setNewCoupon(bal.getNewCoupon());
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		super.onPostLoad(event);
	}

}
