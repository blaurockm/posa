package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.PosInvoice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueSlipRepository extends JpaRepository<PosInvoice, Long> {

}
