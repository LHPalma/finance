package com.falizsh.finance.portfolio.bankAccount.application.query.account;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankAccountQuery {

    Page<BankAccount> findAllByUserId(Long userId, Pageable pageable);

}
