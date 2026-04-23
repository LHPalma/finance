package com.falizsh.finance.portfolio.bankAccount.application.query.account;

import com.falizsh.finance.infrastructure.cqrs.QueryHandler;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.account.BankAccountDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FetchUsersBankAccountsHandler
        implements QueryHandler<FetchUsersBankAccountsQuery, Page<BankAccountDetail>> {

    private final BankAccountDetailRepository repository;

    @Override
    public Page<BankAccountDetail> handle(FetchUsersBankAccountsQuery query) {
        return repository.findAllByUserId(query.userId(), query.pageable());
    }
}
