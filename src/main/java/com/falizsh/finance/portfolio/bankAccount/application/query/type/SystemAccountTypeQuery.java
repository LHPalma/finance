package com.falizsh.finance.portfolio.bankAccount.application.query.type;

import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;

import java.util.List;

public interface SystemAccountTypeQuery {

    List<SystemAccountType> findAllActiveSystemAccountTypes();

    SystemAccountType findById(long typeId);
}
