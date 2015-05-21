package net.buchlese.bofc.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by jangalinski on 21.12.14.
 */
@Title("Main UI")
@Theme("valo")
public class MainUI extends UI {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setHeight(100f,Unit.PERCENTAGE);
        setContent(layout);
        setSizeFull();

        final HorizontalLayout header = new HorizontalLayout();
        header.setWidth(100f, Unit.PERCENTAGE);
        header.setMargin(true);

        Label headline = new Label("<font size=6><b>Buchlese</b></font>", ContentMode.HTML);
		header.addComponent(headline);
		header.setExpandRatio(headline, 20f);
		
        Label gears = new Label("<font size=6>" + FontAwesome.GEAR.getHtml() + "</font>", ContentMode.HTML);
        gears.setWidthUndefined();
		header.addComponent(gears);
		header.setComponentAlignment(gears, Alignment.TOP_RIGHT);
		header.setExpandRatio(gears, 1f);
        
        layout.addComponent(header);
        
        BofcViewImpl view = new BofcViewImpl();
        BofcModel model = new BofcModel();
        
        new BofcPresenter(view, model);

        layout.addComponent(view);
        layout.setExpandRatio(view, 20f);
    }
}