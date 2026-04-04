package com.falizsh.finance.portfolio.bankAccount.userAccountCategory.repository.query;

import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;

import java.util.Optional;

public interface UserAccountCategoryQuery {

    Optional<UserAccountCategory> findById(long id);

}
