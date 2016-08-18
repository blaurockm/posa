package net.buchlese.verw.ctrl;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import net.buchlese.bofc.api.bofc.AccountingExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.verw.repos.BalanceRepository;
import net.buchlese.verw.repos.ExportRepository;
import net.buchlese.verw.repos.InvoiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;


@RestController
@RequestMapping(path="accounting")
public class AccountingController {
	
	@Autowired BalanceRepository balanceRepository;

	@Autowired InvoiceRepository invoiceRepository;

	@Autowired ExportRepository exportRepository;

	@ResponseBody
	@RequestMapping(path="balancesDyn", method = RequestMethod.GET)
	public Page<PosCashBalance> balancesDynamic(@QuerydslPredicate(root = PosCashBalance.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return balanceRepository.findAll(predicate, pageable);
	}
	
	
	@ResponseBody
	@RequestMapping(path="invoicesDyn", method = RequestMethod.GET)
	public Page<PosInvoice> invoicesDynamic(@QuerydslPredicate(root = PosInvoice.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return invoiceRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="exportsDyn", method = RequestMethod.GET)
	public Page<AccountingExport> exportsDynamic(@QuerydslPredicate(root = AccountingExport.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return exportRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="createBalanceExport", method = RequestMethod.GET)
	@Transactional
	public String createBalanceExport(@RequestParam int pointid) {
		PosCashBalance firstUnexported = balanceRepository.findFirstByExportedAndPointidOrderByAbschlussIdAsc(false, pointid);
		// Buchungsperiode es ersten Abschlusses ermitteln
		LocalDate tag = firstUnexported.getFirstCovered().toLocalDate();
		String maxAbschlussId = tag.withDayOfMonth(tag.lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		List<PosCashBalance> balToExp = balanceRepository.findAllByExportedAndPointidAndAbschlussIdLessThanEqualOrderByAbschlussId(false, pointid, maxAbschlussId);
		
		AccountingExport export = new AccountingExport();
		export.setKey(1);
		export.setExecDate(LocalDate.now());
		export.setFrom(balToExp.get(0).getFirstCovered().toLocalDate());
		export.setTill(balToExp.get(balToExp.size()-1).getLastCovered().toLocalDate());
		export.addBalances(balToExp);
		
		exportRepository.saveAndFlush(export);
		
		return "erfolgreich"+balToExp.size();
	}
}
