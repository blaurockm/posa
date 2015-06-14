package net.buchlese.bofc.gui;

import net.buchlese.bofc.core.Voucher.VoucherModel;

public class VoucherPresenter {
	private final VoucherViewIF view;
	private final VoucherModel model;
	
	public VoucherPresenter(VoucherViewIF view,
			VoucherModel model) {
		this.view = view;
		this.model = model;
		model.selectCurrentWeek();
		this.view.setDisplayFooter(model.getSum(1), model.getSum(2));
		this.view.setDisplay(model.getContainer());
	}

	public VoucherViewIF getView() {
		return view;
	}

	public VoucherModel getModel() {
		return model;
	}

}
