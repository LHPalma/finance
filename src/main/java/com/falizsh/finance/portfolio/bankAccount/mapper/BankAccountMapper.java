package com.falizsh.finance.portfolio.bankAccount.mapper;

import com.falizsh.finance.portfolio.bankAccount.dto.response.BankAccountResponse;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    public BankAccountResponse toResponse(BankAccount account) {
        if (account == null) {
            return null;
        }

        return BankAccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .description(account.getDescription())
                .type(account.getType().getName())
                .category(account.getCategory() != null ? account.getCategory().getName() : null)
                .overdraftLimit(account.getOverdraftLimit())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();
    }
}
