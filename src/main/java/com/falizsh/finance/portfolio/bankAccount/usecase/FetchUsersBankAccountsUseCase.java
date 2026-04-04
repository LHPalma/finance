package com.falizsh.finance.portfolio.bankAccount.usecase;

import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FetchUsersBankAccountsUseCase {

    Page<BankAccount> execute(Long userId, Pageable pageable);

}
