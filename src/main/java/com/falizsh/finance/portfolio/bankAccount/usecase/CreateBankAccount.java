package com.falizsh.finance.portfolio.bankAccount.usecase;

import com.falizsh.finance.portfolio.bankAccount.dto.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.repository.command.CreateBankAccountCommand;
import com.falizsh.finance.portfolio.bankAccount.repository.command.CreateBankAccountHandler;
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
