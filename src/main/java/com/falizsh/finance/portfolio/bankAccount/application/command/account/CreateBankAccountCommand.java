package com.falizsh.finance.portfolio.bankAccount.application.command.account;

import com.falizsh.finance.infrastructure.cqrs.Command;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;

import java.math.BigDecimal;

public record CreateBankAccountCommand(
        Long userId,
        String name,
        String description,
        Long systemTypeId,
        Long userCategoryId,
        BigDecimal initialBalance,
        BigDecimal overdraftLimit,
        String currency
) implements Command<BankAccountDetail> {}
