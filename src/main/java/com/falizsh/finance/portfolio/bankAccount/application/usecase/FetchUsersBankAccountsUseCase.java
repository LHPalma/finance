package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FetchUsersBankAccountsUseCase {

    Page<BankAccountDetail> execute(Long userId, Pageable pageable);
}
