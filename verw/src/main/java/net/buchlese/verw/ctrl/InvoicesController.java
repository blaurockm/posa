package net.buchlese.verw.ctrl;


import java.time.LocalDateTime;
import java.util.Set;

import net.buchlese.bofc.api.bofc.AccountingInvoiceExport;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.verw.repos.InvoiceExportRepository;
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
@RequestMapping(path="invoices")
public class InvoicesController {

	@Autowired InvoiceRepository invoiceRepository;

	@Autowired InvoiceExportRepository exportRepository;

	
	@ResponseBody
	@RequestMapping(path="invoicesDyn", method = RequestMethod.GET)
	public Page<PosInvoice> invoicesDynamic(@QuerydslPredicate(root = PosInvoice.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return invoiceRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="exportsDyn", method = RequestMethod.GET)
	public Page<AccountingInvoiceExport> exportsDynamic(@QuerydslPredicate(root = AccountingInvoiceExport.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return exportRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="createExport", method = RequestMethod.GET)
	@Transactional
	public AccountingInvoiceExport createExport(@RequestParam("pointid") Integer pointid) {
		
		AccountingInvoiceExport export = new AccountingInvoiceExport();
		export.setDescription("..asd.");
		export.setExecDate(LocalDateTime.now());
		export.setPointId(pointid);
//		export.setBalances(balToExp); // seiteneffekt: ändert die Balances auf "exported"
		
		exportRepository.saveAndFlush(export);
		
		return export;
	}
	
	
	@ResponseBody
	@RequestMapping(path="deleteExport", method = RequestMethod.DELETE)
	@Transactional
	public void deleteBalanceExport(@RequestParam("id") Long id) {
		AccountingInvoiceExport export = exportRepository.findOne(id);
		Set<PosInvoice> expBal = export.getInvoices(); 
		expBal.forEach(PosInvoice::unexport);
		exportRepository.delete(export);
		
		return;
	}

}
