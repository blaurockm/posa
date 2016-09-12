package net.buchlese.verw.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	 * @param dao
	 * @param subscriber
	 * @return
	 */
	public Settlement createCollectiveSubscription(Subscriber subscriber) {
		List<PosIssueSlip> issueSlips = issueSlipRepository.findByDebitorIdAndPayed(subscriber.getDebitorId(), false);
		List<Subscription> subscriptions = subscriber.getSubscriptions();
		Settlement inv = createTemporaryInvoice(subscriber, subscriptions, issueSlips);
		inv.setCollective(true);
		return inv;
	}

	/**
	 * Einzelrechnung für ein Abo 
	 * @param dao
	 * @param sub
	 * @return
	 */
	public Settlement createSubscription(Subscription sub) {
		Subscriber cust = customerRepository.findOne(sub.getSubscriberId());
		return createTemporaryInvoice(cust, Arrays.asList(sub), null);
	}

	/**
	 * festschreiben einer Rechnung bei den Abos die dvon der Rechnung betroffen sind.
	 * Die Rechnung selbst wird nicht angefasst
	 * @param dao
	 * @param inv
	 */
	private void recordInvoiceOnAgreements(Settlement inv) {
		for (InvoiceAgrDetail iad : inv.getAgreementDetails()) {
			if (InvoiceAgrDetail.TYPE.SUBSCR.equals(iad.getType())) {
				Subscription sub = subscriptionRepository.findOne(iad.getAgreementId());
				sub.setPayedUntil(iad.getDeliveryTill());
				if (iad.getPayType() != null && iad.getPayType().equals(PayIntervalType.EACHDELIVERY)) {
					
//					SubscrDelivery del = subscrDeliveryRepository.findOne(arg0)
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
	public void unrecordInvoiceOnAgreements(Settlement inv) {
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
	 * erzeugt eine komplette Rechnung und legt sie als temporär ab.
	 * 
	 * @param dao
	 * @param subscriber
	 * @param subs
	 * @param issueSlips
	 * @param numgen
	 * @return
	 */
	private Settlement createTemporaryInvoice(Subscriber subscriber, List<Subscription> subs, List<PosIssueSlip> issueSlips) {
		final Settlement inv = createInvoiceSkeleton(subscriber);
		// die Abos nach den Lieferhinweisen sortieren
		Collections.sort(subs, new InvoiceSubComparator());
		// details
		String lastDetailInfo = null;
		for(Subscription sub : subs) {
			if (PayIntervalType.EACHDELIVERY.equals(sub.getPaymentType())) {
				List<SubscrDelivery> deliveries = new ArrayList<>(sub.getArticleDeliveries()); 
// TODO payed = false						subscrDeliveryRepository.findBySubscriptionIdAndPayed(sub.getId(), false);
				lastDetailInfo = addDeliveriesToInvoice(sub, inv, deliveries, lastDetailInfo);
			} else {
				List<SubscrIntervalDelivery> intdeliveries = new ArrayList<>(sub.getIntervalDeliveries()); 
// TODO payed = false						subscrIntervalDeliveryRepository.findBySubscriptionIdAndPayed(sub.getId(), false);
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
	private static void updateTaxValues(Settlement inv) {
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
		iad.setAgreementId(slip.getId());
		iad.setType(InvoiceAgrDetail.TYPE.ISSUESLIP);
		addTextDetail(inv, "Artikel des Lieferscheins " + slip.getNumber() + " vom " + slip.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
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
		LocalDate from = null;
		LocalDate till = null;
		// details per Delivery;
		for (SubscrDelivery deliv : deliveries) {
			SubscrArticle art = subscrArticleRepository.findOne(deliv.getArticleId());
			inv.addInvoiceDetail(createInvoiceDetailForDelivery(deliv,art));
			// Versandkosten
			if (deliv.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, deliv.getShipmentCost());
			}
			if (from == null || deliv.getDeliveryDate().isBefore(from)) {
				from = deliv.getDeliveryDate();
			}
			if (till == null || deliv.getDeliveryDate().isAfter(till)) {
				till = deliv.getDeliveryDate();
			}
		}
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		iad.setAgreementId(sub.getId());
		iad.setPayType(sub.getPaymentType());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.withDayOfMonth(till.lengthOfMonth()));  // immer der letzte des Monats
		//TODO iad.setDeliveryIds(deliveries.stream().mapToLong(SubscrDelivery::getId).boxed().collect(Collectors.toList()));
		inv.addAgreementDetail(iad);
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
		LocalDate from = null;
		LocalDate till = null;
		// details per Delivery;
		for (SubscrIntervalDelivery deliv : deliveries) {
			SubscrInterval interval = subscrIntervalRepository.findOne(deliv.getIntervalId());
			inv.addInvoiceDetail(createInvoiceDetailForInterval(deliv, interval));
			// Versandkosten
			if (deliv.getShipmentCost() > 0) {
				addShipmentCostDetail(inv, deliv.getShipmentCost());
			}
			if (from == null || interval.getStartDate().isBefore(from)) {
				from = interval.getStartDate();
			}
			if (till == null || interval.getEndDate().isAfter(till)) {
				till = interval.getEndDate();
			}
		}
		sub.setPayedUntil(till);
		
		InvoiceAgrDetail iad = new InvoiceAgrDetail();
		iad.setAgreementId(sub.getId());
		iad.setPayType(sub.getPaymentType());
		iad.setDeliveryFrom(from);
		iad.setDeliveryTill(till.withDayOfMonth(till.lengthOfMonth()));  // immer der letzte des Monats
//		TODO iad.setDeliveryIds(deliveries.stream().mapToLong(SubscrIntervalDelivery::getId).boxed().collect(Collectors.toList()));
		inv.addAgreementDetail(iad);
		return newDetail;
	}

	private  Settlement createInvoiceSkeleton(Subscriber subscri) {
		Settlement inv = new Settlement();
		
		// formalia
		inv.setDate(LocalDate.now());
		inv.setCreationTime(LocalDateTime.now());
		inv.setCustomerId(subscri.getCustomerId());
		inv.setDebitorId(subscri.getDebitorId());
		inv.setPointid(subscri.getPointid());
		inv.setAmount(0L);
		inv.setAmountFull(0L);
		inv.setAmountHalf(0L);
		inv.setAmountNone(0L);
		inv.setCollective(false);
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
