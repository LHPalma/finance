package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.dto.account.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccount;

public interface CreateBankAccountUseCase {
    BankAccount execute(CreateBankAccountRequest request);
}
