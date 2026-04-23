package com.falizsh.finance.portfolio.bankAccount.application.usecase;

import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.request.TransferRequest;
import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response.TransferResponse;

public interface TransferBetweenAccountsUseCase {

    TransferResponse execute(TransferRequest request);
}
