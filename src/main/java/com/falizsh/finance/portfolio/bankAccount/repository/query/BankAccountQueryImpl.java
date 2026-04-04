package com.falizsh.finance.portfolio.bankAccount.repository.query;

import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.repository.BankAccountRepository;
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
