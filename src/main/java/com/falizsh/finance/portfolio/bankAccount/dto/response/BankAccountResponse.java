package com.falizsh.finance.portfolio.bankAccount.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BankAccountResponse(
        Long id,
        String name,
        String description,
        String type,
        String category,
        BigDecimal overdraftLimit,
        BigDecimal balance,
        String currency
) {
}
