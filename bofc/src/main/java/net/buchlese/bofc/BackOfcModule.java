package net.buchlese.bofc;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.buchlese.bofc.core.NumberGenerator;
import net.buchlese.bofc.jdbi.SubscrCachedDAO;
import net.buchlese.bofc.jdbi.bofc.JodaLocalDateArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.JodaLocalDateMapper;
import net.buchlese.bofc.jdbi.bofc.MappingDAO;
import net.buchlese.bofc.jdbi.bofc.PayMethArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.bofc.jdbi.bofc.PosTicketDAO;
import net.buchlese.bofc.jdbi.bofc.PosTxDAO;
import net.buchlese.bofc.jdbi.bofc.ShiftCalDAO;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jdbi.bofc.TaxArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.TxTypeArgumentFactory;
import net.buchlese.bofc.jpa.JpaPosTicketDAO;

import org.hibernate.SessionFactory;
import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;

public class BackOfcModule extends AbstractModule {

	public BackOfcModule(Bootstrap<BackOfcConfiguration> bootstrap) {
		super();
		bootstrap.addBundle(hibernate);
	}

	private final HibernateBundle<BackOfcConfiguration> hibernate = new ScanningHibernateBundle<BackOfcConfiguration>("net.buchlese.bofc.api.bofc") {
	    @Override
	    public DataSourceFactory getDataSourceFactory(BackOfcConfiguration configuration) {
	        return configuration.getDataSourceFactory();
	    }
	};

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}
	private DBI bofcDBI;
	private NumberGenerator numService;

	
	@Provides
	@Named("jpaSessFactory")
	public SessionFactory provideHibernateSessionFactory(BackOfcConfiguration configuration, Environment environment) {
		return hibernate.getSessionFactory();
	}

	
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

	@Provides
	@Inject
	public NumberGenerator provideNumberService(@Named("bofcdb")DBI posDBI) {
		if (numService == null) {
			numService = new NumberGenerator(posDBI);
		}
		return numService;
	}

	@Provides @Inject
	public JpaPosTicketDAO provideJpaPosTicketDao(@Named("jpaSessFactory")SessionFactory sf) {
		return new JpaPosTicketDAO(sf);
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
	public MappingDAO provideMappingDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(MappingDAO.class);
	}
	@Provides @Inject
	public ShiftCalDAO provideShiftCalDAO(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(ShiftCalDAO.class);
	}
	
	private SubscrCachedDAO cache;
	
	@Provides @Inject
	public synchronized SubscrDAO provideSubscrDAO(@Named("bofcdb")DBI posDBI) {
		if (cache == null) {
			cache = new SubscrCachedDAO(posDBI.onDemand(SubscrDAO.class));
		}
		return cache;
	}

}
