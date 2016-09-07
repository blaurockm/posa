package net.buchlese.verw.core;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.Settlement;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.InvoiceRepository;
import net.buchlese.verw.repos.IssueSlipRepository;
import net.buchlese.verw.repos.SettlementRepository;
import net.buchlese.verw.repos.SubscriptionRepository;

@Component
@Scope(value="singleton")
public class InvoiceCreator {

	@Autowired SequenceGenerator numgen;
	@Autowired IssueSlipRepository issueSlipRepository;
	@Autowired CustomerRepository customerRepository;
	@Autowired SubscriptionRepository subscriptionRepository;
	@Autowired InvoiceRepository invoiceRepository;
	@Autowired SettlementRepository settlementRepository;
	

	/**
	 * festschreiben einer Rechnung
	 * @param dao
	 * @param invDao
	 * @param inv
	 */
	@Transactional
	public PosInvoice fakturiereSettlement(Settlement sett) {
		PosInvoice inv = new PosInvoice();
		inv.setNumber(String.valueOf(numgen.getNextNumber("invoice" + inv.getPointid())));
		// formalia
		inv.setDate(sett.getDate()); 
		inv.setCreationTime(sett.getCreationTime());
		inv.setCustomerId(sett.getCustomerId());
		inv.setDebitorId(sett.getDebitorId());
		inv.setPointid(sett.getPointid());
		inv.setAmount(sett.getAmount());
		inv.setAmountFull(sett.getAmountFull());
		inv.setAmountHalf(sett.getAmountHalf());
		inv.setAmountNone(sett.getAmountNone());
		inv.setNetto(sett.getNetto());
		inv.setNettoFull(sett.getNettoFull());
		inv.setNettoHalf(sett.getNettoHalf());
		inv.setTax(sett.getTax());
		inv.setTaxHalf(sett.getTaxHalf());
		inv.setTaxFull(sett.getTaxFull());
		inv.setDeliveryFrom(sett.getDeliveryFrom());
		inv.setDeliveryTill(sett.getDeliveryTill());
		inv.setCollective(sett.isCollective());
		inv.setType(sett.getType());
		
		// Rechnungsadresse
		inv.setName1(sett.getName1());
		inv.setName2(sett.getName2());
		inv.setName3(sett.getName3());
		inv.setStreet(sett.getStreet());
		inv.setCity(sett.getCity());
		
		inv.setExported(false);
		inv.setPrinted(false);
		inv.setPayed(false);
		inv.setCancelled(false);
		
		inv.setDetails(sett.getDetails());
		inv.setAgreementDetails(sett.getAgreementDetails());
		
		inv.setPointid(sett.getPointid());
		
		
		invoiceRepository.save(inv);
		settlementRepository.delete(sett);
		return inv;
	}

	/**
	 * festschreiben einer Rechnung wieder rückgängig machen (bei den Abos die von der Rechnung betroffen sind.
	 * Die Rechnung selbst wird nicht angefasst
	 * @param dao
	 * @param inv
	 */
	public void cancelInvoice(PosInvoice inv) {
		unrecordInvoiceOnAgreements(inv);
		inv.setCancelled(true);
		if (inv.isExported()) {
			// oh weh. wir müssen eine stornorechnung erzeugen.
		}
		invoiceRepository.save(inv);
	}
	
	public void unrecordInvoiceOnAgreements(PosInvoice inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = subscriptionRepository.findOne(iad.getAgreementId());
				sub.setPayedUntil(iad.getDeliveryFrom().minusDays(1));
				if (iad.getPayType() != null && iad.getPayType().equals(PayIntervalType.EACHDELIVERY)) {
					
//	TODO				dao.resetDetailsOfInvoice(iad.getDeliveryIds());
				} else {
//	TODO				dao.resetIntervalDetailsOfInvoice(iad.getDeliveryIds());
				}
				subscriptionRepository.save(sub);
			} else {
				PosIssueSlip slip = issueSlipRepository.findOne(iad.getAgreementId());
				slip.setPayed(Boolean.FALSE);
				issueSlipRepository.save(slip); // TODO wir sollten den auch im Libras als unbezahlt markieren
			}
		}
	}
	
	/**
	 * festschreiben einer Rechnung bei den Abos die dvon der Rechnung betroffen sind.
	 * Die Rechnung selbst wird nicht angefasst
	 * @param dao
	 * @param inv
	 */
	private void recordInvoiceOnAgreements(PosInvoice inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = subscriptionRepository.findOne(iad.getAgreementId());
				sub.setPayedUntil(iad.getDeliveryTill());
				if (iad.getPayType() != null && iad.getPayType().equals(PayIntervalType.EACHDELIVERY)) {
//	TODO				dao.recordDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
				} else {
//	TODO				dao.recordIntervalDetailsOnInvoice(iad.getDeliveryIds(), inv.getNumber());
				}
				subscriptionRepository.save(sub);
			} else {
				PosIssueSlip slip = issueSlipRepository.findOne(iad.getAgreementId());
				slip.setPayed(Boolean.TRUE);
				issueSlipRepository.save(slip);
			}
		}
	}



}
