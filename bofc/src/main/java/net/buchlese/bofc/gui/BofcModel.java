package net.buchlese.bofc.gui;

import net.buchlese.bofc.BackOfcApplication;
import net.buchlese.bofc.core.Voucher.VoucherModel;
import net.buchlese.bofc.core.shift.ShiftCalModel;

import com.vaadin.server.VaadinService;

public class BofcModel {

	public ShiftCalModel getShiftCalModel() {
		return new ShiftCalModel(((BackOfcApplication)((DropwizardVaadinServletService)VaadinService.getCurrent()).getApp()).getShiftCalDao());
	}

	public VoucherModel getVoucherModel() {
		return new VoucherModel(((BackOfcApplication)((DropwizardVaadinServletService)VaadinService.getCurrent()).getApp()).getPosCashBalanceDao());
	}
	
}
