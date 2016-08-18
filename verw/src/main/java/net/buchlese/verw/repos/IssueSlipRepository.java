package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.PosIssueSlip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "issueslips", path = "issueslip")
public interface IssueSlipRepository extends JpaRepository<PosIssueSlip, Long> {

}
