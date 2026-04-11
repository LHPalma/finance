package com.falizsh.finance.portfolio.bankAccount.application.query.category;

import com.falizsh.finance.portfolio.bankAccount.domain.model.category.UserAccountCategory;

import java.util.Optional;

public interface UserAccountCategoryQuery {

    Optional<UserAccountCategory> findById(long id);

}
