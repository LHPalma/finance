package com.falizsh.finance.portfolio.bankAccount.domain.repository.account;

import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountDetailRepository extends JpaRepository<BankAccountDetail, Long> {

    Page<BankAccountDetail> findAllByUserId(Long userId, Pageable pageable);

    Optional<BankAccountDetail> findByIdAndUserId(Long id, Long userId);
}
