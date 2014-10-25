package net.buchlese.posa.jdbi.pos;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.io.File;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;

import org.junit.ClassRule;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import com.google.common.io.Resources;

public class TestBigDecimal {
	@ClassRule
	public static final DropwizardAppRule<PosAdapterConfiguration> RULE = new DropwizardAppRule<PosAdapterConfiguration>(PosAdapterApplication.class, resourceFilePath("testconfig.yaml"));

	public static String resourceFilePath(String resourceClassPathLocation) {
		try {
			return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testReadFromDatabase() throws Exception {
	    final DBIFactory posDbfactory = new DBIFactory();
	    final DBI posDBI = posDbfactory.build(RULE.getEnvironment(), RULE.getConfiguration().getPointOfSaleDB(), "posdb");
	    final KassenVorgangDAO vorgangDao = posDBI.onDemand(KassenVorgangDAO.class);
//	    final KassenBelegDAO belegDao = posDBI.onDemand(KassenBelegDAO.class);
//	    final KassenAbschlussDAO abschlussDao = posDBI.onDemand(KassenAbschlussDAO.class);

	    System.out.println(vorgangDao.fetchForBeleg(2700445, 1));
	}
	
}
