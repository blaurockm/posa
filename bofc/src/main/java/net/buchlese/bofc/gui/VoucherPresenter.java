package net.buchlese.bofc.gui;

import net.buchlese.bofc.core.Voucher.VoucherModel;

public class VoucherPresenter {
	private final VoucherViewIF view;
	private final VoucherModel model;
	
	public VoucherPresenter(VoucherViewIF view,
			VoucherModel model) {
		this.view = view;
		this.model = model;
		this.view.setDisplay(model.getDefaultContainer());
	}

	public VoucherViewIF getView() {
		return view;
	}

	public VoucherModel getModel() {
		return model;
	}

}
