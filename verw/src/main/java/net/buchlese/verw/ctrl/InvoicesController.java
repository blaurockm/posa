package net.buchlese.verw.ctrl;


import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import net.buchlese.bofc.api.bofc.AccountingInvoiceExport;
import net.buchlese.bofc.api.bofc.PosInvoice;
import net.buchlese.bofc.api.bofc.QPosInvoice;
import net.buchlese.verw.core.AccountingExportFile;
import net.buchlese.verw.reports.ReportInvoiceExportCreator;
import net.buchlese.verw.reports.obj.ReportInvoiceExport;
import net.buchlese.verw.repos.InvoiceExportRepository;
import net.buchlese.verw.repos.InvoiceRepository;


@RestController
@RequestMapping(path="invoices")
public class InvoicesController {

	@Autowired InvoiceRepository invoiceRepository;

	@Autowired InvoiceExportRepository exportRepository;

	@Autowired AccountingExportFile exportFileCreator;

	@Autowired ReportInvoiceExportCreator reportInvoiceExport;

	@PersistenceContext	EntityManager em;
	
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
	public ResponseEntity<AccountingInvoiceExport> createExport(@RequestParam("pointid") Integer pointid, @RequestParam(name="exportLimit", required=false) Optional<String>  exportLimit) {
		LocalDate tagGrenze = null;
		if (exportLimit.isPresent() ) {
			tagGrenze = LocalDate.parse(exportLimit.get(), DateTimeFormatter.ISO_DATE_TIME);
		} else {
			LocalDate ausgangsTag = LocalDate.now();
			PosInvoice firstUnexported = invoiceRepository.findFirstByExportedAndPointidOrderByDateAsc(false, pointid);
			// Buchungsperiode es ersten Abschlusses ermitteln
			if (firstUnexported != null) {
				ausgangsTag = firstUnexported.getDate();
			}
			tagGrenze = ausgangsTag.withDayOfMonth(ausgangsTag.lengthOfMonth());
		}
		List<PosInvoice> balToExp = invoiceRepository.findAllByExportedAndPointidAndDateLessThanEqualOrderByDateAsc(false, pointid, tagGrenze);
		
		if (balToExp.isEmpty()) {
			return new ResponseEntity<AccountingInvoiceExport>(HttpStatus.NO_CONTENT);
		}
		AccountingInvoiceExport export = new AccountingInvoiceExport();
		export.setDescription("..asd.");
		export.setExecDate(LocalDateTime.now());
		export.setPointId(pointid);
		export.setInvoices(balToExp); // seiteneffekt: ändert die Balances auf "exported"
		
		exportRepository.saveAndFlush(export);
		
		return new ResponseEntity<AccountingInvoiceExport>(export, HttpStatus.OK);
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

	@RequestMapping(path="exportfile", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getExportFile(@RequestParam("id") Long id) {
		AccountingInvoiceExport export = exportRepository.findOne(id);
		
		StringWriter result = new StringWriter();
		try {
			exportFileCreator.createFile(export, result);
		} catch (IOException e) {
			throw new HttpMessageConversionException("problem creating file " + e.getMessage());
		}
		
		HttpHeaders respHeaders = new HttpHeaders();
		respHeaders.setContentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.ISO_8859_1));
		respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wochenliste.csv");
		
		return ResponseEntity.ok().headers(respHeaders).body(result.toString());
	}

	@ResponseBody
	@RequestMapping(path="exportreport", method = RequestMethod.GET)
	@Transactional
	public ReportInvoiceExport getExportReport(@RequestParam("id") Long id) {
		AccountingInvoiceExport export = exportRepository.findOne(id);
		return reportInvoiceExport.createReport(export);
	}

	@RequestMapping(path="updateMapping", method = RequestMethod.GET)
	@Transactional
	public void updateMapping(@RequestParam("pointid") Integer pointid,@RequestParam("customerId") Integer customerId,@RequestParam("debitorId") Integer debitorId) {
		Session s = em.unwrap(Session.class);
		SQLQuery update = s.createSQLQuery("update posinvoice set debitor_id=? where pointid=? and customer_id=?");
		update.setInteger(0, debitorId);
		update.setInteger(1, pointid);
		update.setInteger(2, customerId);
		update.executeUpdate();
	}

	@RequestMapping(path="acceptInvoice", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptInvoice(@RequestBody PosInvoice invoice)  {
		try {
			// mapping der Debitor-Nummer
			Optional<Integer> debNo = invoiceRepository.mapDebitor(invoice.getPointid(), invoice.getCustomerId());
			invoice.setDebitorId(debNo.orElse(Integer.valueOf(0)));
			PosInvoice old = invoiceRepository.findOne(QPosInvoice.posInvoice.number.eq(invoice.getNumber()));
			if (old == null) {
				invoiceRepository.saveAndFlush(invoice);
			} else {
				invoice.setId(old.getId()); // damit wird gezeigt, dass wir ein update sind
				invoice.setExported(old.isExported());
				invoice.setExportDate(old.getExportDate());
				invoiceRepository.saveAndFlush(invoice);
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}
	
}
