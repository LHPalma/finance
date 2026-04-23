package com.falizsh.finance.portfolio.bankAccount.application.command.account;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.repository.UserRepository;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.Account;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.AccountNature;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import com.falizsh.finance.portfolio.bankAccount.domain.model.ledger.JournalEntry;
import com.falizsh.finance.portfolio.bankAccount.domain.model.type.SystemAccountType;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.account.AccountRepository;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.account.BankAccountDetailRepository;
import com.falizsh.finance.infrastructure.cqrs.CommandHandler;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.ledger.JournalEntryRepository;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.type.SystemAccountTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Component
public class CreateBankAccountDetailHandler
        implements CommandHandler<CreateBankAccountCommand, BankAccountDetail> {

    private final UserRepository userRepository;
    private final SystemAccountTypeRepository systemTypeRepository;
    private final BankAccountDetailRepository repository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Override
    @Transactional
    public BankAccountDetail handle(CreateBankAccountCommand command) {

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        SystemAccountType systemType = systemTypeRepository.findById(command.systemTypeId())
                .orElseThrow(() -> new EntityNotFoundException("System account type not found"));

        BankAccountDetail account = BankAccountDetail.create(
                user,
                command.name(),
                command.description(),
                systemType,
                command.initialBalance(),
                command.overdraftLimit(),
                command.currency()
        );

        BankAccountDetail savedAccount = repository.save(account);

        if (hasInitialBalance(command.initialBalance())) {
            registerOpeningBalanceEntry(user, savedAccount, command.initialBalance());
        }

        return savedAccount;
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private boolean hasInitialBalance(BigDecimal initialBalance) {
        return initialBalance != null && initialBalance.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Gera o lançamento contábil de saldo inicial:
     *   DÉBITO  → conta bancária (Ativo sobe)
     *   CRÉDITO → conta de Capital Inicial do usuário (Equity sobe)
     *
     * A conta de Equity é criada automaticamente na primeira vez que o usuário
     * registra um saldo inicial; nas vezes seguintes a existente é reutilizada.
     */
    private void registerOpeningBalanceEntry(User user, BankAccountDetail bankAccount, BigDecimal amount) {
        Account equityAccount = findOrCreateEquityAccount(user);

        JournalEntry journalEntry = JournalEntry.createOpeningBalance(
                user,
                bankAccount.getName(),
                bankAccount,   // DEBIT  — Ativo ↑
                equityAccount, // CREDIT — Equity ↑
                amount
        );

        journalEntryRepository.save(journalEntry);
    }

    /**
     * Busca a conta de Capital Inicial do usuário ou cria uma nova caso não exista.
     * Garante que cada usuário tenha no máximo uma conta de Equity deste tipo.
     */
    private Account findOrCreateEquityAccount(User user) {
        return accountRepository
                .findFirstByUserIdAndNature(user.getId(), AccountNature.EQUITY)
                .orElseGet(() -> accountRepository.save(Account.createOpeningBalanceEquity(user)));
    }
}
