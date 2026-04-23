package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.dto.account.request.CreateBankAccountRequest;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;

public interface CreateBankAccountUseCase {
    BankAccountDetail execute(CreateBankAccountRequest request);
}
