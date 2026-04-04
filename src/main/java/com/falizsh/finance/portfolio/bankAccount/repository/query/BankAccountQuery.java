package com.falizsh.finance.portfolio.bankAccount.repository.query;

import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BankAccountQuery {

    Page<BankAccount> findAllByUserId(Long userId, Pageable pageable);

}
