package net.buchlese.bofc.gui;

import net.buchlese.bofc.core.shift.ShiftCalModel;

public class ShiftCalPresenter {

	private final ShiftCalViewIF view;
	private final ShiftCalModel model;
	
	public ShiftCalPresenter(ShiftCalViewIF shiftCalView, ShiftCalModel shiftCalModel) {
		this.view = shiftCalView;
		this.model = shiftCalModel;
		shiftCalModel.defaultSelect();
		view.setDisplay(shiftCalModel.getContainer());
	}

	public ShiftCalViewIF getView() {
		return view;
	}

	public ShiftCalModel getModel() {
		return model;
	}

}
