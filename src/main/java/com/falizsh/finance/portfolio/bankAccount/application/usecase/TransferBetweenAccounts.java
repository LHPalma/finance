package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.command.transfer.TransferBetweenAccountsCommand;
import com.falizsh.finance.portfolio.bankAccount.application.command.transfer.TransferBetweenAccountsHandler;
import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.request.TransferRequest;
import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransferBetweenAccounts implements TransferBetweenAccountsUseCase {

    private final TransferBetweenAccountsHandler handler;

    @Override
    public TransferResponse execute(TransferRequest request) {
        var command = new TransferBetweenAccountsCommand(
                request.userId(),
                request.sourceAccountId(),
                request.destinationAccountId(),
                request.amount(),
                request.description(),
                request.date()
        );
        return handler.handle(command);
    }
}
