package net.buchlese.bofc.gui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class VoucherViewImpl extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public VoucherViewImpl() {
		setSizeFull();
		setId("voucherView");
		final VerticalLayout layout = new VerticalLayout();
		layout.setHeight(100f, Unit.PERCENTAGE);
		
		Label header = new Label("Kassenbelege");
		header.setSizeUndefined();
		layout.addComponent(header);
		layout.setComponentAlignment(header,  Alignment.TOP_CENTER);
		
		
		setCompositionRoot(layout);
	}

}
