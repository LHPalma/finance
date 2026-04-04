package com.falizsh.finance.portfolio.bankAccount.repository;

import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Page<BankAccount> findAllByUserId(Long userId, Pageable pageable);

}
