package com.falizsh.finance.portfolio.bankAccount.domain.repository.account;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.Account;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.AccountNature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findFirstByUserIdAndNature(Long userId, AccountNature nature);
}
