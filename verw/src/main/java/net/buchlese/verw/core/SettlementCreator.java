package net.buchlese.verw.core;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.buchlese.bofc.api.bofc.InvoiceAgrDetail;
import net.buchlese.bofc.api.bofc.PosInvoiceDetail;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.Settlement;
import net.buchlese.bofc.api.subscr.PayIntervalType;
import net.buchlese.bofc.api.subscr.SubscrArticle;
import net.buchlese.bofc.api.subscr.SubscrDelivery;
import net.buchlese.bofc.api.subscr.SubscrInterval;
import net.buchlese.bofc.api.subscr.SubscrIntervalDelivery;
import net.buchlese.bofc.api.subscr.Subscriber;
import net.buchlese.bofc.api.subscr.Subscription;
import net.buchlese.verw.repos.CustomerRepository;
import net.buchlese.verw.repos.IssueSlipRepository;
import net.buchlese.verw.repos.SettlementRepository;
import net.buchlese.verw.repos.SubscrArticleRepository;
import net.buchlese.verw.repos.SubscrDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalDeliveryRepository;
import net.buchlese.verw.repos.SubscrIntervalRepository;
import net.buchlese.verw.repos.SubscriptionRepository;

@Component
@Scope(value="singleton")
public class SettlementCreator {

	private  static class InvoiceSubComparator implements Comparator<Subscription> {
		@Override
		public int compare(Subscription o1, Subscription o2) {
			String di1sub1 = o1.getDeliveryInfo1();
			String di1sub2 = o2.getDeliveryInfo1();
			int idCompare = (int) (o1.getId() - o2.getId());
			if (di1sub1 == null) {
				return di1sub2 != null ? -1 : idCompare;
			} else if (di1sub1.equals(di1sub2)) {
				// compare second di
				String di2sub1 = o1.getDeliveryInfo2();
				String di2sub2 = o2.getDeliveryInfo2();
				if (di2sub1 == null) {
					return di2sub2 != null ? -1 : idCompare;
				} else if (di2sub1.equals(di2sub2)) {
					return idCompare;
				} 
				return di2sub1.compareTo(di2sub2);
			} 
			return di1sub1.compareTo(di1sub2);
		}

	}

	@Autowired SequenceGenerator numgen;
	@Autowired IssueSlipRepository issueSlipRepository;
	@Autowired CustomerRepository customerRepository;
	@Autowired SubscriptionRepository subscriptionRepository;
	@Autowired SubscrDeliveryRepository subscrDeliveryRepository;
	@Autowired SubscrIntervalDeliveryRepository subscrIntervalDeliveryRepository;
	@Autowired SubscrArticleRepository subscrArticleRepository;
	@Autowired SubscrIntervalRepository subscrIntervalRepository;
	@Autowired SettlementRepository settlementRepository;
	
	/**
	 * Sammelrechnung für einen Abonnenten
	 * @param subscriber
	 * @return
	 */
	public Settlement createCollectiveSettlement(Subscriber subscriber) {
		List<PosIssueSlip> issueSlips = issueSlipRepository.findByDebitorIdAndPayed(subscriber.getDebitorId(), false);
		Set<Subscription> subscriptions = subscriber.getSubscriptions();
		Settlement sett = createTemporaryInvoice(subscriber, subscriptions, issueSlips);
		sett.setCollective(true);
		return sett;
	}

	/**
	 * Einzelrechnung für ein Abo 
	 * @param sub
	 * @return
	 */
	public Settlement createSettlement(Subscription sub) {
		return createTemporaryInvoice(sub.getSubscriber(), Arrays.asList(sub), null);
	}

	/**
	 * Einzelrechnung wieder löschen, als ob nichts gewesen wäre.
	 * @param sett
	 */
	public void deleteSettlement(Settlement sett) {
		if (sett == null || sett.isMerged()) {
			return;
		}
		unrecordInvoiceOnAgreements(sett);
		settlementRepository.delete(sett);
	}
	
	
	/**
	 * die Abrechnung b
	 * Die Rechnung selbst wird nicht angefasst
	 * @param inv
	 */
	private void recordInvoiceOnAgreements(Settlement inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = iad.getSettledAgreement();
				sub.setPayedUntil(iad.getDeliveryTill());
				subscriptionRepository.save(sub);
			} else {
				PosIssueSlip slip = iad.getSettledDeliveryNote();
				slip.setPayed(Boolean.TRUE);
				issueSlipRepository.save(slip); // TODO wir sollten den auch im Libras als unbezahlt markieren
			}
		}
	}
	private void unrecordInvoiceOnAgreements(Settlement inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = iad.getSettledAgreement();
				sub.setPayedUntil(java.sql.Date.valueOf(iad.getDeliveryFrom().toLocalDate().minusDays(1)));
				if (iad.getPayType() != null && iad.getPayType().equals(PayIntervalType.EACHDELIVERY)) {
					iad.getDeliveries().forEach(x -> x.setSettDetail(null));
				} else {
					iad.getIntervalDeliveries().forEach(x -> x.setSettDetail(null));
				}
				subscriptionRepository.save(sub);
			} else {
				PosIssueSlip slip = iad.getSettledDeliveryNote();
				slip.setPayed(Boolean.FALSE);
				issueSlipRepository.save(slip); // TODO wir sollten den auch im Libras als unbezahlt markieren
			}
		}
	}
	

	/**
	 * erzeugt eine komplette Rechnung und legt sie als temporär ab.
	 * 
	 * @param dao
	 * @param subscriber
	 * @param subs
	 * @param issueSlips
	 * @param numgen
	 * @return
	 */
	private Settlement createTemporaryInvoice(Subscriber subscriber, Collection<Subscription> subs, List<PosIssueSlip> issueSlips) {
		final Settlement inv = createSettlementSkeleton(subscriber);
		// die Abos nach den Lieferhinweisen sortieren
		List<Subscription> sortedSubs = new ArrayList<>(subs);
		Collections.sort(sortedSubs, new InvoiceSubComparator());
		// details
		String lastDetailInfo = null;
		for(Subscription sub : subs) {
			if (PayIntervalType.EACHDELIVERY.equals(sub.getPaymentType())) {
				List<SubscrDelivery> deliveries = subscrDeliveryRepository.findBySubscriptionAndPayed(sub, false);
				lastDetailInfo = addDeliveriesToInvoice(sub, inv, deliveries, lastDetailInfo);
			} else {
				List<SubscrIntervalDelivery> intdeliveries = subscrIntervalDeliveryRepository.findBySubscriptionAndPayed(sub, false);
				lastDetailInfo = addIntervalDeliveriesToInvoice(sub, inv, intdeliveries, lastDetailInfo);
			}
		}
		if (issueSlips != null) {
			issueSlips.forEach(s -> addIssueSlipToInvoice(s, inv));
		}
		
		updateTaxValues(inv);
		
		settlementRepository.save(inv);
		
		recordInvoiceOnAgreements(inv);
		return inv;
	}

	/**
	 * errechnet die Steuerbeträge
	 * @param inv
	 */
	private void updateTaxValues(Settlement inv) {
		inv.setNettoHalf((long) (inv.getAmountHalf() / 1.07));
		inv.setTaxHalf(inv.getAmountHalf() - inv.getNettoHalf());
		inv.setTax(inv.getTaxHalf());
		if (inv.getAmountFull() != null) {
			inv.setNettoFull((long) (inv.getAmountFull() / 1.19));
			inv.setTaxFull(inv.getAmountFull() - inv.getNettoFull());
			inv.setTax(inv.getTax() + inv.getTaxFull());
		}
	}
	
	/**
	 * fügt einen Lieferschein aus Libras der Rechnung hinzu
	 * @param dao
	 * @param slip
	 * @param inv
	 * @return
	 */
	private InvoiceAgrDetail addIssueSlipToInvoice(PosIssueSlip slip, Settlement inv) {
		if (slip.isIncludeOnInvoice() == false) {
			return null;
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		iad.setSettledDeliveryNote(slip);
		iad.setType(InvoiceAgrDetail.TYPE.ISSUESLIP);
		addTextDetail(inv, "Artikel des Lieferscheins " + slip.getNumber() + " vom " + slip.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		for (PosInvoiceDetail detail : slip.getDetails()) {
			inv.addInvoiceDetail(detail);
		}
		iad.setDeliveryFrom(slip.getDate());
		iad.setDeliveryTill(slip.getDate());
		inv.addAgreementDetail(iad);
		return iad;
	}
	
	/**
	 * fügt die Lieferung eines Artikel zur Rechnung hinzu
	 * @param dao
	 * @param sub
	 * @param inv
	 * @param deliveries
	 * @return
	 */
	private String addDeliveriesToInvoice(Subscription sub, Settlement inv, List<SubscrDelivery> deliveries, String lastDetailinfo) {
		if (deliveries.isEmpty()) {
			// es gibt nix zu adden - back
			return null;
		}
		String newDetail = null;
		if (sub.getDeliveryInfo1() != null) {
			newDetail = sub.getDeliveryInfo1() + sub.getDeliveryInfo2();
		}
		if ( lastDetailinfo == null || lastDetailinfo.equals(newDetail) == false) {
			addTextDetail(inv, sub.getDeliveryInfo1());
			addTextDetail(inv, sub.getDeliveryInfo2());
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		iad.setSettledAgreement(sub);
		iad.setPayType(sub.getPaymentType());

		java.sql.Date from = null;
		java.sql.Date till = null;
		// details per Delivery;
		List<SubscrDelivery> recordedDeliv = new ArrayList<>();
		for (SubscrDelivery deliv : deliveries) {
			if (deliv.getTotal() == 0L) {
				continue;
			}
			SubscrArticle art = deliv.getArticle();
			inv.addInvoiceDetail(createInvoiceDetailForDelivery(deliv,art));
			// Versandkosten
			if (deliv.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, deliv.getShipmentCost());
			}
			if (from == null || deliv.getDeliveryDate().before(from)) {
				from = deliv.getDeliveryDate();
			}
			if (till == null || deliv.getDeliveryDate().after(till)) {
				till = deliv.getDeliveryDate();
			}
			deliv.setSettDetail(iad);
			recordedDeliv.add(deliv);
		}
		if (recordedDeliv.isEmpty() == false) {
			iad.setDeliveryFrom(from);
			iad.setDeliveryTill(java.sql.Date.valueOf(till.toLocalDate().withDayOfMonth(till.toLocalDate().lengthOfMonth())));  // immer der letzte des Monats
			inv.addAgreementDetail(iad);
		}
		return newDetail;
	}

	/**
	 * fügt die Lieferung eines Artikel zur Rechnung hinzu
	 * @param dao
	 * @param sub
	 * @param inv
	 * @param deliveries
	 * @return
	 */
	private String addIntervalDeliveriesToInvoice(Subscription sub, Settlement inv, List<SubscrIntervalDelivery> deliveries, String lastDetailinfo) {
		if (deliveries.isEmpty()) {
			// es gibt nix zu adden - back
			return null;
		}
		String newDetail = null;
		if (sub.getDeliveryInfo1() != null) {
			newDetail = sub.getDeliveryInfo1() + sub.getDeliveryInfo2();
		}
		if ( lastDetailinfo == null || lastDetailinfo.equals(newDetail) == false) {
			addTextDetail(inv, sub.getDeliveryInfo1());
			addTextDetail(inv, sub.getDeliveryInfo2());
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		iad.setSettledAgreement(sub);
		iad.setPayType(sub.getPaymentType());
		Date from = null;
		Date till = null;
		// details per Delivery;
		for (SubscrIntervalDelivery deliv : deliveries) {
			SubscrInterval interval = deliv.getInterval();
			inv.addInvoiceDetail(createInvoiceDetailForInterval(deliv, interval));
			// Versandkosten
			if (deliv.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, deliv.getShipmentCost());
			}
			if (from == null || interval.getStartDate().before(from)) {
				from = interval.getStartDate();
			}
			if (till == null || interval.getEndDate().after(till)) {
				till = interval.getEndDate();
			}
			deliv.setSettDetail(iad);
		}
		sub.setPayedUntil(till);
		
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(java.sql.Date.valueOf(till.toLocalDate().withDayOfMonth(till.toLocalDate().lengthOfMonth())));  // immer der letzte des Monats
		inv.addAgreementDetail(iad);
		return newDetail;
	}

	private  Settlement createSettlementSkeleton(Subscriber subscri) {
		Settlement inv = new Settlement();
		
		// formalia
		inv.setDate(new java.sql.Date(System.currentTimeMillis()));
		inv.setCreationTime(new java.sql.Timestamp(System.currentTimeMillis()));
		inv.setCustomerId(subscri.getCustomerId());
		inv.setDebitorId(subscri.getDebitorId());
		inv.setPointid(subscri.getPointid());
		inv.setAmount(0L);
		inv.setAmountFull(0L);
		inv.setAmountHalf(0L);
		inv.setAmountNone(0L);
		inv.setCollective(false);
		inv.setMerged(false);
		inv.setType("subscr");
		
		// Rechnungsadresse
		inv.setName1(subscri.getInvoiceAddress().getName1());
		inv.setName2(subscri.getInvoiceAddress().getName2());
		inv.setName3(subscri.getInvoiceAddress().getName3());
		inv.setStreet(subscri.getInvoiceAddress().getStreet());
		inv.setCity(subscri.getInvoiceAddress().getPostalcode() + " " + subscri.getInvoiceAddress().getCity());
		return inv;
	}
	
	private  PosInvoiceDetail createInvoiceDetailForInterval(SubscrIntervalDelivery deliv, SubscrInterval interval) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(deliv.getQuantity());
		detail.setText(interval.getName());
		detail.setSinglePrice(deliv.getTotal() / deliv.getQuantity());
		detail.setAmount(deliv.getTotal());
		detail.setAmountFull(deliv.getTotalFull());
		detail.setAmountHalf(deliv.getTotalHalf());
		detail.setAmountNone(0L); // Abos ohne MwSt können wir noch nicht.
		return detail;
	}

	private  PosInvoiceDetail addShipmentCostDetail(Settlement inv, long shipmentCost) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(1);
		detail.setText("Versandkosten");
		detail.setSinglePrice(shipmentCost);
		detail.setAmount(shipmentCost);
		// die Nebenleistung ereilt das Schicksal der Hauptleistung
		if (inv.getAmountFull() > inv.getAmountHalf()) {
			detail.setAmountFull(shipmentCost);
		} else {
			detail.setAmountHalf(shipmentCost);
		}
		inv.addInvoiceDetail(detail);
		return detail;
	}

	private PosInvoiceDetail createInvoiceDetailForDelivery(SubscrDelivery deliv, SubscrArticle art) {
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setQuantity(deliv.getQuantity());
		detail.setText(art.getName());
		detail.setSinglePrice(deliv.getTotal() / deliv.getQuantity());
		detail.setAmount(deliv.getTotal());
		detail.setAmountFull(deliv.getTotalFull());
		detail.setAmountHalf(deliv.getTotalHalf());
		detail.setAmountNone(0L); // Abos ohne MwSt können wir noch nicht.
		return detail;
	}

	private  void addTextDetail(Settlement inv, String deliveryInfo1) {
		if (deliveryInfo1 == null || deliveryInfo1.isEmpty()) {
			return;
		}
		PosInvoiceDetail detail = new PosInvoiceDetail();
		detail.setTextonly(true);
		detail.setText(deliveryInfo1);
		inv.addInvoiceDetail(detail);
	}

}
