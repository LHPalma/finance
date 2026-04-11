package com.falizsh.finance.portfolio.bankAccount.domain.repository.account;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Page<BankAccount> findAllByUserId(Long userId, Pageable pageable);

}
