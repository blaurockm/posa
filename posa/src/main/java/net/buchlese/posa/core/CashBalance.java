package net.buchlese.posa.core;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.buchlese.posa.api.bofc.ArticleGroup;
import net.buchlese.posa.api.bofc.PaymentMethod;
import net.buchlese.posa.api.bofc.PosCashBalance;
import net.buchlese.posa.api.bofc.PosTicket;
import net.buchlese.posa.api.bofc.PosTx;
import net.buchlese.posa.api.bofc.Tax;
import net.buchlese.posa.api.bofc.TxType;
import net.buchlese.posa.jdbi.bofc.PosTicketDAO;

import org.joda.time.DateTime;

public class CashBalance {

	private final PosTicketDAO ticketDAO;

	private final Comparator<PosTx> txComparator;


	public CashBalance(PosTicketDAO ticketDAO) {
		super();
		this.ticketDAO = ticketDAO;
		txComparator = new Comparator<PosTx>() {
			@Override
			public int compare(PosTx o1, PosTx o2) {
				return o1.getTimestamp().compareTo(o2.getTimestamp());
			}
		};
	}

	public PosCashBalance computeBalanceFast(DateTime from, DateTime till) {
		PosCashBalance balance = createBalance(from, till);
		List<PosTx> txs = ticketDAO.fetchTxfast(from, till);
		List<PosTicket> tickets = ticketDAO.fetch(from, till);
		return computeBalance(balance, txs, tickets);
	}

	public PosCashBalance createBalance(DateTime from, DateTime till) {
		PosCashBalance balance = new PosCashBalance();
		balance.setCreationtime(new DateTime());
		balance.setExported(false);
		balance.setExportDate(null);
		balance.setFirstCovered(from);
		balance.setLastCovered(till);
		return balance;
	}
	

	/**
	 * Berechne die Summen für die geg Listen von Tickets und Transaktionen.
	 * 
	 * @param from   der Startzeitpunkt
	 * @param till   der Endezeitpunkt
	 * @param txs    die Liste der Transaktionen
	 * @param tickets  die Liste der Belege
	 * @return ein Tagesabschluss, Summen-Bildung
	 */
	public PosCashBalance computeBalance(PosCashBalance balance, List<PosTx> txs, List<PosTicket> tickets) {
		balance.setTicketCount((int) tickets.stream().filter(t -> (t.isCancel() && t.isCancelled()) == false).count());

		if (txs.isEmpty()) {
			return balance;
		}
		
		Optional<PosTx> lastTx = txs.stream().max(txComparator);
		Optional<PosTx> firstTx = txs.stream().min(txComparator);
		
		balance.setFirstBelegNr(firstTx.get().getBelegNr());
		balance.setFirstTimestamp(firstTx.get().getTimestamp());
		balance.setLastBelegNr(lastTx.get().getBelegNr());
		balance.setLastTimestamp(lastTx.get().getTimestamp());

		balance.setRevenue(txs.stream().mapToLong(PosTx::getTotal).sum());

		// tax-balance
		Map<Tax, Long> taxBal = txs.stream().filter(t -> t.getType().equals(TxType.SELL))
				.collect(Collectors.groupingBy(PosTx::getTax, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
				
		balance.setTaxBalance(taxBal);

		// paymentMethod-balance
		Map<PaymentMethod, Long> payBal = tickets.stream().collect(
				 Collectors.groupingBy(PosTicket::getPaymentMethod, 
						Collectors.reducing(0l,
								PosTicket::getTotal, Long::sum)));
				
		balance.setPaymentMethodBalance(payBal);

		// articleGroup-balance // nur die Verkäufe
		Map<String, Long> agrpBal = txs.stream().filter(t -> t.getType().equals(TxType.SELL))
				.collect(Collectors.groupingBy(PosTx::getArticleGroupKey, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
		balance.setArticleGroupBalance(agrpBal);
		long profit = 0;
		for (String articleGroupKey : agrpBal.keySet()) {
			if (articleGroupKey != null && ArticleGroup.getArticleGroups().containsKey(articleGroupKey) && ArticleGroup.getArticleGroups().get(articleGroupKey).getMarge() != null ) {
				profit += agrpBal.get(articleGroupKey) * ArticleGroup.getArticleGroups().get(articleGroupKey).getMarge();
			}
		}
		balance.setProfit(profit);

		// Gutschein-Buchungen
		balance.setCouponTradeIn(txs.stream().filter(t -> t.getType().equals(TxType.TRADEIN)).mapToLong(PosTx::getTotal).sum());
		balance.setCouponTradeOut(txs.stream().filter(t -> t.getType().equals(TxType.TRADEOUT)).mapToLong(PosTx::getTotal).sum());

		// Bar ein und auszahlungen
		Map<String, Long> cashIn = txs.stream().filter(t -> t.getType().equals(TxType.CASHIN))
				.collect(Collectors.groupingBy(PosTx::getArticleGroupKey, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
		balance.setCashIn(cashIn);
		
		Map<String, Long> cashOut = txs.stream().filter(t -> t.getType().equals(TxType.CASHOUT))
				.collect(Collectors.groupingBy(PosTx::getArticleGroupKey, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
		balance.setCashOut(cashOut);

		balance.setCashInSum(txs.stream().filter(t -> t.getType().equals(TxType.CASHIN)).mapToLong(PosTx::getTotal).sum());
		balance.setCashOutSum(txs.stream().filter(t -> t.getType().equals(TxType.CASHOUT)).mapToLong(PosTx::getTotal).sum());

		balance.setGoodsOut(txs.stream().filter(t -> t.getType().equals(TxType.SELL)).mapToLong(PosTx::getTotal).sum());


		// verkaufteGutscheine
		Map<String, Long> newCoup = txs.stream().filter(t -> t.getType().equals(TxType.TRADEOUT))
				.collect(Collectors.groupingBy(PosTx::getArticleGroupKey, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
		balance.setNewCoupon(newCoup);

		// eingelöste Gutscheine
		Map<String, Long> oldCoup = txs.stream().filter(t -> t.getType().equals(TxType.TRADEIN))
				.collect(Collectors.groupingBy(PosTx::getArticleGroupKey, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
		balance.setOldCoupon(oldCoup);

		// Bezahlte Rechnungen
		Map<String, Long> debitPays = txs.stream().filter(t -> t.getType().equals(TxType.DEBITPAY))
				.collect(Collectors.groupingBy(PosTx::getMatchCode, 
						Collectors.reducing(0l,
								PosTx::getTotal, Long::sum)));
		balance.setPayedInvoices(debitPays);

		balance.setPayedInvoicesSum(txs.stream().filter(t -> t.getType().equals(TxType.DEBITPAY)).mapToLong(PosTx::getTotal).sum());

//		balance.setCreatedInvoicesSum(txs.stream().filter(t -> t.getType().equals(TxType.DEBITPAY)).mapToLong(PosTx::getTotal).sum());

		
		balance.setCancelledticketCount(txs.size());
		return balance;
	}
	
	
}
