package com.falizsh.finance.portfolio.bankAccount.domain.repository.category;

import com.falizsh.finance.portfolio.bankAccount.domain.model.category.UserAccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountCategoryRepository extends JpaRepository<UserAccountCategory, Long> {
    Optional<UserAccountCategory> findById(Long id);
}
