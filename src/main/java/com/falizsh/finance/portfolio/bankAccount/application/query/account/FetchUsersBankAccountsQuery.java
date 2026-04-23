package com.falizsh.finance.portfolio.bankAccount.application.query.account;

import com.falizsh.finance.infrastructure.cqrs.Query;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record FetchUsersBankAccountsQuery(
        Long userId,
        Pageable pageable
) implements Query<Page<BankAccountDetail>> {}
