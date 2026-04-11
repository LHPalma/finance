package com.falizsh.finance.portfolio.bankAccount.infrastructure.persistence.query.type;

import com.falizsh.finance.portfolio.bankAccount.application.query.type.SystemAccountTypeQuery;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.type.SystemAccountTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SystemAccountTypeQueryImpl implements SystemAccountTypeQuery {

    private final SystemAccountTypeRepository repository;

    @Override
    public List<SystemAccountType> findAllActiveSystemAccountTypes() {
        return repository.getAllByIsActiveIsTrue();
    }

    @Override
    public SystemAccountType findById(long typeId) {
        return repository.findById(typeId)
                .orElseThrow(() -> new EntityNotFoundException("Account.type.not.found"));
    }
}
