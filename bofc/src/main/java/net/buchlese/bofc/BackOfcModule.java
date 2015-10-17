package net.buchlese.bofc;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import net.buchlese.bofc.gui.GuiServlet;
import net.buchlese.bofc.gui.VaadinBundle;
import net.buchlese.bofc.jdbi.bofc.PayMethArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
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
	public VaadinBundle provideVaadinBundle(BackOfcApplication app) {
		return new VaadinBundle(GuiServlet.class, "/gui/*", app);
	}
	
	@Provides
	@Named("bofcdb")
	public DBI provideBofcDBI(BackOfcConfiguration configuration, Environment environment) {
		if (bofcDBI == null) {
			final DBIFactory bofcDbFactory = new DBIFactory();
			try {
				bofcDBI = bofcDbFactory.build(environment, configuration.getBackOfficeDB(), "bofcdb");
				bofcDBI.registerArgumentFactory(new TaxArgumentFactory());
				bofcDBI.registerArgumentFactory(new PayMethArgumentFactory());
				bofcDBI.registerArgumentFactory(new TxTypeArgumentFactory());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				bofcDBI = null;
			}
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
	public ShiftCalDAO provideShiftCalDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(ShiftCalDAO.class);
	}

}
