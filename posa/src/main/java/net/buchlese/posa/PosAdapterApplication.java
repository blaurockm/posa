package net.buchlese.posa;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.Command;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.SendableObject;
import net.buchlese.posa.core.CommandTimer;
import net.buchlese.posa.core.ConfigSyncTimer;
import net.buchlese.posa.core.SendTimer;
import net.buchlese.posa.core.SyncTimer;

import com.hubspot.dropwizard.guice.GuiceBundle;


public class PosAdapterApplication extends Application<PosAdapterConfiguration> {

	private ScheduledThreadPoolExecutor syncTimer;
	
	public static Queue<PosCashBalance> resyncQueue = new ConcurrentLinkedQueue<PosCashBalance>();

	public static Queue<SendableObject> homingQueue = new ConcurrentLinkedQueue<SendableObject>();

	public static Queue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();

	public static Queue<String> problemMessages = new ConcurrentLinkedQueue<String>();

	public static String posName;
	private GuiceBundle<PosAdapterConfiguration> guiceBundle;
	
	public static String getPosName() {
		return posName;
	}
	
	public static void main(String[] args) throws Exception {
		new PosAdapterApplication().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<PosAdapterConfiguration> bootstrap) {
	    guiceBundle = GuiceBundle.<PosAdapterConfiguration>newBuilder()
	    	      .addModule(new PosAdapterModule())
	    	      .enableAutoConfig(getClass().getPackage().getName())
	    	      .setConfigClass(PosAdapterConfiguration.class)
	    	      .build();

	    bootstrap.addBundle(guiceBundle);
		
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

	    syncTimer = new ScheduledThreadPoolExecutor(10);
		SyncTimer syncTimerTask = guiceBundle.getInjector().getInstance(SyncTimer.class);
		syncTimer.scheduleAtFixedRate(syncTimerTask, config.getSyncOffset(), config.getSyncInterval(), TimeUnit.MINUTES);

		SendTimer senTimerTask = guiceBundle.getInjector().getInstance(SendTimer.class);
		syncTimer.scheduleAtFixedRate(senTimerTask, config.getSendOffset(), config.getSendInterval(), TimeUnit.MINUTES);
		
		ConfigSyncTimer configTimerTask = guiceBundle.getInjector().getInstance(ConfigSyncTimer.class);
		syncTimer.scheduleAtFixedRate(configTimerTask, 3, 1440, TimeUnit.MINUTES);

		CommandTimer commandTimerTask = guiceBundle.getInjector().getInstance(CommandTimer.class);
		syncTimer.scheduleAtFixedRate(commandTimerTask, config.getCommandOffset(), config.getCommandInterval(), TimeUnit.MINUTES);

	}

	
}
