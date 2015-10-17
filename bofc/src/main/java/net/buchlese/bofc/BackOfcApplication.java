package net.buchlese.bofc;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import net.buchlese.bofc.api.bofc.ArticleGroup;
import net.buchlese.bofc.api.shift.Employee;
import net.buchlese.bofc.api.shift.Shop;
import net.buchlese.bofc.jdbi.bofc.ShiftCalDAO;

import com.hubspot.dropwizard.guice.GuiceBundle;


public class BackOfcApplication extends Application<BackOfcConfiguration> {

	private GuiceBundle<BackOfcConfiguration> guiceBundle;
	
	public static void main(String[] args) throws Exception {
		new BackOfcApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<BackOfcConfiguration> bootstrap) {
	    guiceBundle = GuiceBundle.<BackOfcConfiguration>newBuilder()
	    	      .addModule(new BackOfcModule())
	    	      .enableAutoConfig(getClass().getPackage().getName())
	    	      .setConfigClass(BackOfcConfiguration.class)
	    	      .build();

	    bootstrap.addBundle(guiceBundle);
//
	    bootstrap.addBundle(new ViewBundle());
//		
//		// wir geben was her. unsere bilder und css - dinger
		bootstrap.addBundle(new AssetsBundle());
//		bootstrap.addBundle(new VaadinBundle(GuiServlet.class, "/gui/*", this));
		
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
	    
		ShiftCalDAO shiftCalDao = guiceBundle.getInjector().getInstance(ShiftCalDAO.class);
	    Employee.inject(shiftCalDao.fetchEmployees());
	    Shop.inject(shiftCalDao.fetchShops());
	    
	}

}
