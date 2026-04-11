package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FetchUsersBankAccountsUseCase {

    Page<BankAccount> execute(Long userId, Pageable pageable);

}
