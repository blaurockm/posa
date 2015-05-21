package net.buchlese.bofc.gui;

public class BofcPresenter {

	private final BofcViewIF view;
	private final BofcModel model;
	
	public BofcPresenter(BofcViewIF view, BofcModel model) {
		this.view = view;
		this.model = model;
		new ShiftCalPresenter(view.getShiftCalView(), model.getShiftCalModel());
//		new StatisticPresenter(view.getStatisticView(), model.getStatisticModel());
//		new VoucherPresenter(view.getVoucherView(), model.getVoucherModel());
	}

	public BofcViewIF getView() {
		return view;
	}

	public BofcModel getModel() {
		return model;
	}

}
