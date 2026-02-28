package com.falizsh.finance.bankAccount.systemAccountType.repository;

import com.falizsh.finance.bankAccount.systemAccountType.model.SystemAccountType;

import java.util.List;

public interface SystemAccountTypeQuery {

    List<SystemAccountType> findAllActiveSystemAccountTypes();

    SystemAccountType findById(long typeId);
}
