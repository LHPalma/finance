package com.falizsh.finance.portfolio.bankAccount.usecase;

import com.falizsh.finance.portfolio.bankAccount.dto.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;

public interface CreateBankAccountUseCase {
    BankAccount execute(CreateBankAccountRequest request);
}
