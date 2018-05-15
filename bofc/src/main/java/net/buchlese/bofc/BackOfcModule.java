package net.buchlese.bofc;

import org.hibernate.SessionFactory;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.buchlese.bofc.core.NumberGenerator;
import net.buchlese.bofc.jdbi.bofc.SubscrDAO;
import net.buchlese.bofc.jpa.JpaPosInvoiceDAO;
import net.buchlese.bofc.jpa.JpaPosIssueSlipDAO;
import net.buchlese.bofc.jpa.JpaPosTicketDAO;
import net.buchlese.bofc.jpa.JpaSequenceGenDAO;
import net.buchlese.bofc.jpa.JpaSubscrArticleDAO;
import net.buchlese.bofc.jpa.JpaSubscrDeliveryDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDAO;
import net.buchlese.bofc.jpa.JpaSubscrIntervalDeliveryDAO;
import net.buchlese.bofc.jpa.JpaSubscrProductDAO;
import net.buchlese.bofc.jpa.JpaSubscriberDAO;
import net.buchlese.bofc.jpa.JpaSubscriptionDAO;
import net.buchlese.bofc.jpa.listeners.MySessionFactoryFactory;

public class BackOfcModule extends AbstractModule {

	public BackOfcModule(Bootstrap<BackOfcConfiguration> bootstrap) {
		super();
		ImmutableList<Class<?>> allCl = new ImmutableList.Builder<Class<?>>().
		addAll(ScanningHibernateBundle.findEntityClassesFromDirectory("net.buchlese.bofc.api.bofc"))
		.addAll(ScanningHibernateBundle.findEntityClassesFromDirectory("net.buchlese.bofc.api.subscr")).build();
		
		hibernate = new HibernateBundle<BackOfcConfiguration>(allCl, new MySessionFactoryFactory()) {
		    @Override
		    public DataSourceFactory getDataSourceFactory(BackOfcConfiguration configuration) {
		        return configuration.getDataSourceFactory();
		    }
		};
		bootstrap.addBundle(hibernate);
	}

	private final HibernateBundle<BackOfcConfiguration> hibernate;

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}
	private NumberGenerator numService;

	
	@Provides
	public SessionFactory provideHibernateSessionFactory(BackOfcConfiguration configuration, Environment environment) {
		SessionFactory sessionFactory = hibernate.getSessionFactory();
		// jetzt per integrator only !?!
//		EventListenerRegistry listenerRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(EventListenerRegistry.class);
//		listenerRegistry.appendListeners(EventType.PRE_INSERT, new PreInsertCashBalanceListener());
//		listenerRegistry.appendListeners(EventType.POST_LOAD, new PostLoadBalanceListener());
		return sessionFactory;
	}

	@Provides
	@Inject
	public NumberGenerator provideNumberService(SessionFactory sf) {
		if (numService == null) {
			numService = new NumberGenerator(new JpaSequenceGenDAO(sf));
		}
		return numService;
	}

	@Provides @Inject
	public JpaPosTicketDAO provideJpaPosTicketDao(SessionFactory sf) {
		return new JpaPosTicketDAO(sf);
	}
	
	private SubscrDAO cache;
	
	@Provides @Inject
	public synchronized SubscrDAO provideSubscrDAO(SessionFactory sf) {
		if (cache == null) {
			cache = new SubscrDAO(new JpaSubscriberDAO(sf), new JpaSubscriptionDAO(sf), new JpaSubscrProductDAO(sf), new JpaSubscrArticleDAO(sf),
					new JpaSubscrDeliveryDAO(sf), new JpaSubscrIntervalDAO(sf), new JpaSubscrIntervalDeliveryDAO(sf), new JpaPosInvoiceDAO(sf), 
					new JpaPosIssueSlipDAO(sf));
		}
		return cache;
	}

}
