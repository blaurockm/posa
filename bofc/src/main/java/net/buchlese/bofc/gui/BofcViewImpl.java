package net.buchlese.bofc.gui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class BofcViewImpl extends CustomComponent  implements BofcViewIF {

	private final StatisticViewImpl statisticView;
	private final VoucherViewImpl voucherView;
	private final ShiftCalViewImpl shiftCalView;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BofcViewImpl() {
		setSizeFull();
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100f, Unit.PERCENTAGE);
		layout.setHeight(100f, Unit.PERCENTAGE);
		layout.setSpacing(true);
		
		shiftCalView = new ShiftCalViewImpl();
		layout.addComponent(shiftCalView);

		final VerticalLayout vert = new VerticalLayout();
		vert.setHeight(100f, Unit.PERCENTAGE);
		vert.setSpacing(true);
		
		statisticView = new StatisticViewImpl();
		vert.addComponent(statisticView);
		voucherView = new VoucherViewImpl();
		vert.addComponent(voucherView);
		
		layout.addComponent(vert);
		
		setCompositionRoot(layout);
	}

	@Override
	public ShiftCalViewIF getShiftCalView() {
		return shiftCalView;
	}

	@Override
	public VoucherViewIF getVoucherView() {
		return voucherView;
	}
	
}
