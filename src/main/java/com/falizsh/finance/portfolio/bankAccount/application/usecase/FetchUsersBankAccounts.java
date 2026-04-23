package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.query.account.FetchUsersBankAccountsHandler;
import com.falizsh.finance.portfolio.bankAccount.application.query.account.FetchUsersBankAccountsQuery;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchUsersBankAccounts implements FetchUsersBankAccountsUseCase {

    private final FetchUsersBankAccountsHandler handler;

    @Override
    public Page<BankAccountDetail> execute(Long userId, Pageable pageable) {
        return handler.handle(new FetchUsersBankAccountsQuery(userId, pageable));
    }
}
