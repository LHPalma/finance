package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.command.account.CreateBankAccountCommand;
import com.falizsh.finance.portfolio.bankAccount.application.command.account.CreateBankAccountDetailHandler;
import com.falizsh.finance.portfolio.bankAccount.application.dto.account.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateBankAccount implements CreateBankAccountUseCase {

    private final CreateBankAccountDetailHandler handler;

    @Override
    public BankAccountDetail execute(CreateBankAccountRequest request) {
        var command = new CreateBankAccountCommand(
                request.userId(),
                request.name(),
                request.description(),
                request.systemTypeId(),
                request.userCategoryId(),
                request.initialBalance(),
                request.overdraftLimit(),
                request.currency()
        );

        return handler.handle(command);
    }
}
