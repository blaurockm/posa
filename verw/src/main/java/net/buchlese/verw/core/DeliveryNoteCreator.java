package net.buchlese.verw.core;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.PosIssueSlipDetail;
import net.buchlese.bofc.api.subscr.Address;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.IssueSlipRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;

@Component
@Scope(value="singleton")
public class DeliveryNoteCreator {

	@Autowired SequenceGenerator numgen;
	@Autowired IssueSlipRepository issueSlipRepository;
	@Autowired SubscrDeliveryRepository subscrDeliveryRepository;

	public PosIssueSlip createDeliveryNote(SubscrDelivery fdeli) {
		PosIssueSlip rep = new PosIssueSlip();
		Subscriber s = fdeli.getSubscriber();
		Subscription sub1 = fdeli.getSubscription();
		rep.setCustomerId(s.getCustomerId());
		rep.setDeliveryDetails(new ArrayList<PosIssueSlipDetail>());
		rep.setDetails(new ArrayList<PosInvoiceDetail>());
		Address addr = sub1.getDeliveryAddress() != null ? sub1.getDeliveryAddress() : s.getInvoiceAddress();
		rep.setName1(addr.getName1());
		rep.setName2(addr.getName2());
		rep.setName3(addr.getName3());
		rep.setStreet(addr.getStreet());
		rep.setCity(addr.getPostalcode() + " " + addr.getCity());
		rep.setDeliveryFrom(fdeli.getDeliveryDate());
		rep.setDeliveryTill(fdeli.getDeliveryDate());
		rep.setPointid(s.getPointid());
		String nummernkreis = "deliveryNotes" + s.getPointid();
		Long nextNumber = numgen.getNextNumber(nummernkreis);
		if (nextNumber == null) {
			throw new IllegalAccessError("Nummernkreis " + nummernkreis + " ist nicht verfügbar");
		}
		rep.setNumber(String.valueOf(nextNumber));
		rep.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
		// alle Lieferungen für diesen Tag
		for (SubscrDelivery deli : subscrDeliveryRepository.findByDeliveryDate(fdeli.getDeliveryDate())) {
			if (deli.getSubscriber().equals(fdeli.getSubscriber())) {
				Subscription sub = deli.getSubscription();
				if (sub.getDeliveryInfo1() != null && sub.getDeliveryInfo1().isEmpty() == false) {
					PosInvoiceDetail det = new  PosInvoiceDetail();
					det.setTextonly(true);
					det.setText(sub.getDeliveryInfo1());
					rep.getDetails().add(det);
				}
				if (sub.getDeliveryInfo2() != null && sub.getDeliveryInfo2().isEmpty() == false) {
					PosInvoiceDetail det = new  PosInvoiceDetail();
					det.setTextonly(true);
					det.setText(sub.getDeliveryInfo2());
					rep.getDetails().add(det);
				}
				PosInvoiceDetail det = new  PosInvoiceDetail();
				det.setTextonly(false);
				det.setQuantity(deli.getQuantity());
				det.setText(deli.getArticleName());
				rep.getDetails().add(det);
				PosIssueSlipDetail pisd = new PosIssueSlipDetail();
				pisd.setDelivery(deli);
				pisd.setWeight(0.1);
				pisd.setQuantity(deli.getQuantity());
				rep.getDeliveryDetails().add(pisd);
			}
		}
		recordDeliveryNoteOnDeliveries(rep);
		issueSlipRepository.save(rep);
		return rep;
	}

	/**
	 * festschreiben einer Rechnung bei den Abos die dvon der Rechnung betroffen sind.
	 * Die Rechnung selbst wird nicht angefasst
	 * @param dao
	 * @param inv
	 */
	private void recordDeliveryNoteOnDeliveries(PosIssueSlip inv) {
		for (PosIssueSlipDetail pisd : inv.getDeliveryDetails()) {
			SubscrDelivery deli = pisd.getDelivery();
			deli.setSlipped(true);
			deli.setSlipNumber(inv.getNumber());
			subscrDeliveryRepository.save(deli);
		}
	}

}
