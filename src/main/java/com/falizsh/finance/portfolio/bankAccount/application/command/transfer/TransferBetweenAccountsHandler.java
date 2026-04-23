package com.falizsh.finance.portfolio.bankAccount.application.command.transfer;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.identity.users.user.repository.UserRepository;
import com.falizsh.finance.infrastructure.cqrs.CommandHandler;
import com.falizsh.finance.infrastructure.exception.BusinessException;
import com.falizsh.finance.portfolio.bankAccount.application.dto.transfer.response.TransferResponse;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.BankAccountDetail;
import com.falizsh.finance.portfolio.bankAccount.domain.model.ledger.JournalEntry;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.account.BankAccountDetailRepository;
import com.falizsh.finance.portfolio.bankAccount.domain.repository.ledger.JournalEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class TransferBetweenAccountsHandler
        implements CommandHandler<TransferBetweenAccountsCommand, TransferResponse> {

    private final UserRepository userRepository;
    private final BankAccountDetailRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;

    @Override
    @Transactional
    public TransferResponse handle(TransferBetweenAccountsCommand command) {

        if (command.sourceAccountId().equals(command.destinationAccountId())) {
            throw new BusinessException("Conta de origem e destino não podem ser a mesma.");
        }

        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        BankAccountDetail source = accountRepository
                .findByIdAndUserId(command.sourceAccountId(), command.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta de origem não encontrada ou não pertence ao usuário."));

        BankAccountDetail destination = accountRepository
                .findByIdAndUserId(command.destinationAccountId(), command.userId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Conta de destino não encontrada ou não pertence ao usuário."));

        // Valida saldo e atualiza o cache antes de persistir o lançamento.
        // applyCredit lança BusinessException se o saldo for insuficiente.
        source.applyCredit(command.amount());      // Origem:  Ativo ↓
        destination.applyDebit(command.amount());  // Destino: Ativo ↑

        LocalDate date = command.date() != null ? command.date() : LocalDate.now();
        String description = command.description() != null
                ? command.description()
                : "Transferência de %s para %s".formatted(source.getName(), destination.getName());

        JournalEntry journalEntry = JournalEntry.createTransfer(
                user,
                description,
                source,
                destination,
                command.amount(),
                date
        );

        JournalEntry saved = journalEntryRepository.save(journalEntry);
        // Os saves nos accounts são cobertos pelo dirty-checking do JPA dentro da mesma transação.

        return TransferResponse.of()
                .journalEntryId(saved.getId())
                .sourceAccountId(source.getId())
                .destinationAccountId(destination.getId())
                .amount(command.amount())
                .sourceNewBalance(source.getBalance())
                .destinationNewBalance(destination.getBalance())
                .date(date)
                .description(description)
                .build();
    }
}
