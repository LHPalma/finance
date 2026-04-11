package com.falizsh.finance.portfolio.bankAccount.domain.repository.type;

import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemAccountTypeRepository extends JpaRepository<SystemAccountType, Long> {

    List<SystemAccountType> getAllByIsActiveIsTrue();

}
