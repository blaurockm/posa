package net.buchlese.bofc.gui;

import net.buchlese.bofc.core.Voucher.VoucherModel;
import net.buchlese.bofc.core.shift.ShiftCalModel;

public class BofcModel {

	public ShiftCalModel getShiftCalModel() {
		return new ShiftCalModel();
	}

	public VoucherModel getVoucherModel() {
		return new VoucherModel();
	}
	
}
