package com.falizsh.finance.portfolio.bankAccount.domain.model.ledger;

import com.falizsh.finance.identity.users.user.model.User;
import com.falizsh.finance.infrastructure.exception.BusinessException;
import com.falizsh.finance.portfolio.bankAccount.domain.model.account.Account;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "journal_entry")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private JournalEntryStatus status = JournalEntryStatus.COMPLETED;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LedgerEntry> entries = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Cria um lançamento de saldo inicial (partidas dobradas).
     * Débita a conta bancária (Ativo ↑) e credita a conta de Equity (Capital Inicial ↑).
     */
    public static JournalEntry createOpeningBalance(
            User user,
            String accountName,
            Account debitAccount,
            Account creditAccount,
            BigDecimal amount
    ) {
        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .date(LocalDate.now())
                .description("Saldo inicial - " + accountName)
                .status(JournalEntryStatus.COMPLETED)
                .build();

        entry.debit(debitAccount, amount);
        entry.credit(creditAccount, amount);

        return entry;
    }

    /**
     * Cria um lançamento de transferência entre duas contas (partidas dobradas).
     *   CRÉDITO → conta de origem  (Ativo ↓ — dinheiro sai)
     *   DÉBITO  → conta de destino (Ativo ↑ — dinheiro chega)
     */
    public static JournalEntry createTransfer(
            User user,
            String description,
            Account sourceAccount,
            Account destinationAccount,
            BigDecimal amount,
            LocalDate date
    ) {
        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .date(date)
                .description(description)
                .status(JournalEntryStatus.COMPLETED)
                .build();

        entry.credit(sourceAccount, amount);     // Origem:  Ativo ↓
        entry.debit(destinationAccount, amount); // Destino: Ativo ↑

        return entry;
    }

    // -------------------------------------------------------------------------
    // Domain Behaviour
    // -------------------------------------------------------------------------

    /** Adiciona um lançamento a débito. */
    public void debit(Account account, BigDecimal amount) {
        entries.add(LedgerEntry.of(this, account, EntryType.DEBIT, amount));
    }

    /** Adiciona um lançamento a crédito. */
    public void credit(Account account, BigDecimal amount) {
        entries.add(LedgerEntry.of(this, account, EntryType.CREDIT, amount));
    }

    @PrePersist
    private void validateBeforeSave() {
        validateGoldenRule();
    }

    /**
     * Regra de Ouro do Ledger: a soma dos débitos deve ser igual à soma dos créditos.
     */
    public void validateGoldenRule() {
        BigDecimal totalDebits = sumByType(EntryType.DEBIT);
        BigDecimal totalCredits = sumByType(EntryType.CREDIT);

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new BusinessException(
                "Lançamento inválido: débitos (%s) != créditos (%s)."
                            .formatted(totalDebits.toPlainString(), totalCredits.toPlainString())
            );
        }
    }

    public List<LedgerEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    private BigDecimal sumByType(EntryType type) {
        return entries.stream()
                .filter(e -> e.getEntryType() == type)
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
