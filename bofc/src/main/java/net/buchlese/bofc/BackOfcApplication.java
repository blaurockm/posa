package net.buchlese.bofc;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import net.buchlese.bofc.api.bofc.ArticleGroup;
import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.Shop;
import net.buchlese.bofc.core.H2TcpServerManager;
import net.buchlese.bofc.gui.GuiServlet;
import net.buchlese.bofc.gui.VaadinBundle;
import net.buchlese.bofc.jdbi.bofc.PayMethArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.PosCashBalanceDAO;
import net.buchlese.bofc.jdbi.bofc.PosTicketDAO;
import net.buchlese.bofc.jdbi.bofc.PosTxDAO;
import net.buchlese.bofc.jdbi.bofc.ShiftCalDAO;
import net.buchlese.bofc.jdbi.bofc.TaxArgumentFactory;
import net.buchlese.bofc.jdbi.bofc.TxTypeArgumentFactory;
import net.buchlese.bofc.resources.AccrualMonthResource;
import net.buchlese.bofc.resources.AccrualWeekResource;
import net.buchlese.bofc.resources.AppResource;
import net.buchlese.bofc.resources.ArticleGroupResource;
import net.buchlese.bofc.resources.PosCashBalanceResource;

import org.skife.jdbi.v2.DBI;


public class BackOfcApplication extends Application<BackOfcConfiguration> {

	
	private PosTxDAO posTxDao;
	private PosCashBalanceDAO posCashBalanceDao;
	private ShiftCalDAO shiftCalDao;

	public static void main(String[] args) throws Exception {
		new BackOfcApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<BackOfcConfiguration> bootstrap) {
		bootstrap.addBundle(new ViewBundle());
		
		// wir geben was her. unsere bilder und css - dinger
		bootstrap.addBundle(new AssetsBundle());
		bootstrap.addBundle(new VaadinBundle(GuiServlet.class, "/gui/*", this));
		
		// wir migrieren immer nur eine DB
	    bootstrap.addBundle(new MigrationsBundle<BackOfcConfiguration>() {
	    	public DataSourceFactory getDataSourceFactory(BackOfcConfiguration configuration) {
	    		return configuration.getBackOfficeDB();
	    	}
	    });	
	}

	@Override
	public void run(BackOfcConfiguration config, Environment environment) throws Exception {
		ArticleGroup.injectMappings(config.getArticleGroupMappings());
		// H2 mixed Mode
		environment.lifecycle().manage(new H2TcpServerManager());

	    final DBIFactory bofcDbFactory = new DBIFactory();
	    final DBI bofcDBI = bofcDbFactory.build(environment, config.getBackOfficeDB(), "bofcdb");
	    bofcDBI.registerArgumentFactory(new TaxArgumentFactory());
	    bofcDBI.registerArgumentFactory(new PayMethArgumentFactory());
	    bofcDBI.registerArgumentFactory(new TxTypeArgumentFactory());
	    posTxDao = bofcDBI.onDemand(PosTxDAO.class);
	    final PosTicketDAO posTicketDao = bofcDBI.onDemand(PosTicketDAO.class);
	    posCashBalanceDao = bofcDBI.onDemand(PosCashBalanceDAO.class);
	    shiftCalDao = bofcDBI.onDemand(ShiftCalDAO.class);
	    
	    Employee.inject(shiftCalDao.fetchEmployees());
	    Shop.inject(shiftCalDao.fetchShops());
	    
	    environment.jersey().register(new PosCashBalanceResource(posCashBalanceDao, posTicketDao, posTxDao));
	    environment.jersey().register(new AccrualWeekResource(posCashBalanceDao));
	    environment.jersey().register(new AccrualMonthResource(posCashBalanceDao));
	    environment.jersey().register(new ArticleGroupResource());

	    environment.jersey().register(new AppResource(config, environment));
	}

	public PosTxDAO getPosTxDao() {
		return posTxDao;
	}

	public void setPosTxDao(PosTxDAO posTxDao) {
		this.posTxDao = posTxDao;
	}

	public PosCashBalanceDAO getPosCashBalanceDao() {
		return posCashBalanceDao;
	}

	public void setPosCashBalanceDao(PosCashBalanceDAO posCashBalanceDao) {
		this.posCashBalanceDao = posCashBalanceDao;
	}

	public ShiftCalDAO getShiftCalDao() {
		return shiftCalDao;
	}

	public void setShiftCalDao(ShiftCalDAO shiftCalDao) {
		this.shiftCalDao = shiftCalDao;
	}

}
