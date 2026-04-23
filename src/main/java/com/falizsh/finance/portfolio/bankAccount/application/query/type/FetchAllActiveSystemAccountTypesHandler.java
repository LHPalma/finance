package com.falizsh.finance.portfolio.bankAccount.application.query.type;

import com.falizsh.finance.infrastructure.cqrs.QueryHandler;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.type.SystemAccountTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FetchAllActiveSystemAccountTypesHandler
        implements QueryHandler<FetchAllActiveSystemAccountTypesQuery, List<SystemAccountType>> {

    private final SystemAccountTypeRepository repository;

    @Override
    public List<SystemAccountType> handle(FetchAllActiveSystemAccountTypesQuery query) {
        return repository.getAllByIsActiveIsTrue();
    }
}
