package net.buchlese.bofc.jpa.listeners;

import io.dropwizard.jackson.Jackson;
import net.buchlese.bofc.api.bofc.PosCashBalance;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PreInsertCashBalanceListener implements SaveOrUpdateEventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event)	throws HibernateException {
		if (event.getObject() instanceof PosCashBalance) {
			PosCashBalance bal = (PosCashBalance) event.getObject();
			ObjectMapper om = Jackson.newObjectMapper();
			try {
				bal.setBalanceSheet(om.writeValueAsString(bal));
			} catch (JsonProcessingException e) {
				System.err.println("cannot serialize CashBalance " + bal);
				e.printStackTrace();
				throw new HibernateException(e);
			}
		}
	}


}
