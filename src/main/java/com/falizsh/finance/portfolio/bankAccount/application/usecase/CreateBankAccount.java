package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.dto.account.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.application.command.account.CreateBankAccountCommand;
import com.falizsh.finance.portfolio.bankAccount.application.command.account.CreateBankAccountHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateBankAccount implements CreateBankAccountUseCase {

    private final CreateBankAccountHandler handler;

    public BankAccount execute(CreateBankAccountRequest request) {

        var command = new CreateBankAccountCommand(
                request.userId(),
                request.name(),
                request.description(),
                request.systemTypeId(),
                request.userCategoryId(),
                request.initialBalance(), // mantido mas ignorado
                request.overdraftLimit(),
                request.currency()
        );

        return handler.handle(command);
    }
}
