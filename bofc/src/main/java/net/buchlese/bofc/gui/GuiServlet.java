package net.buchlese.bofc.gui;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

@VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
public class GuiServlet extends VaadinServlet {

	private Environment environment;
	private Application<?> app;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	public Application<?> getApp() {
		return app;
	}

	public void setApp(Application<?> app) {
		this.app = app;
	}

	@Override
	protected VaadinServletService createServletService(
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {
        DropwizardVaadinServletService service = new DropwizardVaadinServletService(this,
                deploymentConfiguration);
        service.init();
        service.setApp(app);
        service.setEnvironment(environment);
        return service;
	}

	

}
