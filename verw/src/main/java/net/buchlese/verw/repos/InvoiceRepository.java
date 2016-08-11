package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.PosIssueSlip;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<PosIssueSlip, Long> {

}
