package net.buchlese.posa.api.bofc;

import io.dropwizard.testing.junit.DropwizardAppRule;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.core.PDFCashBalance;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.ClassRule;
import org.junit.Test;

import com.google.common.io.Resources;

public class PDFTest {

//	@ClassRule
//	public static final DropwizardAppRule<PosAdapterConfiguration> RULE = new DropwizardAppRule<PosAdapterConfiguration>(PosAdapterApplication.class, resourceFilePath("testconfig.yaml"));


	@Test
	public void testCashBalancePDF() throws Exception {
		PosCashBalance bal = new PosCashBalance();
		bal.setAbschlussId("20141018");
		bal.setRevenue(123450l);
		bal.setAbsorption(75612l);
		bal.setCashIn(666l);
		bal.setCashOut(767l);
		bal.setCreationtime(new DateTime());
		bal.setFirstCovered(new DateTime().minusHours(24));
		bal.setLastCovered(new DateTime().plusHours(24));
		bal.setFirstTimestamp(new DateTime().minusHours(7));
		bal.setLastTimestamp(new DateTime().plusHours(2));
		
		Map<Tax,Long> taxbal = new HashMap<Tax, Long>();
		taxbal.put(Tax.FULL, 555l);
		taxbal.put(Tax.HALF, 555l);
		taxbal.put(Tax.NONE, 555l);
		bal.setTaxBalance(taxbal);
		
		Map<PaymentMethod,Long> paybal = new HashMap<PaymentMethod, Long>();
		paybal.put(PaymentMethod.CASH, 21555l);
		paybal.put(PaymentMethod.TELE, 1765l);
		bal.setPaymentMethodBalance(paybal);

		Map<String,Long> sm = new HashMap<String, Long>();
		sm.put("uhuhuh", 21555l);
		sm.put("asdasd", 1765l);
		bal.setPayedInvoices(sm);

		sm = new HashMap<String, Long>();
		sm.put("GutschJacob", 21555l);
		sm.put("gutsch", 1765l);
		sm.put("Gutsch Dorn", 1765l);
		bal.setOldCoupon(sm);
		sm = new HashMap<String, Long>();
		sm.put("Gutsch", 21555l);
		bal.setNewCoupon(sm);

		sm = new HashMap<String, Long>();
		sm.put("books", 21555l);
		sm.put("nonbook", 2155l);
		sm.put("Tickets", 3600l);
		sm.put("Rubbellose", 500l);
		sm.put("Lotto", 1765l);
		sm.put("Schokolade", 860l);
		bal.setArticleGroupBalance(sm);
		
		bal.setTicketCount(23);

		byte[] pdf = PDFCashBalance.create(bal);

		FileUtils.writeByteArrayToFile(new File("testCashBalance.pdf"), pdf);
	}

	public static String resourceFilePath(String resourceClassPathLocation) {
		try {
			return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
