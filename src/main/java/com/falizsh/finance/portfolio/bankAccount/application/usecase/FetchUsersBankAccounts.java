package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.application.query.account.BankAccountQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchUsersBankAccounts implements FetchUsersBankAccountsUseCase {

    private final BankAccountQuery query;

    @Override
    public Page<BankAccount> execute(Long userId, Pageable pageable) {
        return query.findAllByUserId(userId, pageable);
    }
}
