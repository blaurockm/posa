package net.buchlese.verw.ctrl;


import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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
import net.buchlese.verw.repos.InvoiceRepository;
import net.buchlese.verw.repos.IssueSlipRepository;


@RestController
@RequestMapping(path="issueslips")
public class IssueSlipController {

	@Autowired IssueSlipRepository issueSlipRepository;

	@Autowired InvoiceRepository invoiceRepository;

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
