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

import net.buchlese.bofc.api.bofc.AccountingBalanceExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.bofc.api.bofc.QPosCashBalance;
import net.buchlese.verw.core.AccountingExportFile;
import net.buchlese.verw.reports.ReportBalanceExportCreator;
import net.buchlese.verw.reports.obj.ReportBalanceExport;
import net.buchlese.verw.repos.BalanceExportRepository;
import net.buchlese.verw.repos.BalanceRepository;


@RestController
@RequestMapping(path="balances")
public class BalancesController {
	
	@Autowired BalanceRepository balanceRepository;

	@Autowired BalanceExportRepository exportRepository;

	@Autowired AccountingExportFile exportFileCreator;

	@Autowired ReportBalanceExportCreator reportBalanceExport;

	@ResponseBody
	@RequestMapping(path="balancesDyn", method = RequestMethod.GET)
	public Page<PosCashBalance> balancesDynamic(@QuerydslPredicate(root = PosCashBalance.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return balanceRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="exportsDyn", method = RequestMethod.GET)
	public Page<AccountingBalanceExport> exportsDynamic(@QuerydslPredicate(root = AccountingBalanceExport.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return exportRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="createExport", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<AccountingBalanceExport> createBalanceExport(@RequestParam("pointid") Integer pointid, @RequestParam(name="exportLimit", required=false) Optional<String>  exportLimit) {
		LocalDate tagGrenze = null;
		if (exportLimit.isPresent() ) {
			tagGrenze = LocalDate.parse(exportLimit.get(), DateTimeFormatter.ISO_DATE_TIME);
		} else {
			LocalDate ausgangsTag = LocalDate.now();
			PosCashBalance firstUnexported = balanceRepository.findFirstByExportedAndPointidOrderByAbschlussIdAsc(false, pointid);
			// Buchungsperiode es ersten Abschlusses ermitteln
			if (firstUnexported != null) {
				ausgangsTag = firstUnexported.getFirstCovered().toLocalDate();
			}
			tagGrenze = ausgangsTag.withDayOfMonth(ausgangsTag.lengthOfMonth());
		}
		// Buchungsperiode es ersten Abschlusses ermitteln
		String maxAbschlussId = tagGrenze.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		List<PosCashBalance> balToExp = balanceRepository.findAllByExportedAndPointidAndAbschlussIdLessThanEqualOrderByAbschlussIdAsc(false, pointid, maxAbschlussId);
		if (balToExp.isEmpty()) {
			return new ResponseEntity<AccountingBalanceExport>(HttpStatus.NO_CONTENT);
		}

		AccountingBalanceExport export = new AccountingBalanceExport();
		export.setDescription("..asd.");
		export.setExecDate(LocalDateTime.now());
		export.setPointId(pointid);
		export.setBalances(balToExp); // seiteneffekt: ändert die Balances auf "exported"
		
		exportRepository.saveAndFlush(export);
		
		return new ResponseEntity<AccountingBalanceExport>(export, HttpStatus.OK);
	}
	
	
	@RequestMapping(path="deleteExport", method = RequestMethod.DELETE)
	@Transactional
	public void deleteBalanceExport(@RequestParam("id") Long id) {
		AccountingBalanceExport export = exportRepository.findOne(id);
		Set<PosCashBalance> expBal = export.getBalances(); 
		expBal.forEach(PosCashBalance::unexport);
		exportRepository.delete(export);
	}

	
	@RequestMapping(path="exportfile", method = RequestMethod.GET)
	@Transactional
	public ResponseEntity<?> getExportFile(@RequestParam("id") Long id) {
		AccountingBalanceExport export = exportRepository.findOne(id);
		
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
	public ReportBalanceExport getExportReport(@RequestParam("id") Long id) {
		AccountingBalanceExport export = exportRepository.findOne(id);
		return reportBalanceExport.createReport(export);
	}
	
	@RequestMapping(path="acceptBalance", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptInvoice(@RequestBody PosCashBalance cashBalance)  {
		try {
			PosCashBalance old = balanceRepository.findOne(QPosCashBalance.posCashBalance.abschlussId.eq(cashBalance.getAbschlussId()).and(QPosCashBalance.posCashBalance.pointid.eq(cashBalance.getPointid())));
			if (old == null) {
				balanceRepository.saveAndFlush(cashBalance);
			} else {
				cashBalance.setId(old.getId()); // damit wird gezeigt, dass wir ein update sind
				balanceRepository.saveAndFlush(cashBalance);
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}

}
