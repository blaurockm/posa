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
		bal.setCreationtime(new DateTime());
		bal.setFirstCovered(new DateTime().minusHours(24));
		bal.setLastCovered(new DateTime().plusHours(24));
		Map<Tax,Long> taxbal = new HashMap<Tax, Long>();
		taxbal.put(Tax.FULL, 555l);
		bal.setTaxBalance(taxbal);
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
