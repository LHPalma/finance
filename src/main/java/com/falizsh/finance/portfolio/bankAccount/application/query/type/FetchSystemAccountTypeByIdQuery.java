package com.falizsh.finance.portfolio.bankAccount.application.query.type;

import com.falizsh.finance.infrastructure.cqrs.Query;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;

public record FetchSystemAccountTypeByIdQuery(long id) implements Query<SystemAccountType> {}
