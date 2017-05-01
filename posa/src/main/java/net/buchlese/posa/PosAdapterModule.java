package net.buchlese.posa;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.buchlese.posa.jdbi.bofc.DynamicStateDAO;
import net.buchlese.posa.jdbi.bofc.JodaLocalDateArgumentFactory;
import net.buchlese.posa.jdbi.bofc.JodaLocalDateMapper;
import net.buchlese.posa.jdbi.bofc.PayMethArgumentFactory;
import net.buchlese.posa.jdbi.bofc.PosArticleDAO;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosInvoiceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.bofc.TaxArgumentFactory;
import net.buchlese.posa.jdbi.bofc.TxTypeArgumentFactory;
import net.buchlese.posa.jdbi.pos.ArtikelDAO;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;
import net.buchlese.posa.jdbi.pos.KleinteilDAO;

import org.skife.jdbi.v2.DBI;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class PosAdapterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Lock.class).annotatedWith(Names.named("SyncLock")).to(ReentrantLock.class).in(Singleton.class);
	}

	private DBI posDBI;
	private DBI bofcDBI;

	@Provides
	@Named("posdb")
	public DBI providePosDBDBI(PosAdapterConfiguration configuration, Environment environment) {
		if ( posDBI == null ) {
			final DBIFactory posDbfactory = new DBIFactory();
			configuration.getPointOfSaleDB().setInitialSize(0); // lässt sich nicht über config einstellen
			configuration.getPointOfSaleDB().setMinSize(0); // lässt sich nicht über config einstellen
			try {
				posDBI = posDbfactory.build(environment, configuration.getPointOfSaleDB(), "posdb");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				posDBI = null;
			}
		}
		return posDBI;
	}

	@Provides
	@Named("bofcdb")
	public DBI provideBofcDBI(PosAdapterConfiguration configuration, Environment environment) {
		if (bofcDBI == null) {
			final DBIFactory bofcDbFactory = new DBIFactory();
			try {
				bofcDBI = bofcDbFactory.build(environment, configuration.getBackOfficeDB(), "bofcdb");
				bofcDBI.registerArgumentFactory(new TaxArgumentFactory());
				bofcDBI.registerArgumentFactory(new PayMethArgumentFactory());
				bofcDBI.registerArgumentFactory(new TxTypeArgumentFactory());
				bofcDBI.registerArgumentFactory(new JodaLocalDateArgumentFactory());
				bofcDBI.registerMapper(new JodaLocalDateMapper());
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
	public PosTicketDAO providePosTicketDao(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosTicketDAO.class);
	}
	@Provides @Inject
	public PosCashBalanceDAO providePosCashbalanceDao(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosCashBalanceDAO.class);
	}

	@Provides @Inject
	public PosInvoiceDAO providePosInvoiceDao(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosInvoiceDAO.class);
	}

	@Provides @Inject
	public DynamicStateDAO provideDynamicStateDao(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(DynamicStateDAO.class);
	}

	@Provides @Inject
	public PosArticleDAO providePosArticleDao(@Named("bofcdb")DBI posDBI) {
		return posDBI.onDemand(PosArticleDAO.class);
	}

	@Provides @Inject
	public KassenVorgangDAO provideVorgangsDao(@Named("posdb")DBI posDBI) {
		return posDBI.onDemand(KassenVorgangDAO.class);
	}

	@Provides @Inject
	public KassenBelegDAO provideBelegDao(@Named("posdb")DBI posDBI) {
		return posDBI.onDemand(KassenBelegDAO.class);
	}

	@Provides @Inject
	public ArtikelDAO provideArtikelDao(@Named("posdb")DBI posDBI) {
		return posDBI.onDemand(ArtikelDAO.class);
	}

	@Provides @Inject
	public KassenAbschlussDAO provideAbschlussDao(@Named("posdb")DBI posDBI) {
		return posDBI.onDemand(KassenAbschlussDAO.class);
	}

	@Provides @Inject
	public KleinteilDAO provideKleinteilDao(@Named("posdb")DBI posDBI) {
		return posDBI.onDemand(KleinteilDAO.class);
	}


}
