package com.falizsh.finance.portfolio.bankAccount.userAccountCategory.repository;

import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountCategoryRepository extends JpaRepository<UserAccountCategory, Long> {
    Optional<UserAccountCategory> findById(Long id);
}
