package net.buchlese.posa.jdbi.pos;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.testing.junit.DropwizardAppRule;

import java.io.File;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import net.buchlese.posa.PosAdapterApplication;
import net.buchlese.posa.PosAdapterConfiguration;
import net.buchlese.posa.api.pos.KassenVorgang;
import net.buchlese.posa.core.SynchronizePosTx;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import com.google.common.io.Resources;

@Ignore
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

	@Ignore
	@Test
	public void testReadFromDatabase() throws Exception {
	    final DBIFactory posDbfactory = new DBIFactory();
	    final DBI posDBI = posDbfactory.build(RULE.getEnvironment(), RULE.getConfiguration().getPointOfSaleDB(), "posdb");
	    final KassenVorgangDAO vorgangDao = posDBI.onDemand(KassenVorgangDAO.class);
//	    final KassenBelegDAO belegDao = posDBI.onDemand(KassenBelegDAO.class);
//	    final KassenAbschlussDAO abschlussDao = posDBI.onDemand(KassenAbschlussDAO.class);

	    List<KassenVorgang> vorgs = vorgangDao.fetchForBeleg(2701741, 1);
		System.out.println(
				vorgs.stream().map(KassenVorgang::getGesamt).map(b -> b.movePointRight(2).setScale(0, RoundingMode.HALF_UP)).
				map(String::valueOf).collect(Collectors.joining("   ")))
				;
		
		SynchronizePosTx s = new SynchronizePosTx(null, vorgangDao);
		
	    KassenVorgang vorg = vorgangDao.fetch(2701801, 4);
		
		s.createNewTx(vorg);
	}
	
}
