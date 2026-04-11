package com.falizsh.finance.portfolio.bankAccount.infrastructure.persistence.query.account;

import com.falizsh.finance.portfolio.bankAccount.application.query.account.BankAccountQuery;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.account.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BankAccountQueryImpl implements BankAccountQuery {

    private final BankAccountRepository repository;

    @Override
    public Page<BankAccount> findAllByUserId(Long userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }
}
