package net.buchlese.bofc.gui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class StatisticViewImpl extends CustomComponent implements StatisticViewIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public StatisticViewImpl() {
		setSizeFull();
		setId("statisticView");
		final VerticalLayout layout = new VerticalLayout();
		layout.setHeight(100f, Unit.PERCENTAGE);
		
		Label header = new Label("Statistiken");
		header.setSizeUndefined();
		layout.addComponent(header);
		layout.setComponentAlignment(header,  Alignment.TOP_CENTER);
		
		
		setCompositionRoot(layout);
	}

}
