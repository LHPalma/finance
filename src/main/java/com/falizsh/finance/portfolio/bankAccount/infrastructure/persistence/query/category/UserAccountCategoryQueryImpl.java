package com.falizsh.finance.portfolio.bankAccount.infrastructure.persistence.query.category;

import com.falizsh.finance.portfolio.bankAccount.domain.model.category.UserAccountCategory;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.category.UserAccountCategoryRepository;
import com.falizsh.finance.portfolio.bankAccount.application.query.category.UserAccountCategoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountCategoryQueryImpl implements UserAccountCategoryQuery {

    private final UserAccountCategoryRepository repository;

    @Override
    public Optional<UserAccountCategory> findById(long categoryId) {
        return repository.findById(categoryId);
    }
}
