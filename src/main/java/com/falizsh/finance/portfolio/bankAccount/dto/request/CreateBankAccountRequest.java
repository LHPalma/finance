package com.falizsh.finance.portfolio.bankAccount.dto.request;

import java.math.BigDecimal;

public record CreateBankAccountRequest(
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
