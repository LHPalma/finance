package com.falizsh.finance.portfolio.accounts.systemAccountType.repository;

import com.falizsh.finance.portfolio.accounts.systemAccountType.model.SystemAccountType;

import java.util.List;

public interface SystemAccountTypeQuery {

    List<SystemAccountType> findAllActiveSystemAccountTypes();

    SystemAccountType findById(long typeId);
}
