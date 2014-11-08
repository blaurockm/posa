package net.buchlese.posa.api.bofc;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.buchlese.posa.core.PDFCashBalance;

import org.joda.time.DateTime;
import org.junit.Test;

public class PDFTest {


	@Test
	public void testCashBalancePDF() throws Exception {
		PosCashBalance bal = new PosCashBalance();
		bal.setAbschlussId("20141018");
		bal.setRevenue(123450l);
		bal.setAbsorption(75612l);
		Map<String,Long> cashIn = new HashMap<String, Long>();
		cashIn.put(" einz 1", 2312l);
		bal.setCashIn(cashIn);
		Map<String,Long> cashOut = new HashMap<String, Long>();
		cashOut.put(" ausz 2", 456l);
		bal.setCashOut(cashOut);
		bal.setGoodsOut(32123l);
		bal.setCashStart(323l);
		bal.setCashEnd(76542l);
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
		bal.setCouponTradeIn(21555l +1765l+1765l );
		sm = new HashMap<String, Long>();
		sm.put("Gutsch", 21555l);
		bal.setNewCoupon(sm);
		bal.setCouponTradeOut(21555l );

		sm = new HashMap<String, Long>();
		sm.put("books", 21555l);
		sm.put("nonbook", 2155l);
		sm.put("Tickets", 3600l);
		sm.put("Rubbellose", 500l);
		sm.put("Lotto", 1765l);
		sm.put("Schokolade", 860l);
		bal.setArticleGroupBalance(sm);
		
		bal.setTicketCount(23);

		PDFCashBalance pdfgen = new PDFCashBalance(bal);
		
		pdfgen.generatePDF(new FileOutputStream("testCashBalance.pdf"));
	}


}
