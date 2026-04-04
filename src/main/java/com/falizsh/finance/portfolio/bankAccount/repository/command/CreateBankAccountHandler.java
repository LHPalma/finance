package com.falizsh.finance.portfolio.bankAccount.repository.command;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.repository.UserRepository;
import com.falizsh.finance.infrastructure.exception.BusinessException;
import com.falizsh.finance.portfolio.bankAccount.model.BankAccount;
import com.falizsh.finance.portfolio.bankAccount.repository.BankAccountRepository;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.model.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.systemAccountType.repository.SystemAccountTypeRepository;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.model.UserAccountCategory;
import com.falizsh.finance.portfolio.bankAccount.userAccountCategory.repository.UserAccountCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class CreateBankAccountHandler {

    private final UserRepository userRepository;
    private final SystemAccountTypeRepository systemTypeRepository;
    private final UserAccountCategoryRepository categoryRepository;
    private final BankAccountRepository accountRepository;

    public BankAccount handle(CreateBankAccountCommand command) {

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        SystemAccountType systemType = systemTypeRepository.findById(command.systemTypeId())
                .orElseThrow(() -> new EntityNotFoundException("System account type not found"));

        UserAccountCategory category = null;

        if (command.userCategoryId() != null) {
            category = categoryRepository.findById(command.userCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        if (!systemType.isAllowsOverdraft()
                && command.overdraftLimit() != null
                && command.overdraftLimit().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException("This account type does not allow overdraft");
        }

        BankAccount account = BankAccount.create(
                user,
                command.name(),
                command.description(),
                systemType,
                category,
                command.overdraftLimit(),
                command.currency()
        );

        // TODO: implementar saldo inicial via transaction
        // if (command.initialBalance() != null) { ... }

        accountRepository.save(account);

        return account;
    }

}