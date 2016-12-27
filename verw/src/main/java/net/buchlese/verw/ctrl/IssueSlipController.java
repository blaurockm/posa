package net.buchlese.verw.ctrl;


import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.bofc.api.bofc.QPosIssueSlip;
import net.buchlese.verw.reports.ReportPdfCreator;
import net.buchlese.verw.repos.InvoiceRepository;
import net.buchlese.verw.repos.IssueSlipRepository;


@RestController
@RequestMapping(path="issueslips")
public class IssueSlipController {

	@Autowired IssueSlipRepository issueSlipRepository;

	@Autowired InvoiceRepository invoiceRepository;

	@Autowired ReportPdfCreator reportPdf;

	@PersistenceContext	EntityManager em;
	
	@ResponseBody
	@RequestMapping(path="issueslipsDyn", method = RequestMethod.GET)
	public Page<PosIssueSlip> invoicesDynamic(@QuerydslPredicate(root = PosIssueSlip.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return issueSlipRepository.findAll(predicate, pageable);
	}

	@RequestMapping(path="updateInclude", method = RequestMethod.GET)
	@Transactional
	public void updateMapping(@RequestParam("pointid") Integer pointid,@RequestParam("customerId") Integer customerId,@RequestParam("debitorId") Integer debitorId) {
		Session s = em.unwrap(Session.class);
		SQLQuery update = s.createSQLQuery("update posissueslip set debitor_id=? where pointid=? and customer_id=?");
		update.setInteger(0, debitorId);
		update.setInteger(1, pointid);
		update.setInteger(2, customerId);
		update.executeUpdate();
	}

	@RequestMapping(path="view", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getDeliveryNotePDF(@RequestParam("id") Long id) throws Exception {
		PosIssueSlip note = issueSlipRepository.findOne(id);
		
		byte[] pdf;
		try {
			pdf = reportPdf.createReport(note,"static/templates/report/deliveryNote.xsl", null);
			HttpHeaders respHeaders = new HttpHeaders();
			respHeaders.setContentType(MediaType.APPLICATION_PDF);
			respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Lieferschein_"+note.getNumber()+".pdf");
			
			return ResponseEntity.ok().headers(respHeaders).body(pdf);
		} catch (FOPException | JAXBException | TransformerException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
		
	}

	@RequestMapping(path="unprinted", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getUnprintedInvoicePDFs() throws Exception {
		List<PosIssueSlip> invs = issueSlipRepository.findAllByPrintedOrderByDateAscNumberAsc(false);
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);
			HttpHeaders respHeaders = new HttpHeaders();
			respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ungedruckte_Lieferscheine_"+LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)+".zip");
			for (PosIssueSlip inv : invs) {
				ZipEntry zipEntry = new ZipEntry("Rechnung_" + inv.getNumber() + ".pdf");
				zos.putNextEntry(zipEntry);
				byte[] pdf = reportPdf.createReport(inv,"static/templates/report/invoice.xsl", null);
				zos.write(pdf, 0, pdf.length);
				zos.closeEntry();
			}
			zos.close();
			return ResponseEntity.ok().headers(respHeaders).body(baos.toByteArray());
		} catch (FOPException | JAXBException | TransformerException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
	}

	@RequestMapping(path="markunprinted", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> markUnprintedInvoicesAsPrinted() throws Exception {
		List<PosIssueSlip> notes = issueSlipRepository.findAllByPrintedOrderByDateAscNumberAsc(false);
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);
			HttpHeaders respHeaders = new HttpHeaders();
			respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ungedruckte_Lieferscheine_"+LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)+".zip");
			for (PosIssueSlip note : notes) {
				ZipEntry zipEntry = new ZipEntry("Lieferschein_" + note.getNumber() + ".pdf");
				zos.putNextEntry(zipEntry);
				byte[] pdf = reportPdf.createReport(note,"static/templates/report/deliveryNote.xsl", null);
				zos.write(pdf, 0, pdf.length);
				zos.closeEntry();
				note.setPrinted(true);
				note.setPrintTime(new java.sql.Timestamp(System.currentTimeMillis()));
				issueSlipRepository.save(note);
			}
			zos.close();
			return ResponseEntity.ok().headers(respHeaders).body(baos.toByteArray());
		} catch (FOPException | JAXBException | TransformerException e) {
			return ResponseEntity.unprocessableEntity().body(e.getMessage());
		}
	}
	
	
	@RequestMapping(path="acceptIssueSlip", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptIssueSlip(@RequestBody PosIssueSlip issueSlip)  {
		try {
			// mapping der Debitor-Nummer
			Optional<Integer> debNo = invoiceRepository.mapDebitor(issueSlip.getPointid(), issueSlip.getCustomerId());
			issueSlip.setDebitorId(debNo.orElse(Integer.valueOf(0)));
			PosIssueSlip old = issueSlipRepository.findOne(QPosIssueSlip.posIssueSlip.number.eq(issueSlip.getNumber()));
			if (old == null) {
				issueSlipRepository.saveAndFlush(issueSlip);
			} else {
				issueSlip.setId(old.getId()); // damit wird gezeigt, dass wir ein update sind
				issueSlipRepository.saveAndFlush(issueSlip);
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}

}
