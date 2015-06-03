package net.buchlese.bofc.gui;

import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.common.collect.Maps;

public class VaadinBundle implements Bundle {
    private final Map<String, Class<? extends Servlet>> servlets = Maps.newLinkedHashMap();

    private final SessionHandler sessionHandler;
    
    private final Application<?> app;

    public VaadinBundle(Class<? extends Servlet> servlet, String pathSpec, Application<?> backOfcApplication) {
        this.sessionHandler = new SessionHandler();
        servlets.put(pathSpec, servlet);
        this.app = backOfcApplication;
    }

 
	@Override
	public void initialize(Bootstrap<?> bootstrap) {
		 bootstrap.addBundle(new AssetsBundle("/VAADIN", "/VAADIN", null, "vaadin"));
	}

	@Override
	public void run(Environment environment) {
        environment.servlets().setSessionHandler(sessionHandler);
        for (Map.Entry<String, Class<? extends Servlet>> servlet : servlets.entrySet()) {
        	GuiServlet guiServlet = new GuiServlet();
        	guiServlet.setEnvironment(environment);
        	guiServlet.setApp(app);
			ServletHolder holder = new ServletHolder("Vaadin-Servlet", guiServlet);
            environment.getApplicationContext().addServlet(holder, servlet.getKey());
        }
	}

}
