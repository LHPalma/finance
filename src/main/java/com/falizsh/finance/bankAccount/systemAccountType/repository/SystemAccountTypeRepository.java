package com.falizsh.finance.bankAccount.systemAccountType.repository;

import com.falizsh.finance.bankAccount.systemAccountType.model.SystemAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemAccountTypeRepository extends JpaRepository<SystemAccountType, Long> {

    List<SystemAccountType> getAllByIsActiveIsTrue();

}
