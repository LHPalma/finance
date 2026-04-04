package com.falizsh.finance.portfolio.bankAccount.repository.command;

import java.math.BigDecimal;

public record CreateBankAccountCommand(
        Long userId,
        String name,
        String description,
        Long systemTypeId,
        Long userCategoryId,
        BigDecimal initialBalance, // TODO: implementar via transaction futuramente
        BigDecimal overdraftLimit,
        String currency
) {
}