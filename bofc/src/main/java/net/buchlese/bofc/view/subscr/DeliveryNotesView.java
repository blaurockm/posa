package net.buchlese.bofc.view.subscr;

import java.util.List;

import net.buchlese.bofc.resources.DeliveryNoteVO;
import net.buchlese.bofc.view.AbstractBofcView;

public class DeliveryNotesView extends AbstractBofcView {

	private final List<DeliveryNoteVO> invoices;
	
	public DeliveryNotesView(List<DeliveryNoteVO> notes) {
		super("deliverynotes.ftl");
		this.invoices = notes;
	}

	public List<DeliveryNoteVO> getDeliveryNotes() {
		return invoices;
	}

}
