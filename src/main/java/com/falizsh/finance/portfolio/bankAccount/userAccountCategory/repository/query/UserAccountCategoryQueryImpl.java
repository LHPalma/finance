package com.falizsh.finance.portfolio.bankAccount.userAccountCategory.repository.query;

import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.repository.UserAccountCategoryRepository;
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
