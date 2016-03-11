package net.buchlese.bofc;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import net.buchlese.bofc.jdbi.bofc.JodaLocalDateArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.JodaLocalDateMapper;
import net.buchlese.bofc.jdbi.bofc.PayMethArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.PosTicketDAO;
import net.buchlese.bofc.jdbi.bofc.PosTxDAO;
import net.buchlese.bofc.jdbi.bofc.ShiftCalDAO;
import net.buchlese.bofc.jdbi.bofc.TaxArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.TxTypeArgumentFactory;

import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class BackOfcModule extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}
	private DBI bofcDBI;
	
	@Provides
	@Named("bofcdb")
	public DBI provideBofcDBI(BackOfcConfiguration configuration, Environment environment) {
		if (bofcDBI == null) {
			final DBIFactory bofcDbFactory = new DBIFactory();
			bofcDBI = bofcDbFactory.build(environment, configuration.getBackOfficeDB(), "bofcdb");
			bofcDBI.registerArgumentFactory(new TaxArgumentFactory());
			bofcDBI.registerArgumentFactory(new PayMethArgumentFactory());
			bofcDBI.registerArgumentFactory(new TxTypeArgumentFactory());
			bofcDBI.registerArgumentFactory(new JodaLocalDateArgumentFactory());
			bofcDBI.registerMapper(new JodaLocalDateMapper());
		}
		return bofcDBI;
	}

	@Provides @Inject
	public PosTxDAO providePosTxDao(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosTxDAO.class);
	}

	@Provides @Inject
	public PosTicketDAO providePosTicketDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosTicketDAO.class);
	}
	@Provides @Inject
	public PosCashBalanceDAO providePosCashBalanceDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosCashBalanceDAO.class);
	}
	@Provides @Inject
	public PosInvoiceDAO providePosInvoiceDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosInvoiceDAO.class);
	}
	@Provides @Inject
	public ShiftCalDAO provideShiftCalDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(ShiftCalDAO.class);
	}

}
