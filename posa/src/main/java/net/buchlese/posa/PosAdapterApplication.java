package net.buchlese.posa;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.core.H2TcpServerManager;
import net.buchlese.posa.core.SendTimer;
import net.buchlese.posa.core.SyncTimer;
import net.buchlese.posa.jdbi.bofc.PayMethArgumentFactory;
import net.buchlese.posa.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;
import net.buchlese.posa.jdbi.bofc.PosTxDAO;
import net.buchlese.posa.jdbi.bofc.TaxArgumentFactory;
import net.buchlese.posa.jdbi.bofc.TxTypeArgumentFactory;
import net.buchlese.posa.jdbi.pos.KassenAbschlussDAO;
import net.buchlese.posa.jdbi.pos.KassenBelegDAO;
import net.buchlese.posa.jdbi.pos.KassenVorgangDAO;
import net.buchlese.posa.resources.AppResource;
import net.buchlese.posa.resources.KassenAbschlussResource;
import net.buchlese.posa.resources.KassenBelegResource;
import net.buchlese.posa.resources.KassenVorgangResource;
import net.buchlese.posa.resources.PosCashBalanceResource;
import net.buchlese.posa.resources.PosTicketResource;
import net.buchlese.posa.resources.PosTxResource;
import net.buchlese.posa.resources.SynchronizeTask;

import org.skife.jdbi.v2.DBI;


public class PosAdapterApplication extends Application<PosAdapterConfiguration> {

	private ScheduledThreadPoolExecutor syncTimer;
	private Lock syncLock;
	
	public static Queue<PosCashBalance> resyncQueue = new ConcurrentLinkedQueue<PosCashBalance>();

	public static Queue<PosCashBalance> homingQueue = new ConcurrentLinkedQueue<PosCashBalance>();

	public static String posName;
	
	public static String getPosName() {
		return posName;
	}
	
	public static void main(String[] args) throws Exception {
		new PosAdapterApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<PosAdapterConfiguration> bootstrap) {
		bootstrap.addBundle(new ViewBundle());
		
		// wir geben was her. unsere bilder und css - dinger
		bootstrap.addBundle(new AssetsBundle());
		
		// wir migrieren immer nur eine DB
	    bootstrap.addBundle(new MigrationsBundle<PosAdapterConfiguration>() {
	    	public DataSourceFactory getDataSourceFactory(PosAdapterConfiguration configuration) {
	    		return configuration.getBackOfficeDB();
	    	}
	    });	
	}

	@Override
	public void run(PosAdapterConfiguration config, Environment environment) throws Exception {
		ArticleGroup.injectMappings(config.getArticleGroupMappings());
		posName = config.getName();
		// H2 mixed Mode
		environment.lifecycle().manage(new H2TcpServerManager());
		
	    final DBIFactory posDbfactory = new DBIFactory();
	    config.getPointOfSaleDB().setInitialSize(0); // lässt sich nicht über config einstellen
	    config.getPointOfSaleDB().setMinSize(0); // lässt sich nicht über config einstellen
	    
	    final DBI posDBI = posDbfactory.build(environment, config.getPointOfSaleDB(), "posdb");
	    final KassenVorgangDAO vorgangDao = posDBI.onDemand(KassenVorgangDAO.class);
	    final KassenBelegDAO belegDao = posDBI.onDemand(KassenBelegDAO.class);
	    final KassenAbschlussDAO abschlussDao = posDBI.onDemand(KassenAbschlussDAO.class);
	    
	    environment.jersey().register(new KassenVorgangResource(vorgangDao, posDBI));
	    environment.jersey().register(new KassenBelegResource(belegDao));
	    environment.jersey().register(new KassenAbschlussResource(abschlussDao));
	    
	    final DBIFactory bofcDbFactory = new DBIFactory();
	    final DBI bofcDBI = bofcDbFactory.build(environment, config.getBackOfficeDB(), "bofcdb");
	    bofcDBI.registerArgumentFactory(new TaxArgumentFactory());
	    bofcDBI.registerArgumentFactory(new PayMethArgumentFactory());
	    bofcDBI.registerArgumentFactory(new TxTypeArgumentFactory());
	    final PosTxDAO posTxDao = bofcDBI.onDemand(PosTxDAO.class);
	    final PosTicketDAO posTicketDao = bofcDBI.onDemand(PosTicketDAO.class);
	    final PosCashBalanceDAO posCashBalanceDao = bofcDBI.onDemand(PosCashBalanceDAO.class);
	    
	    environment.jersey().register(new PosTxResource(posTxDao, posCashBalanceDao));
	    environment.jersey().register(new PosTicketResource(posTicketDao, posCashBalanceDao));
	    environment.jersey().register(new PosCashBalanceResource(posCashBalanceDao, posTicketDao));

	    environment.jersey().register(new AppResource(config));

	    syncTimer = new ScheduledThreadPoolExecutor(10);
		syncLock = new ReentrantLock();
		SyncTimer syncTimerTask = new SyncTimer(syncLock, bofcDBI, posDBI);
		syncTimer.scheduleAtFixedRate(syncTimerTask, 5, 3, TimeUnit.MINUTES);
		SendTimer senTimerTask = new SendTimer(config);
		syncTimer.scheduleAtFixedRate(senTimerTask, 1, 1, TimeUnit.MINUTES);
	
		environment.admin().addTask(new SynchronizeTask(syncTimerTask));
	}

	
}
