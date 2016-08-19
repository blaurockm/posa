package net.buchlese.verw.ctrl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import net.buchlese.bofc.api.bofc.AccountingBalanceExport;
import net.buchlese.bofc.api.bofc.PosCashBalance;
import net.buchlese.verw.repos.BalanceExportRepository;
import net.buchlese.verw.repos.BalanceRepository;

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
@RequestMapping(path="balances")
public class BalancesController {
	
	@Autowired BalanceRepository balanceRepository;

	@Autowired BalanceExportRepository exportRepository;

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
	public AccountingBalanceExport createBalanceExport(@RequestParam("pointid") Integer pointid) {
		PosCashBalance firstUnexported = balanceRepository.findFirstByExportedAndPointidOrderByAbschlussIdAsc(false, pointid);
		// Buchungsperiode es ersten Abschlusses ermitteln
		LocalDate tag = firstUnexported.getFirstCovered().toLocalDate();
		String maxAbschlussId = tag.withDayOfMonth(tag.lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		List<PosCashBalance> balToExp = balanceRepository.findAllByExportedAndPointidAndAbschlussIdLessThanEqualOrderByAbschlussId(false, pointid, maxAbschlussId);
		
		AccountingBalanceExport export = new AccountingBalanceExport();
		export.setDescription("..asd.");
		export.setExecDate(LocalDateTime.now());
		export.setPointId(pointid);
		export.setBalances(balToExp); // seiteneffekt: ändert die Balances auf "exported"
		
		exportRepository.saveAndFlush(export);
		
		return export;
	}
	
	
	@ResponseBody
	@RequestMapping(path="deleteExport", method = RequestMethod.DELETE)
	@Transactional
	public void deleteBalanceExport(@RequestParam("id") Long id) {
		AccountingBalanceExport export = exportRepository.findOne(id);
		Set<PosCashBalance> expBal = export.getBalances(); 
		expBal.forEach(PosCashBalance::unexport);
		exportRepository.delete(export);
		
		return;
	}

}
