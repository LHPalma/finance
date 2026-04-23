package com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferRequest(
        @NotNull Long userId,
        @NotNull Long sourceAccountId,
        @NotNull Long destinationAccountId,
        @NotNull @Positive BigDecimal amount,
        String description,
        LocalDate date
) {}
