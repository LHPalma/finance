package com.falizsh.finance.bankAccount.systemAccountType.repository;

import com.falizsh.finance.bankAccount.systemAccountType.model.SystemAccountType;
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
