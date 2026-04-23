package com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record TransferResponse(
        Long journalEntryId,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        BigDecimal sourceNewBalance,
        BigDecimal destinationNewBalance,
        LocalDate date,
        String description
) {
    public static TransferResponseBuilder of() {
        return builder();
    }
}
