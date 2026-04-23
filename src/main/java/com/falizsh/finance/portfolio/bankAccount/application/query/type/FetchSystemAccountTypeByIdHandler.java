package com.falizsh.finance.portfolio.bankAccount.application.query.type;

import com.falizsh.finance.infrastructure.cqrs.QueryHandler;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.type.SystemAccountTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FetchSystemAccountTypeByIdHandler
        implements QueryHandler<FetchSystemAccountTypeByIdQuery, SystemAccountType> {

    private final SystemAccountTypeRepository repository;

    @Override
    public SystemAccountType handle(FetchSystemAccountTypeByIdQuery query) {
        return repository.findById(query.id())
                .orElseThrow(() -> new EntityNotFoundException("Account.type.not.found"));
    }
}
