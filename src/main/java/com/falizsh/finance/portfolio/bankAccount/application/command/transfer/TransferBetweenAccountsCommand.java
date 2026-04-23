package com.falizsh.finance.portfolio.bankAccount.application.command.transfer;

import com.falizsh.finance.infrastructure.cqrs.Command;
import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response.TransferResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferBetweenAccountsCommand(
        Long userId,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount,
        String description,
        LocalDate date
) implements Command<TransferResponse> {}
