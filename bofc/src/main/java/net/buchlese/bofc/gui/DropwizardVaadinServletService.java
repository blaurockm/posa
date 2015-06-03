package net.buchlese.bofc.gui;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public class DropwizardVaadinServletService extends VaadinServletService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2953259780227210498L;
	
	private Environment environment;
	private Application<?> app;

	public DropwizardVaadinServletService(VaadinServlet servlet,
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
		super(servlet, deploymentConfiguration);
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Application<?> getApp() {
		return app;
	}

	public void setApp(Application<?> app) {
		this.app = app;
	}

}
