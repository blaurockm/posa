package net.buchlese.verw.repos;

import net.buchlese.bofc.api.bofc.PosCashBalance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<PosCashBalance, Long> {

}
